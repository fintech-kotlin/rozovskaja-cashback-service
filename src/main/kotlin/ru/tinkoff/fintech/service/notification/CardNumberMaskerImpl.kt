package ru.tinkoff.fintech.service.notification

class CardNumberMaskerImpl: CardNumberMasker {

    override fun mask(cardNumber: String, maskChar: Char, start: Int, end: Int): String {
        if (start > end)
            throw Exception("Start index cannot be greater than end index")
        if (cardNumber.isNotEmpty()) {
            val actualEnd = if (cardNumber.length < end) cardNumber.length else end
            return cardNumber.replaceRange(start, actualEnd, maskChar.toString().repeat(actualEnd - start))
        }
        return cardNumber
    }
}