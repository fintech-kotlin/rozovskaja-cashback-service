package ru.tinkoff.fintech.listener

import org.springframework.kafka.annotation.KafkaListener

class TransactionListener() {

    @KafkaListener(topics = ["transactions"], groupId = "cashback")
    fun onMessage(message: String) {
        println("received: " + message)
    }

}


