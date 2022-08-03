package semonemo.model.dto

import org.springframework.format.annotation.DateTimeFormat
import semonemo.model.entity.Meeting
import semonemo.model.entity.User
import java.time.LocalDateTime

data class MeetingSaveRequest(
    @DateTimeFormat(pattern = "yyyy-MM-dd`T`HH:mm:ss")
    val startDate: LocalDateTime,

    @DateTimeFormat(pattern = "yyyy-MM-dd`T`HH:mm:ss")
    val endDate: LocalDateTime,

    val place: PlaceSaveRequest,
) {

    fun toMeeting(id: Long, host: User): Meeting {
        require(startDate.isAfter(LocalDateTime.now())) { "시작 날짜는 현재 날짜 이후여야 합니다." }

        return Meeting(id = id, host = host, place = place.toPlace(), startDate = startDate, endDate = endDate)
    }
}
