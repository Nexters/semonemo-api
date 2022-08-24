package semonemo.model.stamp

import com.fasterxml.jackson.annotation.JsonInclude
import semonemo.model.entity.User
import semonemo.model.meeting.MeetingGetResponse

data class StampGetResponse(
    val id: Long,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val order: Long?,
    val meeting: MeetingGetResponse,
) {

    companion object {
        fun listOf(user: User, stamps: List<Stamp>): List<StampGetResponse> = stamps.map { of(user, it) }

        fun listOf(user: User, newStamps: List<Stamp>, totalStampCount: Long): List<StampGetResponse> {
            var order = totalStampCount - newStamps.count()
            return newStamps.map {
                order += 1
                of(user, it, order)
            }
        }

        fun of(user: User, stamp: Stamp, order: Long? = null) =
            StampGetResponse(stamp.id, order, MeetingGetResponse.of(stamp.invitation.meeting, user))
    }
}
