package semonemo.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import semonemo.model.dto.InvitationSaveRequest
import semonemo.model.entity.Invitation
import semonemo.model.entity.User
import semonemo.repository.CountersRepository
import semonemo.repository.InvitationRepository
import semonemo.repository.MeetingRepository
import semonemo.repository.UserRepository

@Service
class InvitationService(
    private val invitationRepository: InvitationRepository,
    private val meetingRepository: MeetingRepository,
    private val countersRepository: CountersRepository,
    private val userRepository: UserRepository,
) {

    // TODO: 리팩토링 필요. 웹플럭스 어려웡
    @Transactional
    fun saveInvitation(guest: User, request: InvitationSaveRequest): Mono<Invitation> =
        meetingRepository.findById(request.meetingId)
            .switchIfEmpty(Mono.defer { Mono.error(IllegalArgumentException("존재하지 않는 모임입니다. (meetingId: ${request.meetingId})")) })
            .flatMap { meeting ->
                invitationRepository.findByMeetingIdAndUserId(meeting.id, guest.id!!)
                    .flatMap<Invitation?> { Mono.defer { Mono.error(IllegalArgumentException("이미 초대된 모임입니다. (meetingId: ${request.meetingId}, user: ${guest.nickname})")) } }
                    .switchIfEmpty(countersRepository.findById("invitationId")
                        .flatMap { counter ->
                            counter.increaseSeq()

                            Mono.zip(
                                countersRepository.save(counter),
                                invitationRepository.save(Invitation.guest(counter.seq, meeting, guest))
                            ).flatMap { tuple -> Mono.just(tuple.t2) }
                        })
            }
}
