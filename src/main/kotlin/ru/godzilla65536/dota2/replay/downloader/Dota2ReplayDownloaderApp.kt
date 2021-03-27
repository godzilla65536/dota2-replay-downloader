package ru.godzilla65536.dota2.replay.downloader

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener
import ru.godzilla65536.dota2.replay.downloader.service.StorageService

@SpringBootApplication
@ConfigurationPropertiesScan
class Dota2ReplayDownloaderApp(
    private val storageService: StorageService,
) {

    @EventListener(ApplicationReadyEvent::class)
    fun init() {
        storageService.init()
    }

}


fun main(args: Array<String>) {
    runApplication<Dota2ReplayDownloaderApp>(*args)
}
