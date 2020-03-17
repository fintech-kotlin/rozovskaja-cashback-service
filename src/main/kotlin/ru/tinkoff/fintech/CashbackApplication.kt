package ru.tinkoff.fintech

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka
import ru.tinkoff.fintech.model.Transaction
import ru.tinkoff.fintech.service.transaction.TransactionServiceImpl
import java.time.LocalDateTime

@SpringBootApplication
@EnableKafka
class CashbackApplication

fun main(args: Array<String>) {
    runApplication<CashbackApplication>(*args)
}