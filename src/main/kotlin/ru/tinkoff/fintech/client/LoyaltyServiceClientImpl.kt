package ru.tinkoff.fintech.client

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import ru.tinkoff.fintech.model.LoyaltyProgram

@Component
class LoyaltyServiceClientImpl(
    @Value("\${rest.loyalty}")
    private val url: String? = null,
    private val restTemplate: RestTemplate
) : LoyaltyServiceClient {

    companion object {
        private val log = KotlinLogging.logger {  }
    }

    override fun getLoyaltyProgram(id: String): LoyaltyProgram =
        runCatching {
            restTemplate.getForEntity("$url$id", LoyaltyProgram::class.java).body!!
        }.onFailure {
            log.error { "Failed to get loyalty program with id = $id: ${it.message}" }
        }.getOrThrow()
}