package semonemo.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import semonemo.model.dto.BlacklistSaveRequest
import semonemo.model.entity.Blacklist
import semonemo.model.entity.User
import semonemo.repository.BlacklistRepository
import semonemo.repository.CountersRepository
import semonemo.repository.InvitationRepository
import semonemo.repository.MeetingRepository

@Service
class BlacklistService(
    private val blacklistRepository: BlacklistRepository,
    private val countersRepository: CountersRepository,
    private val meetingRepository: MeetingRepository,
    private val invitationRepository: InvitationRepository,
) {

    @Transactional
    fun saveBlacklist(user: User, request: BlacklistSaveRequest): Mono<Blacklist> =
        meetingRepository.findById(request.meetingId)
            .switchIfEmpty(Mono.defer { Mono.error(IllegalArgumentException("존재하지 않는 모임입니다.")) })
            .flatMap { invitationRepository.findByMeetingIdAndUserId(request.meetingId, user.id!!) }
            .switchIfEmpty(Mono.defer { Mono.error(IllegalArgumentException("초대받지 않은 모입입니다.")) })
            .flatMap {
                blacklistRepository.findByMeetingIdAndUserId(request.meetingId, user.id!!)
                    .flatMap<Blacklist?> { Mono.defer { Mono.error(IllegalArgumentException("이미 신고된 모임입니다.")) } }
            }.switchIfEmpty(countersRepository.findById("blacklistId")
                .flatMap { counter ->
                    counter.increaseSeq()
                    countersRepository.save(counter)
                    blacklistRepository.save(Blacklist(counter.seq, user, request.meetingId, request.content))
                })
}
