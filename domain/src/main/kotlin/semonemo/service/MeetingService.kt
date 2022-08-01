package semonemo.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import semonemo.model.dto.MeetingSaveRequest
import semonemo.model.entity.Invitation
import semonemo.model.entity.Meeting
import semonemo.model.entity.MeetingStatus
import semonemo.model.entity.User
import semonemo.model.exception.ForbiddenException
import semonemo.repository.MeetingRepository
import semonemo.repository.CountersRepository
import semonemo.repository.InvitationRepository

@Service
class MeetingService(
    private val meetingRepository: MeetingRepository,
    private val invitationRepository: InvitationRepository,
    private val countersRepository: CountersRepository,
) {

    @Transactional
    fun saveMeeting(host: User, request: MeetingSaveRequest): Mono<Meeting> =
        // TODO: counter 조회할 때 lock 걸어야 함
        Mono.zip(countersRepository.findById("meetingId"), countersRepository.findById("invitationId"))
            .flatMap { tuple ->
                val meetingCounter = tuple.t1
                val invitationCounter = tuple.t2
                val meeting = request.toMeeting(meetingCounter.seq, host)

                meetingCounter.increaseSeq()
                invitationCounter.increaseSeq()

                Mono.zip(
                    countersRepository.save(meetingCounter),
                    countersRepository.save(invitationCounter),
                    meetingRepository.save(meeting),
                    invitationRepository.save(Invitation.host(invitationCounter.seq, meeting, host)),
                )
            }
            .flatMap { tuple -> Mono.just(tuple.t3) }
            .onErrorResume { Mono.defer { Mono.error(it) } }

    // TODO: 정렬 기능 추가
    @Transactional(readOnly = true)
    fun findMeetings(): Flux<Meeting> = meetingRepository.findAllByStatus(MeetingStatus.ACTIVE)

    @Transactional(readOnly = true)
    fun findMeeting(id: Long): Mono<Meeting> = meetingRepository.findById(id)

    @Transactional
    fun removeMeeting(user: User, id: Long): Mono<Meeting> = meetingRepository.findById(id)
        .switchIfEmpty(Mono.defer { Mono.error(IllegalArgumentException("존재하지 않는 모임입니다. (id: $id)")) })
        .flatMap {
            if (it.hostUserId != user.id) {
                return@flatMap Mono.defer { Mono.error(ForbiddenException("권한이 없는 유저입니다. (호스트가 아님)")) }
            }

            if (it.isRemoved) {
                return@flatMap Mono.defer { Mono.error(IllegalArgumentException("이미 제거된 모임입니다. (id: $id)")) }
            }

            it.remove()
            meetingRepository.save(it)
        }
}
