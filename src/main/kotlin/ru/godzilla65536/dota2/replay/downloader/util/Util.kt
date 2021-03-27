package ru.godzilla65536.dota2.replay.downloader.util

import ru.godzilla65536.dota2.replay.downloader.model.ReplayData
import java.net.URI
import java.nio.file.Paths

fun buildReplayName(replayUri: URI) =
    Paths.get(replayUri.path).fileName.toString().split("_")[0] + ".dem"

fun buildReplayUri(replayData: ReplayData) =
    URI("http://replay${replayData.cluster}.valve.net/570/${replayData.matchId}_${replayData.replaySalt}.dem.bz2")