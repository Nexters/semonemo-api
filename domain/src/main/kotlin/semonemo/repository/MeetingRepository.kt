package semonemo.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import semonemo.model.entity.Meeting

interface MeetingRepository : ReactiveCrudRepository<Meeting, String> {

    fun findById(id: Long): Mono<Meeting>
}
