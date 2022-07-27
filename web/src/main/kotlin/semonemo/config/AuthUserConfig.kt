package semonemo.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer

@Configuration
class AuthUserConfig(
    private val loginUserArgumentResolver: LoginUserArgumentResolver
) : WebFluxConfigurer {

    @Override
    override fun configureArgumentResolvers(configurer: ArgumentResolverConfigurer) {
        configurer.addCustomResolver(loginUserArgumentResolver)
    }
}
