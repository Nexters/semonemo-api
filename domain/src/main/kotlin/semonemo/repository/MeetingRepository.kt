package semonemo.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import semonemo.model.meeting.Meeting
import semonemo.model.meeting.MeetingStatus
import java.time.LocalDateTime

interface MeetingRepository : ReactiveCrudRepository<Meeting, Long> {

    fun findAllByStatusAndEndDateBeforeOrderByStartDate(
        status: MeetingStatus = MeetingStatus.ACTIVE,
        endDate: LocalDateTime = LocalDateTime.now()
    ): Flux<Meeting>

    fun findAllByStatusAndEndDateAfterOrderByStartDate(
        status: MeetingStatus = MeetingStatus.ACTIVE,
        endDate: LocalDateTime = LocalDateTime.now()
    ): Flux<Meeting>
}
