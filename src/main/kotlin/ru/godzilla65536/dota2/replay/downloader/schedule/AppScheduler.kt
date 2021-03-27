package ru.godzilla65536.dota2.replay.downloader.schedule

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.godzilla65536.dota2.replay.downloader.client.OpenDotaClient
import ru.godzilla65536.dota2.replay.downloader.config.props.OpenDotaProps
import ru.godzilla65536.dota2.replay.downloader.config.props.SchedulerProps
import ru.godzilla65536.dota2.replay.downloader.exception.ReplayNotFoundException
import ru.godzilla65536.dota2.replay.downloader.service.DownloadList
import ru.godzilla65536.dota2.replay.downloader.service.DownloadService
import ru.godzilla65536.dota2.replay.downloader.service.StorageService
import ru.godzilla65536.dota2.replay.downloader.util.buildReplayName
import ru.godzilla65536.dota2.replay.downloader.util.buildReplayUri

@Component
@EnableScheduling
class AppScheduler(
    private val client: OpenDotaClient,
    private val downloadList: DownloadList,
    private val storageService: StorageService,
    private val downloadService: DownloadService,
    private val schedulerProps: SchedulerProps,
    private val openDotaProps: OpenDotaProps
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Scheduled(fixedDelay = 10000)
    fun findReplaysForRecentMatches() = mono {
        val recentMatches = openDotaProps.steamAccountIds.map { client.getRecentMatches(it) }.flatMap { it.toList() }
        val savedReplays = storageService.getSavedReplays()
        val newReplays = recentMatches.map { it.matchId }.toSet().minus(savedReplays)
        if (newReplays.isNotEmpty()) downloadList.addAll(newReplays)
    }.block()


    @Scheduled(fixedDelay = 20000)
    fun download() = mono {

        downloadList.removeAll(storageService.getSavedReplays())
        if (downloadList.getReplays().isEmpty()) return@mono

        val downloadNow = downloadList.getReplays().take(schedulerProps.maxDownloadsAtTime)

        downloadNow.map { matchId ->
            async {
                try {
                    val replayData = client.getReplayData(matchId)
                    val replayUri = buildReplayUri(replayData)
                    val replay = downloadService.downloadReplay(replayUri)
                    storageService.save(replay, buildReplayName(replayUri))
                    downloadList.remove(matchId)
                } catch (e: ReplayNotFoundException) {
                    downloadList.remove(matchId)
                    logger.error("Cannot download replay for match $matchId, replay not found", e)
                }
            }
        }.awaitAll()

    }.block()

}