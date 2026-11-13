package com.example.expensetracker.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("dashboard", "Home", Icons.Default.Home)
    object Transactions : Screen("transactions", "Transactions", Icons.Default.ReceiptLong)
    object Reports : Screen("reports", "Reports", Icons.Default.BarChart)
    object Budgets : Screen("budgets", "Budgets", Icons.Default.PieChart)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)

    // Additional sub-routes
    object AddEditTransaction : Screen("add_edit_transaction?transactionId={transactionId}", "Transaction", Icons.Default.Add) {
        fun createRoute(transactionId: Long = 0L) = "add_edit_transaction?transactionId=$transactionId"
    }
    object Accounts : Screen("accounts", "Accounts", Icons.Default.AccountBalance)
    object Categories : Screen("categories", "Categories", Icons.Default.Category)
    object Savings : Screen("savings", "Savings Goals", Icons.Default.Savings)
    object Recurring : Screen("recurring", "Recurring", Icons.Default.Repeat)
    object Calendar : Screen("calendar", "Calendar", Icons.Default.CalendarMonth)
    object Insights : Screen("insights", "Insights", Icons.Default.AutoAwesome)
}