package semonemo.model.dto

import semonemo.model.entity.Meeting

data class MeetingRemoveResponse(
    val result: String?,
    val removedId: Long? = null,
) {

    companion object {
        fun success(meeting: Meeting): MeetingRemoveResponse = MeetingRemoveResponse("SUCCESS", meeting.id)

        fun fail(message: String?): MeetingRemoveResponse = MeetingRemoveResponse(message)
    }
}
