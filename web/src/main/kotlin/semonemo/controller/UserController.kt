package semonemo.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.WebSession
import reactor.core.publisher.Mono
import semonemo.config.LoginUserArgumentResolver.Companion.LOGIN_ATTRIBUTE_NAME
import semonemo.model.dto.AuthRequest
import semonemo.model.dto.SemonemoResponse
import semonemo.model.dto.UserGetResponse
import semonemo.model.entity.User
import semonemo.service.UserService

@RestController
class UserController(
    private val userService: UserService,
) {

    @PostMapping("/api/login", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun login(@RequestBody request: AuthRequest, session: WebSession): Mono<ResponseEntity<SemonemoResponse>> {
        return userService.findUserByAuthKey(request.authKey)
            .flatMap {
                session.attributes[LOGIN_ATTRIBUTE_NAME] = it
                Mono.just(ResponseEntity.ok(SemonemoResponse(data = UserGetResponse.of(it))))
            }.onErrorResume {
                Mono.just(ResponseEntity.ok(SemonemoResponse(message = it.message)))
            }
    }

    @PostMapping("/api/logout")
    fun logout(session: WebSession): Mono<ResponseEntity<String>> =
        session.invalidate()
            .flatMap { Mono.just(ResponseEntity.ok("SUCCESS")) }

    @GetMapping("/api/my-info")
    fun getMyInfo(session: WebSession): Mono<ResponseEntity<SemonemoResponse>> {
        val user = session.attributes[LOGIN_ATTRIBUTE_NAME] as User?
            ?: return Mono.defer {
                Mono.just(
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(SemonemoResponse(statusCode = 401, message = "로그인이 필요합니다."))
                )
            }

        return Mono.just(ResponseEntity.ok(SemonemoResponse(data = UserGetResponse.of(user))))
    }
}
