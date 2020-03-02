package ru.tinkoff.fintech.service.cashback.utils

import ru.tinkoff.fintech.model.TransactionInfo
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import kotlin.math.round

class CashbackUtils {
    fun cashback(transactionSum: Double, cashbackPercent: Double) =
        round(transactionSum * cashbackPercent * 100) / 100

    fun cashbackLoyaltyProgramAll(transactionInfo: TransactionInfo) =
        with (transactionInfo) {
            round((lcm(firstName.length, lastName.length).toDouble() * transactionSum / 100000) * 100) / 100
        }

    fun checkCurrentMonth(name: String) : Boolean {
        val month = LocalDate.now().month.getDisplayName(TextStyle.FULL, Locale("ru"))
        return month.startsWith(name[0], true)
    }

    fun checkPreviousOrNextMonth(name: String) : Boolean {
        val previousMonth = LocalDate.now().minusMonths(1).month.getDisplayName(TextStyle.FULL, Locale("ru"))
        val nextMonth = LocalDate.now().plusMonths(1).month.getDisplayName(TextStyle.FULL, Locale("ru"))
        return previousMonth.startsWith(name[0], true) || nextMonth.startsWith(name[0], true)
    }

    fun isPalindromeIf(transactionSum: Double) : Boolean {
        val pennies = (transactionSum * 100).toInt().toString()
        val len = pennies.length
        var count = 0
        for (i in 0..len / 2)
            if (pennies[i] != pennies[len - i - 1]) count++
        return count <= 1
    }

    private fun gcd(a: Int, b: Int): Int {
        if (b == 0) return a
        return gcd(b, a % b)
    }

    private fun lcm(a: Int, b: Int): Int {
        return (a * b) / gcd(a, b)
    }
}