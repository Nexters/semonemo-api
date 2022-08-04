package semonemo.model.dto

import semonemo.model.entity.User

data class ParticipantGetResponse(
    val nickname: String,
    val group: String?,
    // TODO: 출석 처리 기능 작업할 때, 처리 필요
    val attend: Boolean = false,
) {

    companion object {
        fun listOf(participants: List<User>): List<ParticipantGetResponse> = participants.map { of(it) }

        private fun of(user: User) = ParticipantGetResponse(user.nickname, user.group)
    }
}