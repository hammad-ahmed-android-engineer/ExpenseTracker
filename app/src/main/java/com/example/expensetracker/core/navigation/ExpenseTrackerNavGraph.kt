package com.example.expensetracker.core.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppNavigationBar
import com.example.expensetracker.core.designsystem.components.AppNavigationBarItem
import com.example.expensetracker.core.designsystem.components.AppNavigationRail
import com.example.expensetracker.core.designsystem.components.AppNavigationRailItem
import com.example.expensetracker.presentation.accounts.AccountViewModel
import com.example.expensetracker.presentation.accounts.AccountsScreen
import com.example.expensetracker.presentation.budgets.BudgetViewModel
import com.example.expensetracker.presentation.budgets.BudgetsScreen
import com.example.expensetracker.presentation.calendar.CalendarScreen
import com.example.expensetracker.presentation.calendar.CalendarViewModel
import com.example.expensetracker.presentation.categories.CategoriesScreen
import com.example.expensetracker.presentation.categories.CategoryViewModel
import com.example.expensetracker.presentation.dashboard.DashboardScreen
import com.example.expensetracker.presentation.dashboard.DashboardViewModel
import com.example.expensetracker.presentation.insights.InsightsScreen
import com.example.expensetracker.presentation.insights.InsightsViewModel
import com.example.expensetracker.presentation.recurring.RecurringScreen
import com.example.expensetracker.presentation.recurring.RecurringViewModel
import com.example.expensetracker.presentation.reports.ReportsScreen
import com.example.expensetracker.presentation.reports.ReportsViewModel
import com.example.expensetracker.presentation.savings.SavingsGoalsScreen
import com.example.expensetracker.presentation.savings.SavingsViewModel
import com.example.expensetracker.presentation.settings.SettingsScreen
import com.example.expensetracker.presentation.settings.SettingsViewModel
import com.example.expensetracker.presentation.transactions.AddEditTransactionScreen
import com.example.expensetracker.presentation.transactions.AddEditTransactionViewModel
import com.example.expensetracker.presentation.transactions.TransactionViewModel
import com.example.expensetracker.presentation.transactions.TransactionsScreen

@Composable
fun MainAppNavigation(
    navController: NavHostController = rememberNavController()
) {
    val bottomNavItems = listOf(
        Screen.Home,
        Screen.Transactions,
        Screen.Reports,
        Screen.Budgets,
        Screen.Settings
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showTopLevelNav = bottomNavItems.any { it.route == currentRoute } || currentRoute == Screen.Accounts.route
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val navigateTopLevel: (Screen) -> Unit = { screen ->
        navController.navigate(screen.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    if (isLandscape && showTopLevelNav) {
        Row(modifier = Modifier.fillMaxSize()) {
            AppNavigationRail(modifier = Modifier.fillMaxHeight()) {
                bottomNavItems.forEach { screen ->
                    AppNavigationRailItem(
                        selected = currentRoute == screen.route,
                        onClick = { navigateTopLevel(screen) },
                        icon = { AppIcon(imageVector = screen.icon, contentDescription = screen.title) },
                        label = { AppTypography.LabelSmall(text = screen.title, maxLines = 1) }
                    )
                }
            }
            Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                ExpenseTrackerNavHost(
                    navController = navController,
                    navigateTopLevel = navigateTopLevel
                )
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                ExpenseTrackerNavHost(
                    navController = navController,
                    navigateTopLevel = navigateTopLevel
                )
            }
            if (showTopLevelNav) {
                AppNavigationBar {
                    bottomNavItems.forEach { screen ->
                        AppNavigationBarItem(
                            selected = currentRoute == screen.route,
                            onClick = { navigateTopLevel(screen) },
                            icon = { AppIcon(imageVector = screen.icon, contentDescription = screen.title) },
                            label = { AppTypography.LabelSmall(text = screen.title, maxLines = 1) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpenseTrackerNavHost(
    navController: NavHostController,
    navigateTopLevel: (Screen) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(Screen.Home.route) {
            val viewModel: DashboardViewModel = hiltViewModel()
            DashboardScreen(
                viewModel = viewModel,
                onNavigateToAddTransaction = { navController.navigate(Screen.AddEditTransaction.createRoute(0L)) },
                onNavigateToEditTransaction = { id -> navController.navigate(Screen.AddEditTransaction.createRoute(id)) },
                onNavigateToTransactions = { navigateTopLevel(Screen.Transactions) },
                onNavigateToReports = { navigateTopLevel(Screen.Reports) },
                onNavigateToBudgets = { navigateTopLevel(Screen.Budgets) },
                onNavigateToAccounts = { navController.navigate(Screen.Accounts.route) },
                onNavigateToCategories = { navController.navigate(Screen.Categories.route) },
                onNavigateToSavings = { navController.navigate(Screen.Savings.route) },
                onNavigateToRecurring = { navController.navigate(Screen.Recurring.route) },
                onNavigateToCalendar = { navController.navigate(Screen.Calendar.route) },
                onNavigateToInsights = { navController.navigate(Screen.Insights.route) }
            )
        }
        composable(Screen.Transactions.route) {
            val viewModel: TransactionViewModel = hiltViewModel()
            TransactionsScreen(
                viewModel = viewModel,
                onNavigateToAddTransaction = { navController.navigate(Screen.AddEditTransaction.createRoute(0L)) },
                onNavigateToEditTransaction = { id -> navController.navigate(Screen.AddEditTransaction.createRoute(id)) },
                onNavigateToCalendar = { navController.navigate(Screen.Calendar.route) },
                onNavigateToCategories = { navController.navigate(Screen.Categories.route) }
            )
        }
        composable(
            route = Screen.AddEditTransaction.route,
            arguments = listOf(navArgument("transactionId") { type = NavType.LongType; defaultValue = 0L })
        ) {
            val viewModel: AddEditTransactionViewModel = hiltViewModel()
            AddEditTransactionScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Reports.route) {
            val viewModel: ReportsViewModel = hiltViewModel()
            ReportsScreen(
                viewModel = viewModel,
                onNavigateToInsights = { navController.navigate(Screen.Insights.route) },
                onNavigateToCalendar = { navController.navigate(Screen.Calendar.route) }
            )
        }
        composable(Screen.Budgets.route) {
            val viewModel: BudgetViewModel = hiltViewModel()
            BudgetsScreen(
                viewModel = viewModel,
                onNavigateToSavings = { navController.navigate(Screen.Savings.route) },
                onNavigateToRecurring = { navController.navigate(Screen.Recurring.route) }
            )
        }
        composable(Screen.Accounts.route) {
            val viewModel: AccountViewModel = hiltViewModel()
            AccountsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Categories.route) {
            val viewModel: CategoryViewModel = hiltViewModel()
            CategoriesScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Savings.route) {
            val viewModel: SavingsViewModel = hiltViewModel()
            SavingsGoalsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Recurring.route) {
            val viewModel: RecurringViewModel = hiltViewModel()
            RecurringScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Calendar.route) {
            val viewModel: CalendarViewModel = hiltViewModel()
            CalendarScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Insights.route) {
            val viewModel: InsightsViewModel = hiltViewModel()
            InsightsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.route) {
            val viewModel: SettingsViewModel = hiltViewModel()
            SettingsScreen(viewModel = viewModel)
        }
    }
}
