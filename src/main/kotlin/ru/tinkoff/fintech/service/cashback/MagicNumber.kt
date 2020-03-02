package ru.tinkoff.fintech.service.cashback

import ru.tinkoff.fintech.model.TransactionInfo

class MagicNumber {
    companion object {
        private const val cashbackMagicNumber = 6.66
        private const val magicNumber = 666
    }

    fun increaseCashbackIf(transactionInfo: TransactionInfo): Double {
        return with(transactionInfo) {
            if (transactionSum.compareTo(magicNumber.toDouble()) == 0
                || (transactionSum % magicNumber.toDouble()).compareTo(0.0) == 0) {
                cashbackMagicNumber
            }
            else CashbackCalculatorImpl.cashbackNothing
        }
    }
}