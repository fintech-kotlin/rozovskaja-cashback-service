package ru.tinkoff.fintech.listener

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import ru.tinkoff.fintech.model.Transaction
import ru.tinkoff.fintech.service.transaction.TransactionService

@Component
class TransactionListener(
    private val transactionService: TransactionService,
    private val objectMapper: ObjectMapper
) {

    companion object {
        private val log = KotlinLogging.logger {  }
    }

    @KafkaListener(topics = ["\${spring.kafka.consumer.topic}"], groupId = "\${spring.kafka.consumer.groupId}")
    fun onMessage(message: String) {
        val transaction = objectMapper.readValue(message, Transaction::class.java)
        if (transaction.mccCode != null)
            transactionService.processTransaction(transaction)
        else
            log.info("Do not accrue cashback for transactions other than purchase." +
                    "Marked by mccCode = ${transaction.mccCode} and operationType = ${transaction.operationType}")
    }
}


