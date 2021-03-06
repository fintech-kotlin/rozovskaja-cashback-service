package ru.tinkoff.fintech.service.notification

import org.springframework.stereotype.Component
import ru.tinkoff.fintech.model.NotificationMessageInfo

@Component
class NotificationMessageGeneratorImpl(
    private val cardNumberMasker: CardNumberMasker
) : NotificationMessageGenerator {

    override fun generateMessage(notificationMessageInfo: NotificationMessageInfo): String {
        return with(notificationMessageInfo) {
            """
            Уважаемый, $name!
            Спешим Вам сообщить, что на карту ${cardNumberMasker.mask(cardNumber)}
            начислен cashback в размере $cashback
            за категорию $category.
            Спасибо за покупку $transactionDate
            """.trimIndent()
        }
    }
}