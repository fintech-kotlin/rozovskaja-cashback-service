package ru.tinkoff.fintech.service.cashback

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.tinkoff.fintech.service.cashback.utils.CashbackUtils

class CashbackUtilsTest {

    private val observable = CashbackUtils()

    @Test
    fun `should be palindrome`() {
        val transactionSum = 12_222.20
        val actual = observable.isPalindromeIf(transactionSum)
        Assertions.assertEquals(true, actual)
    }

    @Test
    fun `should be palindrome 2`() {
        val transactionSum = 12_222.31
        val actual = observable.isPalindromeIf(transactionSum)
        Assertions.assertEquals(true, actual)
    }

    @Test
    fun `should be palindrome 3`() {
        val transactionSum = 12_265.21
        val actual = observable.isPalindromeIf(transactionSum)
        Assertions.assertEquals(true, actual)
    }

    @Test
    fun `should not be palindrome `() {
        val transactionSum = 12_224.31
        val actual = observable.isPalindromeIf(transactionSum)
        Assertions.assertEquals(false, actual)
    }
}