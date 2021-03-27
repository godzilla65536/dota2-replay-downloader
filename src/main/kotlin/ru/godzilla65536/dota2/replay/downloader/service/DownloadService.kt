package ru.godzilla65536.dota2.replay.downloader.service

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import ru.godzilla65536.dota2.replay.downloader.exception.ReplayNotFoundException
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.net.URI

@Service
class DownloadService {

    fun downloadReplay(uri: URI): ByteArray {
        return try {
            val compressedReplay = RestTemplate().getForObject(uri, ByteArray::class.java)
            uncompressReplay(compressedReplay!!)
        } catch (e: HttpClientErrorException.NotFound) {
            throw ReplayNotFoundException()
        }
    }

    private fun uncompressReplay(replay: ByteArray): ByteArray {
        val bufferedInputStream = BufferedInputStream(replay.inputStream())
        val bZip2CompressorInputStream = BZip2CompressorInputStream(bufferedInputStream)

        val byteArrayOutputStream = ByteArrayOutputStream()
        bZip2CompressorInputStream.transferTo(byteArrayOutputStream)

        return byteArrayOutputStream.toByteArray()
    }

}