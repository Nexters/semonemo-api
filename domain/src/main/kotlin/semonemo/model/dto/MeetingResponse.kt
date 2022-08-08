package semonemo.model.dto

import semonemo.model.entity.Meeting

data class MeetingResponse(
    val id: Long,
) {

    companion object {
        fun of(meeting: Meeting): MeetingResponse = MeetingResponse(meeting.id)
    }
}
