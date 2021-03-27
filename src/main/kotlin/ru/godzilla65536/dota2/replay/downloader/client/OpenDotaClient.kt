package ru.godzilla65536.dota2.replay.downloader.client

import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.stereotype.Component
import ru.godzilla65536.dota2.replay.downloader.config.OpenDotaWebClientConfig
import ru.godzilla65536.dota2.replay.downloader.config.props.OpenDotaProps
import ru.godzilla65536.dota2.replay.downloader.model.GameMode
import ru.godzilla65536.dota2.replay.downloader.model.RecentMatch
import ru.godzilla65536.dota2.replay.downloader.model.ReplayData

@Component
class OpenDotaClient(
    private val webClientConfig: OpenDotaWebClientConfig,
    private val props: OpenDotaProps,
) {

    suspend fun getRecentMatches(steamAccountId: Long, gameMode: GameMode? = null): Array<RecentMatch> =
        webClientConfig.openDotaWebClient
            .get()
            .uri { uriBuilder ->
                uriBuilder
                    .pathSegment("players")
                    .pathSegment(steamAccountId.toString())
                    .pathSegment("recentMatches")
                    .queryParam("game_mode", gameMode?.id)
                    .build()
            }
            .retrieve()
            .bodyToMono(Array<RecentMatch>::class.java)
            .awaitSingle()


    suspend fun getReplayData(matchId: Long): ReplayData {
        val arrayOfReplayData = webClientConfig.openDotaWebClient
            .get()
            .uri { uriBuilder ->
                uriBuilder
                    .pathSegment("replays")
                    .queryParam("match_id", matchId)
                    .build()
            }
            .retrieve()
            .bodyToMono(Array<ReplayData>::class.java)
            .awaitSingle()

        if (arrayOfReplayData.isEmpty()) throw RuntimeException("Replay not found")
        return arrayOfReplayData[0]
    }

}