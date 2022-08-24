package semonemo.model

data class SemonemoResponse(
    val statusCode: Int = 200,
    val message: String? = null,
    val data: Any? = null,
)
