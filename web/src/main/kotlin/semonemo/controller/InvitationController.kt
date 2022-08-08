package semonemo.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.WebSession
import reactor.core.publisher.Mono
import semonemo.config.LoginUserArgumentResolver
import semonemo.model.dto.InvitationSaveRequest
import semonemo.model.dto.InvitationSaveResponse
import semonemo.model.dto.SemonemoResponse
import semonemo.model.entity.User
import semonemo.service.InvitationService

@RestController
class InvitationController(
    private val invitationService: InvitationService,
) {

    @PostMapping("/api/invitations", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createInvitation(
        @RequestBody request: InvitationSaveRequest,
        session: WebSession
    ): Mono<ResponseEntity<SemonemoResponse>> {
        val user = session.attributes[LoginUserArgumentResolver.LOGIN_ATTRIBUTE_NAME] as User?
            ?: return Mono.defer {
                Mono.just(
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(SemonemoResponse(statusCode = 401, message = "로그인이 필요합니다."))
                )
            }

        return invitationService.saveInvitation(user, request)
            .flatMap { Mono.just(ResponseEntity.ok(SemonemoResponse(data = InvitationSaveResponse.of(it)))) }
            .onErrorResume {
                Mono.just(
                    ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(SemonemoResponse(statusCode = 400, message = it.message))
                )
            }
    }
}
