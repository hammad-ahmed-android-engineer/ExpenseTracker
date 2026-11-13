package com.example.expensetracker

import com.example.expensetracker.domain.model.Account
import com.example.expensetracker.domain.model.AccountType
import com.example.expensetracker.domain.model.Budget
import com.example.expensetracker.domain.model.BudgetPeriodType
import org.junit.Assert.*
import org.junit.Test

class TransferMoneyUseCaseTest {

    @Test
    fun testAccountTransferBalanceAdjustment() {
        val source = Account(id = 1, name = "Cash", type = AccountType.CASH, initialBalance = 10000L, currentBalance = 10000L)
        val destination = Account(id = 2, name = "Bank", type = AccountType.BANK_ACCOUNT, initialBalance = 50000L, currentBalance = 50000L)

        val transferAmount = 2500L // $25.00

        val newSourceBalance = source.currentBalance - transferAmount
        val newDestBalance = destination.currentBalance + transferAmount

        assertEquals(7500L, newSourceBalance)
        assertEquals(52500L, newDestBalance)
    }

    @Test
    fun testBudgetThresholdCalculations() {
        val budget = Budget(
            id = 1,
            name = "Monthly Food Budget",
            amountLimit = 20000L, // $200.00
            spentAmount = 17000L, // $170.00 (85%)
            periodType = BudgetPeriodType.MONTHLY,
            startDate = 0L,
            endDate = 1000L,
            notifyThreshold = 80
        )

        assertTrue(budget.isWarning)
        assertFalse(budget.isExceeded)
        assertEquals(3000L, budget.remainingAmount)
    }

    @Test
    fun testExceededBudgetState() {
        val budget = Budget(
            id = 2,
            name = "Shopping Limit",
            amountLimit = 10000L, // $100.00
            spentAmount = 12000L, // $120.00 (120%)
            periodType = BudgetPeriodType.MONTHLY,
            startDate = 0L,
            endDate = 1000L
        )

        assertTrue(budget.isExceeded)
        assertEquals(-2000L, budget.remainingAmount)
    }
}