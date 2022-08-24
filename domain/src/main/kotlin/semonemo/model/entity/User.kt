package semonemo.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import semonemo.model.AuditableDocument
import java.io.Serializable

/**
 * 패키지 위치 바꾸게되면 deserialize 과정 중 이슈 발생하므로
 * 패키지 위치 등 클래스 관련 정보 함부로 바꾸면 안됨
 */
@Document("user")
class User(
    var nickname: String,
    var group: String?,
    var profileImageUrl: String? = null,
) : AuditableDocument(), Serializable {

    @Id
    var id: Long? = null

    @Transient
    var authKey: String? = null
        private set

    @Transient
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
