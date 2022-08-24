package semonemo.model.invitation

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import semonemo.model.AuditableDocument
import semonemo.model.meeting.Meeting
import semonemo.model.entity.User

@Document("invitation")
class Invitation(
    @Id
    var id: Long,
    val meeting: Meeting,
    val user: User,
    var wantToAttend: Boolean = false,
    var attended: Boolean = false,
    var stamped: Boolean = false,
) : AuditableDocument() {

    val isMeetingEnd: Boolean
        get() = meeting.isEnd

    companion object {
        fun host(id: Long, meeting: Meeting, host: User): Invitation =
            Invitation(id = id, meeting = meeting, user = host, wantToAttend = true, attended = true)

        fun guest(id: Long, meeting: Meeting, guest: User): Invitation =
            Invitation(id = id, meeting = meeting, user = guest)
    }
}
