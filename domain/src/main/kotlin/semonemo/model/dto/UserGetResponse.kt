package semonemo.model.dto

import semonemo.model.entity.User

data class UserGetResponse(
    val nickname: String,
) {
    companion object {
        fun of(user: User): UserGetResponse = UserGetResponse(user.nickname)
    }
}
