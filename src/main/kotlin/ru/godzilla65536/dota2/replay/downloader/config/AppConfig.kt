package ru.godzilla65536.dota2.replay.downloader.config

import kotlinx.coroutines.reactor.mono
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import ru.godzilla65536.dota2.replay.downloader.client.OpenDotaClient
import ru.godzilla65536.dota2.replay.downloader.model.GameMode
import ru.godzilla65536.dota2.replay.downloader.service.DownloadReplayService
import ru.godzilla65536.dota2.replay.downloader.service.ReplaysStorageService

@Configuration
@EnableScheduling
class AppConfig(
    private val openDotaClient: OpenDotaClient,
    private val downloadService: DownloadReplayService,
    private val storageService: ReplaysStorageService
) {

    @Scheduled(fixedDelay = 30000)
    fun downloadReplays() = mono {

        val recentMatches = openDotaClient.getRecentMatches(GameMode.TURBO)
        val savedReplays = storageService.getSavedReplays()

        val missingReplays = recentMatches.map { it.matchId }.toSet().minus(savedReplays)

        if (missingReplays.isNotEmpty()) {
            missingReplays.forEach { matchId ->
                val match = openDotaClient.getMatch(matchId)
                downloadService.downloadReplay(match.replayUri)
            }
        }

    }.block()

}