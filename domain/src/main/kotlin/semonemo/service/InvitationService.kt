package semonemo.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import semonemo.model.invitation.InvitationSaveRequest
import semonemo.model.invitation.Invitation
import semonemo.model.entity.User
import semonemo.repository.CountersRepository
import semonemo.repository.InvitationRepository
import semonemo.repository.MeetingRepository

@Service
class InvitationService(
    private val invitationRepository: InvitationRepository,
    private val meetingRepository: MeetingRepository,
    private val countersRepository: CountersRepository,
) {

    @Transactional
    fun saveInvitation(guest: User, request: InvitationSaveRequest): Mono<Invitation> =
        meetingRepository.findById(request.meetingId)
            .switchIfEmpty(Mono.defer { Mono.error(IllegalArgumentException("존재하지 않는 모임입니다.")) })
            .flatMap { meeting ->
                if (meeting.isStarted) {
                    return@flatMap Mono.defer { Mono.error(IllegalStateException("이미 시작한 모임입니다.")) }
                }

                invitationRepository.findByMeetingIdAndUserId(meeting.id, guest.id!!)
                    .flatMap<Invitation?> { Mono.defer { Mono.error(IllegalArgumentException("이미 초대된 모임입니다.")) } }
                    .switchIfEmpty(countersRepository.findById("invitationId")
                        .flatMap { counter ->
                            counter.increaseSeqOne()

                            Mono.zip(
                                countersRepository.save(counter),
                                invitationRepository.save(Invitation.guest(counter.seq, meeting, guest))
                            ).flatMap { tuple -> Mono.just(tuple.t2) }
                        })
            }
}
