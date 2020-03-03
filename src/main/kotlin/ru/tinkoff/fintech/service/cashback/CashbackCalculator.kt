package ru.tinkoff.fintech.service.cashback

import ru.tinkoff.fintech.model.TransactionInfo
import kotlin.math.round

interface CashbackCalculator {

    fun calculateCashback(transactionInfo: TransactionInfo): Double

    fun CashbackCalculator.cashback(transactionSum: Double, cashbackPercent: Double) =
            round(transactionSum * cashbackPercent * 100) / 100
}