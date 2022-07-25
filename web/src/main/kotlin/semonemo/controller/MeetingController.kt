package semonemo.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.WebSession
import reactor.core.publisher.Mono
import semonemo.config.LoginUserArgumentResolver.Companion.findUser
import semonemo.model.dto.MeetingGetResponse
import semonemo.service.MeetingService

@RestController
class MeetingController(
    private val meetingService: MeetingService,
) {

    @GetMapping("/api/meetings")
    fun getMeetings(session: WebSession): Mono<ResponseEntity<List<MeetingGetResponse>>> {
        return findUser(session)
            .flatMap {
                meetingService.findMeetings()
                    .collectList()
                    .flatMap { Mono.just(ResponseEntity.ok(MeetingGetResponse.listOf(it))) }
            }.switchIfEmpty(Mono.defer { Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)) })
    }
}
