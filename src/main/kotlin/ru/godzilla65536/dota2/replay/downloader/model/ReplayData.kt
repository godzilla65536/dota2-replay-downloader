package ru.godzilla65536.dota2.replay.downloader.model

import com.fasterxml.jackson.annotation.JsonProperty

data class ReplayData(
    @JsonProperty("match_id")
    val matchId: Long,

    val cluster: Int,

    @JsonProperty("replay_salt")
    val replaySalt: Long,
)
