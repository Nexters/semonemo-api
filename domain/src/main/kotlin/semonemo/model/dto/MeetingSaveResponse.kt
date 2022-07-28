package semonemo.model.dto

import semonemo.model.entity.Meeting

data class MeetingSaveResponse(
    val result: String?,
    val savedId: Long? = null,
) {

    companion object {
        fun success(meeting: Meeting): MeetingSaveResponse = MeetingSaveResponse("SUCCESS", meeting.id)

        fun fail(message: String?): MeetingSaveResponse = MeetingSaveResponse(message)
    }
}
