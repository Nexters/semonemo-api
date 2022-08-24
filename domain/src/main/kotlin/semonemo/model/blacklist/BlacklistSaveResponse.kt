package semonemo.model.blacklist

data class BlacklistSaveResponse(
    val id: Long,
) {

    companion object {
        fun of(blacklist: Blacklist): BlacklistSaveResponse = BlacklistSaveResponse(blacklist.id)
    }
}
