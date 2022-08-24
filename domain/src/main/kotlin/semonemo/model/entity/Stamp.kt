package semonemo.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Stamp(
    id: Long,
    invitation: Invitation,
    confirmed: Boolean = false,
) : AuditableDocument() {

    @Id
    var id = id

    var invitation = invitation
        private set

    var confirmed = confirmed
}
