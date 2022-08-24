package semonemo.model.meeting

import semonemo.model.user.LoginUserInfoResponse
import semonemo.model.user.UserGetResponse
import semonemo.model.entity.User
import java.time.LocalDateTime

data class MeetingGetResponse(
    val id: Long?,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val place: PlaceGetResponse,
    val host: UserGetResponse,
    val loginUser: LoginUserInfoResponse,
    val isEnd: Boolean,
    val participants: List<ParticipantGetResponse>,
) {

    companion object {
        fun listOf(
            meetings: List<Meeting>,
            user: User,
            now: LocalDateTime = LocalDateTime.now()
        ): List<MeetingGetResponse> =
            meetings.map { of(it, user, now) }

        fun of(
            meeting: Meeting,
            user: User,
            now: LocalDateTime = LocalDateTime.now()
        ): MeetingGetResponse {
            val isLoginUserParticipant = meeting.participants.map { it.id }
                .contains(user.id)

            return MeetingGetResponse(
                id = meeting.id,
                startDate = meeting.startDate,
                endDate = meeting.endDate,
                place = PlaceGetResponse.of(meeting.place),
                host = UserGetResponse.of(meeting.host),
                loginUser = LoginUserInfoResponse(
                    user.id == meeting.hostUserId,
                    user.id == meeting.hostUserId || isLoginUserParticipant
                ),
                isEnd = meeting.endDate.isBefore(now),
                participants = ParticipantGetResponse.listOf(meeting.participants)
            )
        }
    }
}
