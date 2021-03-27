package ru.godzilla65536.dota2.replay.downloader.config.props

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.net.URI

@ConstructorBinding()
@ConfigurationProperties(prefix = "scheduler")
data class SchedulerProps(
    val maxDownloadsAtTime: Int,
)