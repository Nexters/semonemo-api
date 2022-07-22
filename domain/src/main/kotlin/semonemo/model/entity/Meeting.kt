package semonemo.model.entity

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

@Document
class Meeting(
    host: User,
    place: Place,
    startDate: LocalDateTime,
    endDate: LocalDateTime,
): AuditableDocument() {

    var host = host
        private set

    var place = place
        private set

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    var startDate = startDate
        private set

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    var endDate = endDate
        private set
}
