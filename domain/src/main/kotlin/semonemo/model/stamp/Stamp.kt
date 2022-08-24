package semonemo.model.stamp

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import semonemo.model.AuditableDocument
import semonemo.model.invitation.Invitation

@Document("stamp")
class Stamp(
    @Id
    var id: Long,
    var invitation: Invitation,
    var confirmed: Boolean = false,
) : AuditableDocument()
