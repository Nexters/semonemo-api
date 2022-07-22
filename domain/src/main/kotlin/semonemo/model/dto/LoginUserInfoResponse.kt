package semonemo.model.dto

data class LoginUserInfoResponse(
    val isHost: Boolean = false,
    val wantToAttend: Boolean = false,
)
