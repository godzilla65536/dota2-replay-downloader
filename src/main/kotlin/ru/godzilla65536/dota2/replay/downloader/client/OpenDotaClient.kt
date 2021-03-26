package ru.godzilla65536.dota2.replay.downloader.client

import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.stereotype.Component
import ru.godzilla65536.dota2.replay.downloader.config.OpenDotaWebClientConfig
import ru.godzilla65536.dota2.replay.downloader.config.props.OpenDotaProps
import ru.godzilla65536.dota2.replay.downloader.model.GameMode
import ru.godzilla65536.dota2.replay.downloader.model.Match
import ru.godzilla65536.dota2.replay.downloader.model.RecentMatch

@Component
class OpenDotaClient(
    private val webClientConfig: OpenDotaWebClientConfig,
    private val props: OpenDotaProps,
) {

    suspend fun getRecentMatches(gameMode: GameMode): Array<RecentMatch> =
        webClientConfig.openDotaWebClient
            .get()
            .uri { uriBuilder ->
                uriBuilder
                    .pathSegment("players")
                    .pathSegment(props.steamAccountId.toString())
                    .pathSegment("recentMatches")
                    .queryParam("game_mode", gameMode.id)
                    .build()
            }
            .retrieve()
            .bodyToMono(Array<RecentMatch>::class.java)
            .awaitSingle()


    suspend fun getMatch(matchId: Long): Match =
        webClientConfig.openDotaWebClient
            .get()
            .uri { uriBuilder ->
                uriBuilder
                    .pathSegment("matches")
                    .pathSegment(matchId.toString())
                    .build()
            }
            .retrieve()
            .bodyToMono(Match::class.java)
            .awaitSingle()

}