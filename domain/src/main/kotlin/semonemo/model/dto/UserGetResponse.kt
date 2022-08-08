package semonemo.model.dto

import semonemo.model.entity.User

data class UserGetResponse(
    val id: Long?,
    val nickname: String,
    val group: String?,
) {
    companion object {
        fun of(user: User): UserGetResponse = UserGetResponse(user.id, user.nickname, user.group)
    }
}
