package ru.tinkoff.fintech.service.cashback

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.program.LoyaltyProgramAll
import ru.tinkoff.fintech.service.cashback.program.LoyaltyProgramBeer
import ru.tinkoff.fintech.service.cashback.program.LoyaltyProgramBlack

internal const val LOYALTY_PROGRAM_BLACK = "BLACK"
internal const val LOYALTY_PROGRAM_ALL = "ALL"
internal const val LOYALTY_PROGRAM_BEER = "BEER"
internal const val MAX_CASH_BACK = 3000.0
internal const val MCC_SOFTWARE = 5734
internal const val MCC_BEER = 5921

class CashbackCalculatorImpl : CashbackCalculator {

    companion object {
        internal const val cashbackNothing = 0.0
    }

    override fun calculateCashback(transactionInfo: TransactionInfo): Double {
        var res = with(transactionInfo) {
            when (loyaltyProgramName) {
                LOYALTY_PROGRAM_BLACK -> LoyaltyProgramBlack().calculateCashback(transactionInfo)
                LOYALTY_PROGRAM_BEER -> LoyaltyProgramBeer().calculateCashback(transactionInfo)
                LOYALTY_PROGRAM_ALL -> LoyaltyProgramAll().calculateCashback(transactionInfo)
                else -> cashbackNothing
            }
        }
        res += MagicNumber().increaseCashbackIf(transactionInfo)
        return with(transactionInfo) {
            if (cashbackTotalValue >= MAX_CASH_BACK) cashbackNothing
            if (res + cashbackTotalValue > MAX_CASH_BACK) MAX_CASH_BACK - cashbackTotalValue
            else res
        }
    }
}