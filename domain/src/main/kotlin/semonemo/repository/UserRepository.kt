package semonemo.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import semonemo.model.entity.User

interface UserRepository : ReactiveCrudRepository<User, String> {

    fun findUserByAuthKey(authKey: String): Mono<User>
}
