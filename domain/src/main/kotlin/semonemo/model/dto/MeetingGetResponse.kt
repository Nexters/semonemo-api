package semonemo.model.dto

import semonemo.model.entity.Meeting
import semonemo.model.entity.User
import java.time.LocalDateTime

data class MeetingGetResponse(
    val id: Long?,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val place: PlaceGetResponse,
    val host: UserGetResponse,
    val loginUser: LoginUserInfoResponse,
    val isEnd: Boolean = false,
    val createdAt: LocalDateTime?,
) {

    companion object {
        fun listOf(meetings: List<Meeting>, user: User): List<MeetingGetResponse> = meetings.map { of(it, user) }

        private fun of(meeting: Meeting, user: User): MeetingGetResponse =
            MeetingGetResponse(
                id = meeting.id,
                startDate = meeting.startDate,
                endDate = meeting.endDate,
                place = PlaceGetResponse.of(meeting.place),
                host = UserGetResponse.of(meeting.host),
                loginUser = LoginUserInfoResponse(user.id == meeting.hostUserId, false),
                createdAt = meeting.createdAt,
            )
    }
}
