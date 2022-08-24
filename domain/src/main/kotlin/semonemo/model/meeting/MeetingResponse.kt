package semonemo.model.meeting

data class MeetingResponse(
    val id: Long,
) {

    companion object {
        fun of(meeting: Meeting): MeetingResponse = MeetingResponse(meeting.id)
    }
}
