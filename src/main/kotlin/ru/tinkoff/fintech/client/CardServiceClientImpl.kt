package ru.tinkoff.fintech.client

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import ru.tinkoff.fintech.model.Card

@Component
class CardServiceClientImpl(
    @Value("\${rest.card}")
    private val url: String? = null,
    private val restTemplate: RestTemplate
) : CardServiceClient {

    companion object {
        private val log = KotlinLogging.logger {  }
    }

    override fun getCard(id: String): Card =
        runCatching {
            restTemplate.getForEntity("$url$id", Card::class.java).body!!
        }.onFailure {
            log.error { "Failed to get client card with id = $id: ${it.message}" }
        }.getOrThrow()
}