package ru.tinkoff.fintech.service.transaction

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.tinkoff.fintech.client.*
import ru.tinkoff.fintech.db.entity.LoyaltyPaymentEntity
import ru.tinkoff.fintech.db.repository.LoyaltyPaymentRepository
import ru.tinkoff.fintech.model.NotificationMessageInfo
import ru.tinkoff.fintech.model.Transaction
import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.CashbackCalculator
import ru.tinkoff.fintech.service.notification.NotificationMessageGenerator
import java.time.LocalDateTime

@Service
class TransactionServiceImpl(
    @Value("\${transaction.sign}")
    private val sign: String,
    private val cardServiceClient: CardServiceClient,
    private val clientService: ClientService,
    private val loyaltyServiceClient: LoyaltyServiceClient,
    private val notificationServiceClient: NotificationServiceClient,
    private val loyaltyPaymentRepository: LoyaltyPaymentRepository,
    private val cashbackCalculator: CashbackCalculator,
    private val notificationMessageGenerator: NotificationMessageGenerator
) : TransactionService {

    override fun processTransaction(transaction: Transaction) {
        val card = cardServiceClient.getCard(transaction.cardNumber)
        runBlocking(Dispatchers.IO) {
            val clientAsync = async { clientService.getClient(card.client) }
            val programAsync = async { loyaltyServiceClient.getLoyaltyProgram(card.loyaltyProgram) }
            val currentCashbackTotalValue = async {
                loyaltyPaymentRepository.findByCardIdAndDateTimeAndSign(card.id, transaction.time, sign).sumByDouble { it.value }
            }
            val client = clientAsync.await()
            val program = programAsync.await()
            val transactionInfo = TransactionInfo(
                program.name,
                transaction.value,
                currentCashbackTotalValue.await(),
                transaction.mccCode,
                client.birthDate,
                client.firstName,
                client.lastName,
                client.middleName
            )
            val cashback = cashbackCalculator.calculateCashback(transactionInfo)
            loyaltyPaymentRepository.save(LoyaltyPaymentEntity(
                value = cashback,
                cardId = card.id,
                sign = sign,
                transactionId = transaction.transactionId,
                dateTime = LocalDateTime.now()
            ))
            val notificationMessageInfo = NotificationMessageInfo(
                client.firstName,
                card.cardNumber,
                cashback,
                transaction.value,
                program.name,
                transaction.time
            )
            val message = notificationMessageGenerator.generateMessage(notificationMessageInfo)
            launch { notificationServiceClient.sendNotification(client.id, message) }
        }
    }
}