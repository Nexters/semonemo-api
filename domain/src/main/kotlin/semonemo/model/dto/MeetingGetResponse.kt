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
        fun listOf(meetings: Flux<Meeting>): Flux<MeetingGetResponse> {
            return meetings.map { of(it) }
        }

        private fun of(meeting: Meeting): MeetingGetResponse {
            return MeetingGetResponse(
                id = meeting.id,
                host = UserGetResponse.of(meeting.host),
                place = PlaceGetResponse.of(meeting.place),
                startDate = meeting.startDate,
                endDate = meeting.endDate,
            )
        }
    }
}