package ru.tinkoff.fintech.service.cashback.program

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.CashbackCalculator
import ru.tinkoff.fintech.service.cashback.utils.CashbackUtils

class LoyaltyProgramBlack : CashbackCalculator {
    companion object {
        private const val cashbackLoyaltyProgramBlack = 0.01
    }

    override fun calculateCashback(transactionInfo: TransactionInfo): Double {
        return with(transactionInfo) {
            CashbackUtils().cashback(transactionSum, cashbackLoyaltyProgramBlack)
        }
    }
}