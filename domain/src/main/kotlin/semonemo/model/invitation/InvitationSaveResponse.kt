package semonemo.model.invitation

data class InvitationSaveResponse(
    val id: Long,
) {

    companion object {
        fun of(invitation: Invitation): InvitationSaveResponse = InvitationSaveResponse(invitation.id)
    }
}
