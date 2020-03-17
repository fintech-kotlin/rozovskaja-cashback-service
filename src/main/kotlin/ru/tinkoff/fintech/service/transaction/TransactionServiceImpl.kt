package ru.tinkoff.fintech.service.transaction

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
    @Value("\${spring.sign}")
    private val sign: String,
    private val cardServiceClient: CardServiceClient,
    private val clientService: ClientService,
    private val loyaltyServiceClient: LoyaltyServiceClient,
    private val notificationServiceClient: NotificationServiceClient,
    private val loyaltyPaymentRepository: LoyaltyPaymentRepository,
    private val cashbackCalculator: CashbackCalculator,
    private val notificationMessageGenerator: NotificationMessageGenerator
) : TransactionService {

    override fun getTransaction(transaction: Transaction) {
        val card = cardServiceClient.getCard(transaction.cardNumber)
        val client = clientService.getClient(card.client)
        val program = loyaltyServiceClient.getLoyaltyProgram(card.loyaltyProgram)
        val currentCashbackTotalValue =
            loyaltyPaymentRepository.findByCardIdAndDateTimeAndSign(card.id, transaction.time, sign).sumByDouble { it.value }
        val transactionInfo = TransactionInfo(
            program.name,
            transaction.value,
            currentCashbackTotalValue,
            transaction.mccCode,
            client.birthDate,
            client.firstName,
            client.lastName,
            client.middleName
        )
        val cashback = cashbackCalculator.calculateCashback(transactionInfo)
        loyaltyPaymentRepository.save(LoyaltyPaymentEntity(
            0, // TD: should I increment id with every consequent saving..?
            cashback,
            card.id,
            sign,
            transaction.transactionId,
            LocalDateTime.now()
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
        notificationServiceClient.sendNotification(client.id, message)
    }
}