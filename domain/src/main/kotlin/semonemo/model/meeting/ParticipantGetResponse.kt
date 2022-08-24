package semonemo.model.meeting

import semonemo.model.entity.User

data class ParticipantGetResponse(
    val id: Long,
    val nickname: String,
    val group: String?,
    val attended: Boolean?,
    val profileImageUrl: String?,
) {

    companion object {
        fun listOf(participants: List<User>): List<ParticipantGetResponse> = participants.map { of(it) }

        private fun of(user: User) =
            ParticipantGetResponse(user.id!!, user.nickname, user.group, user.attended, user.profileImageUrl)
    }
}