package semonemo.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import semonemo.model.entity.Meeting
import semonemo.model.entity.MeetingStatus

interface MeetingRepository : ReactiveCrudRepository<Meeting, String> {

    fun findAllByStatus(status: MeetingStatus): Flux<Meeting>

    fun findById(id: Long): Mono<Meeting>
}
