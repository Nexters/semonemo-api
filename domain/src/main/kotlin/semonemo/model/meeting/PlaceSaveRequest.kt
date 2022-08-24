package semonemo.model.meeting

data class PlaceSaveRequest(
    val summary: String,
    val address: String,
    val mapLink: String,
) {

    fun toPlace(): Place = Place(summary = summary, address = address, mapLink = mapLink)
}
