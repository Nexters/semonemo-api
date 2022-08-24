package semonemo.repository

import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import semonemo.model.invitation.Invitation

interface InvitationRepository : ReactiveCrudRepository<Invitation, Long> {

    @Query("{'meeting._id' : ?0}")
    fun findByMeetingId(meetingId: Long): Flux<Invitation>

    @Query("{'meeting._id' : ?0, 'wantToAttend' : ?1}")
    fun findByMeetingIdAndWantToAttend(meetingId: Long, wantToAttend: Boolean): Flux<Invitation>

    @Query("{'meeting._id' : ?0, 'user._id' : ?1}")
    fun findByMeetingIdAndUserId(meetingId: Long, userId: Long): Mono<Invitation>

    @Query("{'attended' : ?0, 'stamped' : ?1}")
    fun findByAttendedAndStamped(attended: Boolean, stamped: Boolean): Flux<Invitation>
}
