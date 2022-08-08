package semonemo.model.dto

import semonemo.model.entity.Invitation

data class InvitationSaveResponse(
    val id: Long,
) {

    companion object {
        fun of(invitation: Invitation): InvitationSaveResponse = InvitationSaveResponse(invitation.id)
    }
}
