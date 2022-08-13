package semonemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.*
import javax.annotation.PostConstruct

@EnableScheduling
@SpringBootApplication
class SemonemoApplication {

    @PostConstruct
    fun setDefaultTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
    }
}

fun main(args: Array<String>) {
    runApplication<SemonemoApplication>(*args)
}
