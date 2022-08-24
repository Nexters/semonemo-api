package semonemo.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("counters")
class Counters(
    @Id
    var id: String,
    var seq: Long,
) {

    fun increaseSeqOne() {
        seq = seq.plus(1L)
    }
}
