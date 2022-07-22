package semonemo.model.dto

import semonemo.model.entity.User

data class AuthResponse(
    val result: String,
    val loginUser: UserGetResponse? = null,
) {

    companion object {
        fun success(user: User): AuthResponse = AuthResponse("SUCCESS", UserGetResponse.of(user))

        fun fail(message: String): AuthResponse = AuthResponse(message)
    }
}
