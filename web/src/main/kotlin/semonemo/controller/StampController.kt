package semonemo.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.WebSession
import reactor.core.publisher.Mono
import semonemo.config.LoginUserArgumentResolver
import semonemo.model.dto.SemonemoResponse
import semonemo.model.dto.StampGetResponse
import semonemo.model.entity.User
import semonemo.service.StampService

@RestController
class StampController(
    private val stampService: StampService
) {

    @GetMapping("/api/stamps")
    fun getStamps(session: WebSession): Mono<ResponseEntity<SemonemoResponse>> {
        val user = session.attributes[LoginUserArgumentResolver.LOGIN_ATTRIBUTE_NAME] as User?
            ?: return Mono.defer {
                Mono.just(
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(SemonemoResponse(statusCode = 401, message = "로그인이 필요합니다."))
                )
            }

        return stampService.findStamps(user)
            .collectList()
            .flatMap { Mono.just(ResponseEntity.ok(SemonemoResponse(data = StampGetResponse.listOf(user, it)))) }
    }

    @GetMapping("/api/stamps/new")
    fun getNewStamps(session: WebSession): Mono<ResponseEntity<SemonemoResponse>> {
        val user = session.attributes[LoginUserArgumentResolver.LOGIN_ATTRIBUTE_NAME] as User?
            ?: return Mono.defer {
                Mono.just(
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(SemonemoResponse(statusCode = 401, message = "로그인이 필요합니다."))
                )
            }

        return stampService.findNewStamps(user)
            .collectList()
            .flatMap { Mono.just(ResponseEntity.ok(SemonemoResponse(data = StampGetResponse.listOf(user, it)))) }
    }
}
