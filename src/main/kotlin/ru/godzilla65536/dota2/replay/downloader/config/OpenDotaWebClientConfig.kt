package ru.godzilla65536.dota2.replay.downloader.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import ru.godzilla65536.dota2.replay.downloader.config.props.OpenDotaProps
import java.util.function.Consumer

@Configuration
class OpenDotaWebClientConfig(
    private val props: OpenDotaProps,
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    val openDotaWebClient: WebClient by lazy {
        WebClient
            .builder()
            .exchangeStrategies(
                ExchangeStrategies
                    .builder()
                    .codecs { it.defaultCodecs().maxInMemorySize(16 * 1024 * 1024) }
                    .build()
            )
            .baseUrl(props.baseUrl.toString())
            .filter(logRequest()).build()
    }

    private fun logRequest() = ExchangeFilterFunction.ofRequestProcessor { clientRequest ->
        logger.info("Request: {} {}", clientRequest.method(), clientRequest.url())
        clientRequest.headers()
            .forEach { name: String?, values: List<String?> ->
                values.forEach(
                    Consumer { value: String? ->
                        logger.info("{}={}",
                            name,
                            value)
                    })
            }
        Mono.just(clientRequest)
    }

}