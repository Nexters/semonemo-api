package semonemo.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

@Document
class Meeting(
    id: Long,
    host: User,
    place: Place,
    startDate: LocalDateTime,
    endDate: LocalDateTime,
) : AuditableDocument() {

    init {
        validateDate(startDate, endDate)
    }

    @Id
    var id = id

    var host = host
        private set

    var place = place
        private set

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    var startDate = startDate
        private set

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    var endDate = endDate
        private set

    var status: MeetingStatus = MeetingStatus.ACTIVE

    @Transient
    val hostUserId = host.id

    @Transient
    var participants: List<User> = listOf()

    val isRemoved: Boolean
        get() = status == MeetingStatus.REMOVED

    val isStarted: Boolean
        get() = startDate.isBefore(LocalDateTime.now())

    val isOnGoing: Boolean
        get(): Boolean {
            val now = LocalDateTime.now()
            return startDate.isBefore(now) && endDate.isAfter(now)
        }

    fun remove() {
        status = MeetingStatus.REMOVED
    }

    private fun validateDate(startDate: LocalDateTime, endDate: LocalDateTime) {
        require(startDate.isBefore(endDate)) { "시작 날짜는 종료 날짜보다 이전이어야 합니다." }
    }

    companion object {
        const val serialVersionUID: Long = -68923944814214L
    }
}

enum class MeetingStatus {
    ACTIVE, REMOVED
}
