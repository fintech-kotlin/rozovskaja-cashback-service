package ru.tinkoff.fintech.service.cashback.program

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.*
import ru.tinkoff.fintech.service.cashback.MCC_BEER
import ru.tinkoff.fintech.service.cashback.utils.CashbackUtils

class LoyaltyProgramBeer : CashbackCalculator {
    companion object {
        private const val cashbackFullName = 0.1
        private const val cashbackShortName = 0.07
        private const val cashbackCurrentMonth = 0.05
        private const val cashbackPreviousOrNextMonth = 0.03
        private const val cashbackStandard = 0.02

        private const val specialName = "Олег"
        private const val specialSurname = "Олегов"
    }

    override fun calculateCashback(transactionInfo: TransactionInfo): Double {
        return with(transactionInfo) {
            if (mccCode == MCC_BEER && firstName.equals(specialName, true) && lastName.equals(specialSurname, true)) {
                CashbackUtils().cashback(transactionSum, cashbackFullName)
            } else if (mccCode == MCC_BEER && firstName.equals(specialName, true)) {
                CashbackUtils().cashback(transactionSum, cashbackShortName)
            } else if (mccCode == MCC_BEER && CashbackUtils().checkCurrentMonth(firstName)) {
                CashbackUtils().cashback(transactionSum, cashbackCurrentMonth)
            } else if (mccCode == MCC_BEER && CashbackUtils().checkPreviousOrNextMonth(firstName)) {
                CashbackUtils().cashback(transactionSum, cashbackPreviousOrNextMonth)
            } else
                CashbackUtils().cashback(transactionSum, cashbackStandard)
        }
    }
}