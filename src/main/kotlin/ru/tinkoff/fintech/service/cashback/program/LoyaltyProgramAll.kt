package ru.tinkoff.fintech.service.cashback.program

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.CashbackCalculator
import ru.tinkoff.fintech.service.cashback.MCC_SOFTWARE
import ru.tinkoff.fintech.service.cashback.utils.CashbackUtils

class LoyaltyProgramAll : CashbackCalculator {
    companion object {
        private const val cashbackNothing = 0.0
    }

    override fun calculateCashback(transactionInfo: TransactionInfo): Double {
        return with(transactionInfo) {
            if (mccCode == MCC_SOFTWARE && CashbackUtils().isPalindromeIf(transactionSum)) {
                CashbackUtils().cashbackLoyaltyProgramAll(transactionInfo)
            }
            else cashbackNothing
        }
    }
}