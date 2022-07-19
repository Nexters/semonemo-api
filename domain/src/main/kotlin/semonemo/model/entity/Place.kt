package semonemo.model.entity

import org.springframework.data.mongodb.core.mapping.Document

@Document
class Place(
    summary: String,
    address: String,
    mapLink: String,
) {

    var summary = summary
        private set

    var address = address
        private set

    var mapLink = mapLink
        private set
}
