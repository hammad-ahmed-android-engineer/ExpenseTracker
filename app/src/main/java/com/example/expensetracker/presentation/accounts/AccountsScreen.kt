package com.example.expensetracker.presentation.accounts

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppButton
import com.example.expensetracker.core.designsystem.components.AppCircularProgressIndicator
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.components.AppScaffold
import com.example.expensetracker.core.designsystem.components.AppSnackbarHost
import com.example.expensetracker.core.designsystem.components.AppTopAppBar
import com.example.expensetracker.domain.model.Account
import com.example.expensetracker.domain.model.AccountType
import com.example.expensetracker.presentation.accounts.components.AccountCardItem
import com.example.expensetracker.presentation.accounts.components.AddAccountDialog
import com.example.expensetracker.presentation.accounts.components.QuickTransferCard
import com.example.expensetracker.presentation.accounts.components.TransferMoneyDialog

@Composable
fun AccountsScreen(
    viewModel: AccountViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var showTransferDialog by rememberSaveable { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessage()
        }
    }

    val displayAccounts = if (state.accounts.isEmpty()) {
        listOf(
            Account(id = 1L, name = "Checking", type = AccountType.BANK_ACCOUNT, initialBalance = 284050L, currentBalance = 284050L, currency = "USD", colorHex = "#3B4371", iconName = "CreditCard"),
            Account(id = 2L, name = "Cash", type = AccountType.CASH, initialBalance = 254050L, currentBalance = 254050L, currency = "USD", colorHex = "#1E5E4E", iconName = "Payments"),
            Account(id = 3L, name = "Savings", type = AccountType.SAVINGS_ACCOUNT, initialBalance = 1020000L, currentBalance = 1020000L, currency = "USD", colorHex = "#1D6A5D", iconName = "AccountBalance"),
            Account(id = 4L, name = "Credit cards", type = AccountType.CREDIT_CARD, initialBalance = 1020000L, currentBalance = 1020000L, currency = "USD", colorHex = "#9E432A", iconName = "CreditCard")
        )
    } else {
        state.accounts
    }

    AppScaffold(
        topBar = {
            AppTopAppBar(
                title = {
                    AppTypography.TitleLarge(
                        text = stringResource(R.string.my_accounts),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    AppIconButton(onClick = onNavigateBack) {
                        AppIcon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { AppSnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                AppCircularProgressIndicator()
            }
        } else if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = AppDimensions.paddingDefault),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingDefault)
            ) {
                // Left Column: 2x2 Grid of Account Cards
                LazyColumn(
                    modifier = Modifier
                        .weight(1.1f)
                        .fillMaxHeight(),
                    contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                ) {
                    item(key = "accounts_heading_landscape") {
                        AppTypography.TitleLarge(
                            text = stringResource(R.string.my_accounts),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    val chunkedAccounts = displayAccounts.chunked(2)
                    chunkedAccounts.forEachIndexed { rowIndex, rowAccounts ->
                        item(key = "acc_row_land_$rowIndex") {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                            ) {
                                rowAccounts.forEach { acc ->
                                    Box(modifier = Modifier.weight(1f)) {
                                        AccountCardItem(acc = acc)
                                    }
                                }
                                if (rowAccounts.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                // Right Column: Quick Transfer Card
                LazyColumn(
                    modifier = Modifier
                        .weight(0.9f)
                        .fillMaxHeight(),
                    contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                ) {
                    item(key = "quick_transfer_card_landscape") {
                        QuickTransferCard(
                            accounts = displayAccounts,
                            onOpenFullTransfer = { _, _ -> showTransferDialog = true }
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = AppDimensions.paddingDefault),
                contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
            ) {
                // Header row: My Accounts + Add Account pill button
                item(key = "accounts_heading") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppTypography.HeadlineSmall(
                            text = stringResource(R.string.my_accounts),
                            fontWeight = FontWeight.Bold
                        )
                        AppButton(
                            onClick = { showAddDialog = true },
                            shape = RoundedCornerShape(AppDimensions.cornerPill),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFA5B4FC),
                                contentColor = Color(0xFF1E1B4B)
                            )
                        ) {
                            AppTypography.ButtonText(
                                text = stringResource(R.string.add_account_pill),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E1B4B)
                            )
                        }
                    }
                }

                // 2x2 Grid of Account Cards
                val chunkedAccounts = displayAccounts.chunked(2)
                chunkedAccounts.forEachIndexed { rowIndex, rowAccounts ->
                    item(key = "acc_row_$rowIndex") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                        ) {
                            rowAccounts.forEach { acc ->
                                Box(modifier = Modifier.weight(1f)) {
                                    AccountCardItem(acc = acc)
                                }
                            }
                            if (rowAccounts.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                item(key = "spacer_before_transfer") {
                    Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
                }

                // Quick Transfer Card
                item(key = "quick_transfer_card") {
                    QuickTransferCard(
                        accounts = displayAccounts,
                        onOpenFullTransfer = { _, _ -> showTransferDialog = true }
                    )
                }

                item { Spacer(modifier = Modifier.height(AppDimensions.paddingLarge)) }
            }
        }
    }

    if (showAddDialog) {
        AddAccountDialog(
            defaultCurrency = state.currencyCode,
            onDismiss = { showAddDialog = false },
            onConfirm = { name, type, balanceStr, curr ->
                viewModel.addOrUpdateAccount(
                    name = name,
                    type = type,
                    initialBalanceString = balanceStr,
                    currency = curr
                )
                showAddDialog = false
            }
        )
    }

    if (showTransferDialog) {
        TransferMoneyDialog(
            accounts = displayAccounts.filter { !it.isArchived },
            onDismiss = { showTransferDialog = false },
            onConfirm = { fromId, toId, amountStr, note ->
                viewModel.transferMoney(fromId, toId, amountStr, note)
                showTransferDialog = false
            }
        )
    }
}
