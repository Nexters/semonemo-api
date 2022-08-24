package semonemo.model.entity

import org.springframework.data.mongodb.core.mapping.Document

@Document("place")
class Place(
    val summary: String,
    val address: String,
    val mapLink: String,
) {

    init {
        validateNotEmpty(summary, address, mapLink)
    }

    private fun validateNotEmpty(summary: String, address: String, mapLink: String) {
        check(summary.isNotBlank()) { "장소명이 비었습니다." }
        check(address.isNotBlank()) { "장소 주소가 비었습니다." }
        check(mapLink.isNotBlank()) { "장소 링크가 비었습니다." }
    }
}
