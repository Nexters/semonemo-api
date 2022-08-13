package semonemo.schedule

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import semonemo.service.StampService

@Service
class StampScheduler(
    private val stampService: StampService,
) {

    @Scheduled(cron = "0 0/1 * * * ?")
    fun createStamp() {
        stampService.createStamp().subscribe()
    }
}
