package ru.godzilla65536.dota2.replay.downloader.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


@Service
class StorageService {

    @Value("\${replays-path}")
    private val replaysPath: Path = Path.of("")

    fun init() {
        Files.createDirectories(replaysPath)
    }

    fun getSavedReplays() =
        File(replaysPath.toAbsolutePath().toString())
            .walk()
            .asIterable()
            .filter { it.isFile }
            .filter { it.name.matches(Regex("\\d.dem")) }
            .map { it.nameWithoutExtension.toLong() }
            .toSet()

    fun save(byteArray: ByteArray, fileName: String) {
        val filePath = Paths.get(replaysPath.toString(), fileName)
        Files.write(filePath, byteArray)
    }

    fun deleteAll() {
        FileSystemUtils.deleteRecursively(replaysPath)
    }

}