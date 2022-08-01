package semonemo.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Invitation(
    id: Long,
    meeting: Meeting,
    user: User,
    wantToAttend: Boolean,
    attended: Boolean,
) : AuditableDocument() {

    @Id
    var id = id
        private set

    var meeting = meeting
        private set

    var user = user
        private set

    var wantToAttend = wantToAttend
        private set

    var attended = attended
        private set
}
