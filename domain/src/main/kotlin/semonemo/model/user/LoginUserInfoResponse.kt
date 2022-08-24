package semonemo.model.user

data class LoginUserInfoResponse(
    val isHost: Boolean = false,
    val wantToAttend: Boolean = false,
)
