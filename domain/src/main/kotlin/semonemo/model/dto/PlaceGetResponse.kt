package semonemo.model.dto

import semonemo.model.entity.Place

data class PlaceGetResponse(
    val summary: String,
    val address: String,
    val mapLink: String,
) {
    companion object {
        fun of(place: Place): PlaceGetResponse = PlaceGetResponse(
            summary = place.summary,
            address = place.address,
            mapLink = place.mapLink
        )
    }
}
