package semonemo.model.blacklist

data class BlacklistSaveRequest(
    val meetingId: Long,
    val content: String,
)
