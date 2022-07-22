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

    @Transactional(readOnly = true)
    fun findUserByAuthKey(authKey: String): Mono<User> {
        return userRepository.findUserByAuthKey(authKey)
            .switchIfEmpty(Mono.defer { Mono.error(IllegalArgumentException("존재하지 않는 인증키 입니다. (authKey: $authKey)")) })
    }
}
