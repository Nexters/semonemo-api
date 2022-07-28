package semonemo.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.WebSession
import reactor.core.publisher.Mono
import semonemo.config.LoginUserArgumentResolver
import semonemo.model.dto.MeetingGetResponse
import semonemo.model.dto.MeetingRemoveResponse
import semonemo.model.dto.MeetingSaveRequest
import semonemo.model.dto.MeetingSaveResponse
import semonemo.model.entity.User
import semonemo.model.exception.ForbiddenException
import semonemo.service.MeetingService

@RestController
class MeetingController(
    private val meetingService: MeetingService,
) {

    @PostMapping("/api/meetings", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createMeeting(
        @RequestBody request: MeetingSaveRequest,
        session: WebSession
    ): Mono<ResponseEntity<MeetingSaveResponse>> {
        val user = session.attributes[LoginUserArgumentResolver.LOGIN_ATTRIBUTE_NAME] as User?
            ?: return Mono.defer { Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)) }

        return meetingService.saveMeeting(user, request)
            .flatMap { Mono.just(ResponseEntity.ok(MeetingSaveResponse.success(it))) }
            .onErrorResume { Mono.just(ResponseEntity.ok(MeetingSaveResponse.fail(it.message))) }
    }

    @GetMapping("/api/meetings")
    fun getMeetings(session: WebSession): Mono<ResponseEntity<List<MeetingGetResponse>>> {
        val user = session.attributes[LoginUserArgumentResolver.LOGIN_ATTRIBUTE_NAME] as User?
            ?: return Mono.defer { Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)) }

        return meetingService.findMeetings()
            .collectList()
            .flatMap { Mono.just(ResponseEntity.ok(MeetingGetResponse.listOf(it))) }
    }

    @DeleteMapping("/api/meetings/{id}")
    fun removeMeeting(session: WebSession, @PathVariable id: Long): Mono<ResponseEntity<MeetingRemoveResponse>> {
        val user = session.attributes[LoginUserArgumentResolver.LOGIN_ATTRIBUTE_NAME] as User?
            ?: return Mono.defer { Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)) }

        return meetingService.removeMeeting(user, id)
            .flatMap { Mono.just(ResponseEntity.ok(MeetingRemoveResponse.success(it))) }
            .onErrorResume {
                when (it) {
                    is java.lang.IllegalArgumentException -> Mono.just(
                        ResponseEntity.status(HttpStatus.NOT_FOUND).body(MeetingRemoveResponse.fail(it.message))
                    )
                    is ForbiddenException -> Mono.just(
                        ResponseEntity.status(HttpStatus.FORBIDDEN).body(MeetingRemoveResponse.fail(it.message))
                    )
                    else -> Mono.just(
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
                    )
                }
            }
    }
}
