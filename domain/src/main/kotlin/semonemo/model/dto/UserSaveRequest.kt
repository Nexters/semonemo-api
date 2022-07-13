package semonemo.model.dto

import semonemo.model.entity.User

data class UserSaveRequest(
    val nickname: String
) {

    fun toUser(): User = User(nickname = nickname)
}
