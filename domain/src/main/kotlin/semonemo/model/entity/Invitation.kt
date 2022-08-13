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
    stamped: Boolean = false,
) : AuditableDocument() {

    @Id
    var id = id

    var meeting = meeting
        private set

    var user = user
        private set

    var wantToAttend = wantToAttend

    var attended = attended

    var stamped = stamped

    val isMeetingEnd: Boolean
        get() = meeting.isEnd

    companion object {
        fun host(id: Long, meeting: Meeting, host: User): Invitation =
            Invitation(id = id, meeting = meeting, user = host, wantToAttend = true, attended = true)

        fun guest(id: Long, meeting: Meeting, guest: User): Invitation =
            Invitation(id = id, meeting = meeting, user = guest)
    }
}
