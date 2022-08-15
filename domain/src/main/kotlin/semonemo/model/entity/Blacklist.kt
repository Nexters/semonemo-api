package semonemo.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Blacklist(
    id: Long,
    user: User,
    meetingId: Long,
    content: String,
) : AuditableDocument() {

    @Id
    var id = id

    var user = user
        private set

    var meetingId = meetingId
        private set

    var content = content
        private set
}
