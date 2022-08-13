package semonemo.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import semonemo.model.entity.Stamp
import semonemo.model.entity.User
import semonemo.repository.CountersRepository
import semonemo.repository.InvitationRepository
import semonemo.repository.StampRepository
import java.time.LocalDateTime

@Service
class StampService(
    private val stampRepository: StampRepository,
    private val countersRepository: CountersRepository,
    private val invitationRepository: InvitationRepository,
) {

    @Transactional
    fun createStamp(): Flux<Stamp> {
        return invitationRepository.findByAttendedAndStamped(attended = true, stamped = false)
            .take(1L) // TODO: 1개씩 저장하지 않으면, 여러 개의 이벤트가 비동기로 실행돼서 정확히 저장되지 않음(stampId에 lock이 안 걸려서 값이 중복되는 경우가 생겨서 그런 것 같음)
            .filter { it.isMeetingEnd }
            .flatMap { invitation ->
                countersRepository.findById("stampId")
                    .flatMap { stampCounter ->
                        stampCounter.increaseSeq()
                        invitation.stamped = true

                        Mono.zip(
                            countersRepository.save(stampCounter),
                            invitationRepository.save(invitation),
                            stampRepository.save(Stamp(stampCounter.seq, invitation)),
                        )
                    }.flatMap { tuple ->
                        println("[스탬프] 미팅 id: ${tuple.t2.meeting.id}, 유저 닉네임: ${tuple.t2.user.nickname}, 생성 시간: ${LocalDateTime.now()}")
                        Mono.just(tuple.t3)
                    }
            }
    }

    @Transactional(readOnly = true)
    fun findStamps(user: User): Flux<Stamp> = stampRepository.findByUser(userId = user.id!!)
        .sort(Comparator.comparingLong<Stamp?> { it.id }.reversed())

    @Transactional
    fun findNewStamps(user: User): Flux<Stamp> =
        stampRepository.findByUserAndConfirmed(userId = user.id!!, confirmed = false)
            .flatMap { stamp ->
                stamp.confirmed = true
                stampRepository.save(stamp)
                    .flatMap { Mono.just(it) }
            }.sort(Comparator.comparingLong { it.id })
}
