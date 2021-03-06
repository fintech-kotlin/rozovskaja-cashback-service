package ru.tinkoff.fintech.service.notification

import org.springframework.stereotype.Component

@Component
class CardNumberMaskerImpl: CardNumberMasker {

    override fun mask(cardNumber: String, maskChar: Char, start: Int, end: Int): String {
        require (start <= end) { "Start index cannot be greater than end index" }
        if (cardNumber.isNotEmpty()) {
            val actualEnd = if (cardNumber.length < end) cardNumber.length else end
            return cardNumber.replaceRange(start, actualEnd, maskChar.toString().repeat(actualEnd - start))
        }
        return cardNumber
    }
}