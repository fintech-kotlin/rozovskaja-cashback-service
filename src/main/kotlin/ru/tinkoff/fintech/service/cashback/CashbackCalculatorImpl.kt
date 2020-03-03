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
        private const val cashbackMagicNumber = 6.66
        private const val magicNumber = 666

        @JvmStatic fun increaseCashbackIf(transactionInfo: TransactionInfo): Double {
            return with(transactionInfo) {
                if (transactionSum.compareTo(magicNumber.toDouble()) == 0
                    || (transactionSum % magicNumber.toDouble()).compareTo(0.0) == 0) {
                    cashbackMagicNumber
                }
                else cashbackNothing
            }
        }
    }

    override fun calculateCashback(transactionInfo: TransactionInfo): Double {
        with(transactionInfo) {
            var res = when (loyaltyProgramName) {
                LOYALTY_PROGRAM_BLACK -> LoyaltyProgramBlack().calculateCashback(transactionInfo)
                LOYALTY_PROGRAM_BEER -> LoyaltyProgramBeer().calculateCashback(transactionInfo)
                LOYALTY_PROGRAM_ALL -> LoyaltyProgramAll().calculateCashback(transactionInfo)
                else -> cashbackNothing
            }
            res += increaseCashbackIf(transactionInfo)
            return when {
                cashbackTotalValue >= MAX_CASH_BACK -> cashbackNothing
                res + cashbackTotalValue > MAX_CASH_BACK -> MAX_CASH_BACK - cashbackTotalValue
                else -> res
            }
        }
    }
}