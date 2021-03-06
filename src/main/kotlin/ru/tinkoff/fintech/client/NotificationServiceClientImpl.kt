package ru.tinkoff.fintech.client

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class NotificationServiceClientImpl(
    @Value("\${transaction.rest.notification}")
    private val url: String? = null,
    private val restTemplate: RestTemplate
) : NotificationServiceClient {

    companion object {
        private val log = KotlinLogging.logger {  }
        private val headers = HttpHeaders()
    }

    override fun sendNotification(clientId: String, message: String) {
        runCatching {
            headers.accept = listOf(MediaType.APPLICATION_JSON)
            restTemplate.exchange("$url$clientId/message", HttpMethod.POST, HttpEntity(message, headers), String::class.java)
        }.onFailure {
            log.error { "Post request sendNotification failed" }
        }.onSuccess {
            log.info { "Post request sendNotification succeeded" }
        }
    }
}