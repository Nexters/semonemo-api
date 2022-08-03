package semonemo.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Invitation(
    id: Long,
    meeting: Meeting,
    user: User,
    wantToAttend: Boolean = false,
    attended: Boolean = false,
) : AuditableDocument() {

    @Id
    var id = id

    var meeting = meeting
        private set

    var user = user
        private set

    var wantToAttend = wantToAttend
        private set

    var attended = attended
        private set

    companion object {
        fun host(id: Long, meeting: Meeting, host: User): Invitation =
            Invitation(id = id, meeting = meeting, user = host, wantToAttend = true, attended = true)

        fun guest(id: Long, meeting: Meeting, guest: User): Invitation =
            Invitation(id = id, meeting = meeting, user = guest)
    }
}
