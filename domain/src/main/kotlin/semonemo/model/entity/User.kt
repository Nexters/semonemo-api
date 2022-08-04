package semonemo.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document
class User(
    nickname: String,
    group: String?,
) : AuditableDocument(), Serializable {

    @Id
    var id: Long? = null

    var nickname = nickname
        private set

    @Transient
    var authKey: String? = null
        private set

    var group = group
        private set

    companion object {
        const val serialVersionUID: Long = 23894278394214L
    }
}
