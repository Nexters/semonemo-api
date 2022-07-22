package semonemo.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document
class User(
    nickname: String,
): Serializable {

    @Id
    var id: Long? = null
        private set

    var nickname = nickname
        private set

    var authKey: String? = null
        private set

    companion object {
        const val serialVersionUID : Long = 23894278394214L
    }
}
