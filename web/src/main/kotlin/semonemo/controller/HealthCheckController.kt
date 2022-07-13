package semonemo.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {

    @GetMapping("/")
    fun healthCheck(): ResponseEntity<String> {
        return ResponseEntity.ok("\uD83C\uDF7A\uD83C\uDF7A\uD83C\uDF7A 맥주값만12마넌 \uD83C\uDF7A\uD83C\uDF7A\uD83C\uDF7A")
    }
}
