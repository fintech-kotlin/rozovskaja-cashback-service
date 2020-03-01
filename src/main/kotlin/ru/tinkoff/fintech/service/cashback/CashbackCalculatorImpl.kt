package ru.tinkoff.fintech.service.cashback

import ru.tinkoff.fintech.model.TransactionInfo
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import kotlin.math.round

internal const val LOYALTY_PROGRAM_BLACK = "BLACK"
internal const val LOYALTY_PROGRAM_ALL = "ALL"
internal const val LOYALTY_PROGRAM_BEER = "BEER"
internal const val MAX_CASH_BACK = 3000.0
internal const val MCC_SOFTWARE = 5734
internal const val MCC_BEER = 5921

class CashbackCalculatorImpl : CashbackCalculator {

    companion object {
        private val cashbackLoyaltyProgramBlack = 0.01
        private val cashbackMagicNumber = 6.66
        private val cashbackFullName = 0.1
        private val cashbackShortName = 0.07
        private val cashbackCurrentMonth = 0.05
        private val cashbackPreviousOrNextMonth = 0.03
        private val cashbackStandard = 0.02
        private val cashbackNothing = 0.0

        private val magicNumber = 666

        private val specialName = "Олег"
        private val specialSurname = "Олегов"
    }

    override fun calculateCashback(transactionInfo: TransactionInfo): Double {
        var res = with(transactionInfo) {
            when (loyaltyProgramName) {
                LOYALTY_PROGRAM_BLACK -> cashback(transactionSum, cashbackLoyaltyProgramBlack)
                LOYALTY_PROGRAM_BEER -> {
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
                LOYALTY_PROGRAM_ALL -> {
                    if (mccCode == MCC_SOFTWARE && isPalindromeIf(transactionSum)) {
                        round((lcm(firstName.length, lastName.length).toDouble() * transactionSum / 100000) * 100) / 100
                    }
                    else
                        cashbackNothing
                }
                else -> cashbackNothing
            }
        }
        with(transactionInfo) {
            if (transactionSum.compareTo(magicNumber.toDouble()) == 0 || (transactionSum % magicNumber.toDouble()).compareTo(0.0) == 0) {
                res += cashbackMagicNumber
            }
            return with(transactionInfo) {
                if (cashbackTotalValue >= MAX_CASH_BACK)
                    cashbackNothing
                if (res + cashbackTotalValue > MAX_CASH_BACK)
                    MAX_CASH_BACK - cashbackTotalValue
                else
                    res
            }
        }
    }

    fun cashback(transactionSum: Double, cashbackPercent: Double) : Double {
        return round(transactionSum * cashbackPercent * 100) / 100
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

    fun reverseNumber(number: Int) : Int {
        var a = 0
        var b = 0
        var n = number
        while (n > 0) {
            a = n % 10
            b = b * 10 + a
            n /= 10
        }
        return b
    }

    fun isPalindromeIf(transactionSum: Double) : Boolean {
        val pennies = (transactionSum * 100).toInt()
        var rev = reverseNumber(pennies)
        var n = pennies
        var flag = false
        while (n > 0) {
            if (n % 10 != rev % 10) {
                rev = (rev / 10) * (n % 10)
                flag = true
            }
            if (flag) break
        }
        if (flag) {
            n = rev
            if (reverseNumber(rev) == n) return true
            return false
        }
        return true
    }

    fun gcd(a: Int, b: Int): Int {
        if (b == 0) return a
        return gcd(b, a % b)
    }

    fun lcm(a: Int, b: Int): Int {
        return (a * b) / gcd(a, b)
    }
}