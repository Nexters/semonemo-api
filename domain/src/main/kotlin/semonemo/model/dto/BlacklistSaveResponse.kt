package semonemo.model.dto

import semonemo.model.entity.Blacklist

data class BlacklistSaveResponse(
    val id: Long,
) {

    companion object {
        fun of(blacklist: Blacklist): BlacklistSaveResponse = BlacklistSaveResponse(blacklist.id)
    }
}
