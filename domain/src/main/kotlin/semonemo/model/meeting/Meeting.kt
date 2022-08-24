package semonemo.model.meeting

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.format.annotation.DateTimeFormat
import semonemo.model.AuditableDocument
import semonemo.model.entity.User
import java.time.LocalDateTime

@Document("meeting")
class Meeting(
    @Id
    var id: Long,
    val host: User,
    val place: Place,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    val startDate: LocalDateTime,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    val endDate: LocalDateTime,

    private var status: MeetingStatus = MeetingStatus.ACTIVE
) : AuditableDocument() {

    init {
        validateDate(startDate, endDate)
    }

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

    val isEnd: Boolean
        get() = endDate.isBefore(LocalDateTime.now())

    fun remove() {
        status = MeetingStatus.REMOVED
    }

    private fun validateDate(startDate: LocalDateTime, endDate: LocalDateTime) =
        require(startDate.isBefore(endDate)) { "시작 날짜는 종료 날짜보다 이전이어야 합니다." }

    companion object {
        const val serialVersionUID: Long = -68923944814214L
    }
}

enum class MeetingStatus {
    ACTIVE, REMOVED
}
