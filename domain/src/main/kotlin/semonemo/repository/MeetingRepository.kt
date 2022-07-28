package semonemo.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import semonemo.model.entity.Meeting
import semonemo.model.entity.MeetingStatus

interface MeetingRepository : ReactiveCrudRepository<Meeting, Long> {

    fun findAllByStatus(status: MeetingStatus): Flux<Meeting>
}
