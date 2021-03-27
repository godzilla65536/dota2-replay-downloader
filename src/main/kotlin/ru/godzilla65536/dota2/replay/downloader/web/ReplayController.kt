package ru.godzilla65536.dota2.replay.downloader.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.godzilla65536.dota2.replay.downloader.service.DownloadList

@RestController
class ReplayController(
    private val downloadList: DownloadList,
) {

    @GetMapping("add-replays-to-download-list")
    fun addReplaysToDownloadList(@RequestParam matchIds: List<Long>) {
        downloadList.addAll(matchIds)
    }

}