package ru.tinkoff.fintech.listener

import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import ru.tinkoff.fintech.model.Transaction
import ru.tinkoff.fintech.service.transaction.TransactionService

@Component
class TransactionListener(private val transactionService: TransactionService) {

    companion object {
        private val log = KotlinLogging.logger {  }
    }

    @KafkaListener(topics = ["\${paymentprocessing.kafka.consumer.topic}"],
        groupId = "\${paymentprocessing.kafka.consumer.group-id}")
    fun onMessage(transaction: Transaction) {
        if (transaction.mccCode != null)
            transactionService.getTransaction(transaction)
        else
            log.info("Do not accrue cashback for transactions other than purchase." +
                    "Marked by mccCode = ${transaction.mccCode} and operationType = ${transaction.operationType}")
    }
}


