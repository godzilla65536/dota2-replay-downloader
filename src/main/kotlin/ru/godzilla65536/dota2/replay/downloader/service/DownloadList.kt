package ru.godzilla65536.dota2.replay.downloader.service

import org.springframework.stereotype.Component
import java.util.*

@Component
class DownloadList {

    private val replaysToDownload = mutableSetOf<Long>()

    fun getReplays() = synchronized(this) {
        replaysToDownload
    }

    fun addAll(matchIds: Collection<Long>) = synchronized(this) {
        replaysToDownload.addAll(matchIds)
    }

    fun removeAll(matchIds: Collection<Long>) = synchronized(this) {
        replaysToDownload.removeAll(matchIds)
    }

    fun remove(matchId: Long) = synchronized(this) {
        replaysToDownload.remove(matchId)
    }

}