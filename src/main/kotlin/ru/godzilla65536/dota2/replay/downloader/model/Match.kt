package ru.godzilla65536.dota2.replay.downloader.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URI

data class Match(
    @JsonProperty("match_id")
    val matchId: Long,

    @JsonProperty("replay_url")
    val replayUri: URI,
)
