package semonemo.repository

import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import semonemo.model.entity.Blacklist

interface BlacklistRepository : ReactiveCrudRepository<Blacklist, Long> {

    @Query("{'meetingId' : ?0, 'user._id' : ?1}")
    fun findByMeetingIdAndUserId(meetingId: Long, userId: Long): Mono<Blacklist>

    @Query("{'user._id' : ?0}")
    fun findByUserId(userId: Long): Flux<Blacklist>
}
