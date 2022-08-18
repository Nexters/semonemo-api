package semonemo.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document
class User(
    nickname: String,
    group: String?,
    profileImageUrl: String? = null,
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

    var profileImageUrl = profileImageUrl
        private set

    var attended: Boolean? = null

    companion object {
        const val serialVersionUID: Long = 23894278394214L
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false

        if (id != other.id) return false
        if (nickname != other.nickname) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + nickname.hashCode()
        return result
    }
}
