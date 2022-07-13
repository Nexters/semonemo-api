package semonemo.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class User(
    nickname: String
) {

    @Id
    var id: String? = null
        private set

    var nickname = nickname
        private set
}
