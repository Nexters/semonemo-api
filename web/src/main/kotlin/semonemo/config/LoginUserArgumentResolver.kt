package semonemo.config

import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.reactive.BindingContext
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebSession
import reactor.core.publisher.Mono
import semonemo.model.SemonemoResponse
import semonemo.model.entity.User

/**
 * TODO
 * 로그인 유저 정보를 session에서 받아오는 기능을 resolveArgument()에 구현해서 argument 형식으로 주입하려 했지만
 * 정상적으로 동작하지 않아 임시로 companion 메소드로 구현해 사용
 */
@Component
class LoginUserArgumentResolver : HandlerMethodArgumentResolver {

    private val session: WebSession? = null

    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.hasParameterAnnotation(LoginUser::class.java)

    override fun resolveArgument(
        parameter: MethodParameter,
        bindingContext: BindingContext,
        exchange: ServerWebExchange
    ): Mono<Any> {
        if (session == null) {
            return Mono.just("web session not found")
        }
        return Mono.just(session.attributes[LOGIN_ATTRIBUTE_NAME]!!)
    }

    companion object {
        const val LOGIN_ATTRIBUTE_NAME = "LOGIN_USER"

        fun findLoginUser(session: WebSession): User? = session.attributes[LOGIN_ATTRIBUTE_NAME] as User?

        fun unauthorizedResponse(): Mono<ResponseEntity<SemonemoResponse>> = Mono.defer {
            Mono.just(
                ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(SemonemoResponse(statusCode = 401, message = "로그인이 필요합니다."))
            )
        }
    }
}
