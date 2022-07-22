package semonemo.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.WebSession
import reactor.core.publisher.Mono
import semonemo.config.CurrentLoginMemberArgumentResolver.Companion.LOGIN_ATTRIBUTE_NAME
import semonemo.model.dto.AuthRequest
import semonemo.model.dto.AuthResponse
import semonemo.service.UserService

@RestController
class UserController(
    private val userService: UserService
) {

    @PostMapping("/api/login", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun login(@RequestBody request: AuthRequest, session: WebSession): Mono<ResponseEntity<AuthResponse>> {
        return userService.findUserByAuthKey(request.authKey)
            .flatMap {
                session.attributes[LOGIN_ATTRIBUTE_NAME] = it
                Mono.just(ResponseEntity.ok(AuthResponse.success(it)))
            }.onErrorResume {
                Mono.just(ResponseEntity.ok(AuthResponse.fail(it.message)))
            }
    }
}
