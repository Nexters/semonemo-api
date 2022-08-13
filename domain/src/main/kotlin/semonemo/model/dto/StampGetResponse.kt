package semonemo.model.dto

import semonemo.model.entity.Stamp
import semonemo.model.entity.User

data class StampGetResponse(
    val id: Long,
    val meeting: MeetingGetResponse
) {

    companion object {
        fun listOf(user: User, stamps: List<Stamp>): List<StampGetResponse> = stamps.map { of(user, it) }

        private fun of(user: User, stamp: Stamp) =
            StampGetResponse(stamp.id, MeetingGetResponse.of(stamp.invitation.meeting, user))
    }
}