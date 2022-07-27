package semonemo.config

import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.reactive.BindingContext
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebSession
import reactor.core.publisher.Mono
import semonemo.model.entity.User

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

        fun findUser(session: WebSession): Mono<User> {
            val user = session.attributes[LOGIN_ATTRIBUTE_NAME] as User? ?: return Mono.empty()
            return Mono.just(user)
        }
    }
}
