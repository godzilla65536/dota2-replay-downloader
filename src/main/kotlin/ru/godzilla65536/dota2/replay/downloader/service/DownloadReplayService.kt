package ru.godzilla65536.dota2.replay.downloader.service

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import ru.godzilla65536.dota2.replay.downloader.model.ReplayData
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.net.URI
import java.nio.file.Paths

@Service
class DownloadReplayService(
    private val storageService: ReplaysStorageService,
) {

    fun downloadReplay(uri: URI) {
        val compressedReplay = RestTemplate().getForObject(uri, ByteArray::class.java)
            ?: throw RuntimeException("Replay not found...")
        val replay = uncompressReplay(compressedReplay)
        storageService.save(replay, buildReplayName(uri))
    }

    private fun uncompressReplay(replay: ByteArray): ByteArray {
        val bufferedInputStream = BufferedInputStream(replay.inputStream())
        val bZip2CompressorInputStream = BZip2CompressorInputStream(bufferedInputStream)

        val byteArrayOutputStream = ByteArrayOutputStream()
        bZip2CompressorInputStream.transferTo(byteArrayOutputStream)

        return byteArrayOutputStream.toByteArray()
    }

    private fun buildReplayName(replayUri: URI) =
        Paths.get(replayUri.path).fileName.toString().split("_")[0] + ".dem"

    fun buildReplayUri(replayData: ReplayData) =
        URI("http://replay${replayData.cluster}.valve.net/570/${replayData.matchId}_${replayData.replaySalt}.dem.bz2")

}