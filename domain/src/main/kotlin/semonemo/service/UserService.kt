package semonemo.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import semonemo.model.entity.User
import semonemo.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository
) {

    @Transactional
    fun createUser(user: User): Mono<User> = userRepository.save(user)
}
