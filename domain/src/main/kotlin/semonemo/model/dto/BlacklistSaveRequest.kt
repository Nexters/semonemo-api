package semonemo.model.dto

data class BlacklistSaveRequest(
    val meetingId: Long,
    val content: String,
)
