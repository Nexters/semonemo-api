package semonemo.model.blacklist

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import semonemo.model.AuditableDocument
import semonemo.model.entity.User

@Document("blacklist")
class Blacklist(
    @Id
    var id: Long,
    val user: User,
    val meetingId: Long,
    val content: String,
) : AuditableDocument()
