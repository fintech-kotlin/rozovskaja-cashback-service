package ru.tinkoff.fintech.service.cashback.program

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.*
import ru.tinkoff.fintech.service.cashback.MCC_BEER
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

class LoyaltyProgramBeer : CashbackCalculator {
    companion object {
        private const val cashbackFullName = 0.1
        private const val cashbackShortName = 0.07
        private const val cashbackCurrentMonth = 0.05
        private const val cashbackPreviousOrNextMonth = 0.03
        private const val cashbackStandard = 0.02

        private const val specialName = "Олег"
        private const val specialSurname = "Олегов"

        private fun checkCurrentMonth(name: String) : Boolean {
            val month = LocalDate.now().month.getDisplayName(TextStyle.FULL, Locale("ru"))
            return month.startsWith(name[0], true)
        }

        private fun checkPreviousOrNextMonth(name: String) : Boolean {
            val previousMonth = LocalDate.now().minusMonths(1).month.getDisplayName(TextStyle.FULL, Locale("ru"))
            val nextMonth = LocalDate.now().plusMonths(1).month.getDisplayName(TextStyle.FULL, Locale("ru"))
            return previousMonth.startsWith(name[0], true) || nextMonth.startsWith(name[0], true)
        }
    }

    override fun calculateCashback(transactionInfo: TransactionInfo): Double {
        return with(transactionInfo) {
            if (mccCode == MCC_BEER && firstName.equals(specialName, true) && lastName.equals(specialSurname, true)) {
                cashback(transactionSum, cashbackFullName)
            } else if (mccCode == MCC_BEER && firstName.equals(specialName, true)) {
                cashback(transactionSum, cashbackShortName)
            } else if (mccCode == MCC_BEER && checkCurrentMonth(firstName)) {
                cashback(transactionSum, cashbackCurrentMonth)
            } else if (mccCode == MCC_BEER && checkPreviousOrNextMonth(firstName)) {
                cashback(transactionSum, cashbackPreviousOrNextMonth)
            } else
                cashback(transactionSum, cashbackStandard)
        }
    }
}