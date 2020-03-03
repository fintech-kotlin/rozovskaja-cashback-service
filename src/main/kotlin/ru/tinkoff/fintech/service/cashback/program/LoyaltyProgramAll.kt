package ru.tinkoff.fintech.service.cashback.program

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.CashbackCalculator
import ru.tinkoff.fintech.service.cashback.MCC_SOFTWARE
import kotlin.math.round

class LoyaltyProgramAll : CashbackCalculator {
    companion object {
        private const val cashbackNothing = 0.0

        private fun gcd(a: Int, b: Int): Int {
            if (b == 0) return a
            return gcd(b, a % b)
        }

        private fun lcm(a: Int, b: Int): Int {
            return (a * b) / gcd(a, b)
        }

        @JvmStatic fun cashbackLoyaltyProgramAll(transactionInfo: TransactionInfo) =
            with (transactionInfo) {
                round((lcm(firstName.length, lastName.length).toDouble() * transactionSum / 100000) * 100) / 100
            }

        @JvmStatic fun isPalindromeIf(transactionSum: Double) : Boolean {
            val pennies = (transactionSum * 100).toInt().toString()
            val len = pennies.length
            var count = 0
            for (i in 0..len / 2)
                if (pennies[i] != pennies[len - i - 1]) count++
            return count <= 1
        }
    }

    override fun calculateCashback(transactionInfo: TransactionInfo): Double {
        return with(transactionInfo) {
            if (mccCode == MCC_SOFTWARE && isPalindromeIf(transactionSum)) {
                cashbackLoyaltyProgramAll(transactionInfo)
            }
            else cashbackNothing
        }
    }
}