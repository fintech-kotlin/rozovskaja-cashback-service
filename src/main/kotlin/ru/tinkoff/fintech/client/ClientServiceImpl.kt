package ru.tinkoff.fintech.client

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import ru.tinkoff.fintech.model.Client

@Service
class ClientServiceImpl(
    @Value("\${transaction.rest.client}")
    private val url: String? = null,
    private val restTemplate: RestTemplate
) : ClientService {

    companion object {
        private val log = KotlinLogging.logger {  }
    }

    override fun getClient(id: String): Client =
        runCatching {
            restTemplate.getForEntity("$url$id", Client::class.java).body!!
        }.onFailure {
            log.error { "Failed to get client with id = $id: ${it.message}" }
        }.getOrThrow()
}