package semonemo.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Counters(
    id: String,
    seq: Long,
) {

    @Id
    var id = id
        private set

    var seq = seq
        private set

    fun increaseSeq() {
        seq = seq.plus(1L)
    }
}
