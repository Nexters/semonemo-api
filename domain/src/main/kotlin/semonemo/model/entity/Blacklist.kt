package semonemo.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("blacklist")
class Blacklist(
    @Id
    var id: Long,
    val user: User,
    val meetingId: Long,
    val content: String,
) : AuditableDocument()
