package semonemo.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("stamp")
class Stamp(
    @Id
    var id: Long,
    var invitation: Invitation,
    var confirmed: Boolean = false,
) : AuditableDocument()
