package semonemo.model.dto

import semonemo.model.entity.Invitation

data class InvitationSaveResponse(
    val result: String?,
    val savedId: Long? = null,
) {

    companion object {
        fun success(invitation: Invitation): InvitationSaveResponse = InvitationSaveResponse("SUCCESS", invitation.id)

        fun fail(message: String?): InvitationSaveResponse = InvitationSaveResponse(message)
    }
}
