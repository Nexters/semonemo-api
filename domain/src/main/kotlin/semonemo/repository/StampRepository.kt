package semonemo.repository

import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import semonemo.model.stamp.Stamp

interface StampRepository : ReactiveCrudRepository<Stamp, Long> {

    @Query("{'invitation.user._id' : ?0}")
    fun findByUserId(userId: Long): Flux<Stamp>

    @Query("{'invitation.user._id' : ?0, 'confirmed' : ?1}")
    fun findByUserIdAndConfirmed(userId: Long, confirmed: Boolean): Flux<Stamp>
}
