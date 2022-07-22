package semonemo.model.dto

import semonemo.model.entity.Meeting
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
        fun listOf(meetings: List<Meeting>): List<MeetingGetResponse> = meetings.map { of(it) }

        private fun of(meeting: Meeting): MeetingGetResponse =
            MeetingGetResponse(
                id = meeting.id,
                startDate = meeting.startDate,
                endDate = meeting.endDate,
                place = PlaceGetResponse.of(meeting.place),
                host = UserGetResponse.of(meeting.host),
                loginUser = LoginUserInfoResponse(),
                createdAt = meeting.createdAt,
            )
    }
}
