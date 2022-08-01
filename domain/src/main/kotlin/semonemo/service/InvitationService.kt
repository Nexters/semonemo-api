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

@Service
class InvitationService(
    private val invitationRepository: InvitationRepository,
    private val meetingRepository: MeetingRepository,
    private val countersRepository: CountersRepository,
) {

    @Transactional
    fun saveInvitation(guest: User, request: InvitationSaveRequest): Mono<Invitation> =
        Mono.zip(countersRepository.findById("invitationId"), meetingRepository.findById(request.meetingId))
            .flatMap { tuple ->
                val counter = tuple.t1
                val meeting = tuple.t2

                counter.increaseSeq()

                Mono.zip(
                    countersRepository.save(counter),
                    invitationRepository.save(Invitation.guest(counter.seq, meeting, guest)),
                )
            }
            .flatMap { tuple -> Mono.just(tuple.t2) }
            .onErrorResume { Mono.defer { Mono.error(it) } }
}
