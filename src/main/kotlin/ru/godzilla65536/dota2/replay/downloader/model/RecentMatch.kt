package ru.godzilla65536.dota2.replay.downloader.model

import com.fasterxml.jackson.annotation.JsonProperty

data class RecentMatch(
    @JsonProperty("match_id")
    val matchId: Long
)
