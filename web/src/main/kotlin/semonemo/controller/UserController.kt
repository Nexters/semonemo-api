package semonemo.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import semonemo.model.entity.User
import semonemo.model.dto.UserSaveRequest
import semonemo.service.UserService

@RestController
class UserController(
    private val userService: UserService
) {

    @PostMapping("/users", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createUser(@RequestBody request: UserSaveRequest): Mono<User> =
        userService.createUser(request.toUser())
}
