package semonemo.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.WebSession
import reactor.core.publisher.Mono
import semonemo.config.LoginUserArgumentResolver.Companion.findLoginUser
import semonemo.config.LoginUserArgumentResolver.Companion.unauthorizedResponse
import semonemo.model.invitation.InvitationSaveRequest
import semonemo.model.invitation.InvitationSaveResponse
import semonemo.model.SemonemoResponse
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
        val user = findLoginUser(session) ?: return unauthorizedResponse()

        return invitationService.saveInvitation(user, request)
            .flatMap { Mono.just(ResponseEntity.ok(SemonemoResponse(data = InvitationSaveResponse.of(it)))) }
            .onErrorResume { Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(SemonemoResponse(statusCode = 400, message = it.message))) }
    }
}
