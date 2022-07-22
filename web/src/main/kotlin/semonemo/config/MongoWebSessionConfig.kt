package semonemo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.session.data.mongo.JdkMongoSessionConverter
import org.springframework.session.data.mongo.config.annotation.web.reactive.EnableMongoWebSession
import java.time.Duration

@Configuration
@EnableMongoWebSession(maxInactiveIntervalInSeconds = 3600)
class MongoWebSessionConfig {

    @Bean
    fun jdkMongoSessionConverter(): JdkMongoSessionConverter = JdkMongoSessionConverter(Duration.ofMinutes(60L))
}
