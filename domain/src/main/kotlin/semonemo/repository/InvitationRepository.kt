package semonemo.repository

import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import semonemo.model.entity.Invitation

interface InvitationRepository : ReactiveCrudRepository<Invitation, Long> {

    @Query("{'meeting._id' : ?0, 'user._id' : ?1}")
    fun findByMeetingIdAndUserId(meetingId: Long, userId: Long): Mono<Invitation>
}
