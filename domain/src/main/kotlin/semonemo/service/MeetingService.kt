package semonemo.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import semonemo.model.invitation.AttendanceUpdateRequest
import semonemo.model.meeting.MeetingSaveRequest
import semonemo.model.invitation.WantToAttendRequest
import semonemo.model.invitation.Invitation
import semonemo.model.meeting.Meeting
import semonemo.model.entity.User
import semonemo.model.exception.ForbiddenException
import semonemo.repository.BlacklistRepository
import semonemo.repository.MeetingRepository
import semonemo.repository.CountersRepository
import semonemo.repository.InvitationRepository
import java.time.LocalDateTime

@Service
class MeetingService(
    private val meetingRepository: MeetingRepository,
    private val invitationRepository: InvitationRepository,
    private val countersRepository: CountersRepository,
    private val blacklistRepository: BlacklistRepository,
) {

    @Transactional
    fun saveMeeting(host: User, request: MeetingSaveRequest): Mono<Meeting> =
        Mono.zip(countersRepository.findById("meetingId"), countersRepository.findById("invitationId"))
            .flatMap { tuple ->
                val meetingCounter = tuple.t1
                val invitationCounter = tuple.t2
                val meeting = request.toMeeting(meetingCounter.seq, host)

                meetingCounter.increaseSeqOne()
                invitationCounter.increaseSeqOne()

                Mono.zip(
                    countersRepository.save(meetingCounter),
                    countersRepository.save(invitationCounter),
                    meetingRepository.save(meeting),
                    invitationRepository.save(Invitation.host(invitationCounter.seq, meeting, host)),
                )
            }
            .flatMap { tuple -> Mono.just(tuple.t3) }
            .onErrorResume { Mono.defer { Mono.error(it) } }

    @Transactional(readOnly = true)
    fun findMeetings(now: LocalDateTime, user: User): Flux<Meeting> =
        Flux.concat(
            meetingRepository.findAllByStatusAndEndDateAfterOrderByStartDate(endDate = now),
            meetingRepository.findAllByStatusAndEndDateBeforeOrderByStartDate(endDate = now),
        ).concatMap { mergeWithParticipants(it) }
            .filterWhen { isUserInvited(it, user) }
            .collectList()
            .flatMap { findMeetingsFilteredByBlacklist(it, user) }
            .flatMapIterable { it }

    // TODO: 초대받지 않은 모임은 조회 권한이 없어야 함
    @Transactional(readOnly = true)
    fun findMeeting(id: Long, user: User): Mono<Meeting> =
        meetingRepository.findById(id)
            .switchIfEmpty(Mono.defer { Mono.error(IllegalArgumentException("존재하지 않는 모임입니다.")) })
            .flatMap { mergeWithParticipants(it) }
            .filterWhen { isNotBlacklistMeeting(it, user) }
            .switchIfEmpty(Mono.defer { Mono.error(IllegalArgumentException("신고된 모임입니다.")) })

    @Transactional
    fun removeMeeting(user: User, id: Long): Mono<Meeting> = meetingRepository.findById(id)
        .switchIfEmpty(Mono.defer { Mono.error(IllegalArgumentException("존재하지 않는 모임입니다.")) })
        .flatMap { meeting ->
            if (meeting.hostUserId != user.id) {
                return@flatMap Mono.defer { Mono.error(ForbiddenException("권한이 없는 유저입니다. (호스트가 아님)")) }
            }

            if (meeting.isRemoved) {
                return@flatMap Mono.defer { Mono.error(IllegalArgumentException("이미 제거된 모임입니다.")) }
            }

            meeting.remove()
            meetingRepository.save(meeting)
        }

    @Transactional
    fun updateWantToAttend(id: Long, user: User, request: WantToAttendRequest): Mono<Meeting> =
        meetingRepository.findById(id)
            .switchIfEmpty(Mono.defer { Mono.error(IllegalArgumentException("존재하지 않는 모임입니다.")) })
            .flatMap { meeting ->
                if (meeting.isStarted) {
                    return@flatMap Mono.defer { Mono.error(IllegalStateException("이미 시작된 모임입니다.")) }
                }

                invitationRepository.findByMeetingIdAndUserId(id, user.id!!)
                    .switchIfEmpty(Mono.defer { Mono.error(IllegalStateException("초대받지 않은 모임입니다.")) })
                    .doOnNext { invitation ->
                        invitation.wantToAttend = request.wantToAttend
                    }.flatMap {
                        invitationRepository.save(it).flatMap {
                            Mono.just(meeting)
                        }
                    }
            }

    @Transactional
    fun updateAttendance(
        loginUser: User,
        meetingId: Long,
        userId: Long,
        request: AttendanceUpdateRequest
    ): Mono<Meeting> = meetingRepository.findById(meetingId)
        .switchIfEmpty(Mono.defer { Mono.error(IllegalArgumentException("존재하지 않는 모임입니다.")) })
        .flatMap { meeting ->
            if (!meeting.isOnGoing) {
                return@flatMap Mono.defer { Mono.error(IllegalStateException("진행중인 모임이 아닙니다.")) }
            }

            if (meeting.hostUserId != loginUser.id) {
                return@flatMap Mono.defer { Mono.error(ForbiddenException("출석 처리 권한이 없습니다.")) }
            }

            invitationRepository.findByMeetingIdAndUserId(meetingId, userId)
                .switchIfEmpty(Mono.defer { Mono.error(IllegalStateException("해당 유저는 초대받지 않았습니다.")) })
                .doOnNext { invitation ->
                    if (!invitation.wantToAttend) {
                        throw IllegalStateException("해당 유저는 참석자가 아닙니다.")
                    }
                    invitation.attended = request.attend
                }.flatMap {
                    invitationRepository.save(it).flatMap {
                        Mono.just(meeting)
                    }
                }
        }

    private fun mergeWithParticipants(meeting: Meeting): Mono<Meeting> =
        invitationRepository.findByMeetingIdAndWantToAttend(meeting.id, true)
            .collectList()
            .flatMap { invitations ->
                val participants = invitations.map {
                    val participant = User(nickname = it.user.nickname, group = it.user.group, profileImageUrl = it.user.profileImageUrl)
                    participant.id = it.user.id
                    participant.attended = it.attended
                    participant
                }.toList()
                meeting.participants = participants

                Mono.just(meeting)
            }

    private fun isUserInvited(meeting: Meeting, user: User): Mono<Boolean> =
        invitationRepository.findByMeetingId(meeting.id)
            .collectList()
            .flatMap { invitations ->
                val invitees = invitations.map { it.user }.toList()
                Mono.just(invitees.contains(user))
            }

    private fun findMeetingsFilteredByBlacklist(meetings: List<Meeting>, user: User): Mono<List<Meeting>> =
        blacklistRepository.findByUserId(user.id!!)
            .map { blacklist -> blacklist.meetingId }
            .collectList()
            .flatMap { blacklistMeetingIds ->
                val filtered = meetings.filter { meeting -> !blacklistMeetingIds.contains(meeting.id) }
                Mono.just(filtered)
            }

    private fun isNotBlacklistMeeting(meeting: Meeting, user: User): Mono<Boolean> =
        blacklistRepository.findByUserId(user.id!!)
            .map { blacklist -> blacklist.meetingId }
            .collectList()
            .flatMap { blacklistIds -> Mono.just(!blacklistIds.contains(meeting.id)) }
}
