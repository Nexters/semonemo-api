package semonemo.model.dto

import reactor.core.publisher.Flux
import semonemo.model.entity.Meeting
import java.time.LocalDateTime

data class MeetingGetResponse(
    val id: Long?,
    val host: UserGetResponse,
    val place: PlaceGetResponse,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
) {

    companion object {
        fun listOf(meetings: List<Meeting>): List<MeetingGetResponse> = meetings.map { of(it) }

        fun of(meeting: Meeting): MeetingGetResponse =
            MeetingGetResponse(
                id = meeting.id,
                host = UserGetResponse.of(meeting.host),
                place = PlaceGetResponse.of(meeting.place),
                startDate = meeting.startDate,
                endDate = meeting.endDate,
            )
    }
}
