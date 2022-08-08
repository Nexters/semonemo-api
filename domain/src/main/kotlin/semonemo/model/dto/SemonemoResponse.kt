package semonemo.model.dto

data class SemonemoResponse(
    val statusCode: Int = 200,
    val message: String? = null,
    val data: Any? = null,
)
