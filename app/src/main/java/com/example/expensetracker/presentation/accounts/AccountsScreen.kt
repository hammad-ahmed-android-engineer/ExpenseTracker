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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.common.formatCurrency
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppCircularProgressIndicator
import com.example.expensetracker.core.designsystem.components.AppFloatingActionButton
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.components.AppOutlinedButton
import com.example.expensetracker.core.designsystem.components.AppScaffold
import com.example.expensetracker.core.designsystem.components.AppSnackbarHost
import com.example.expensetracker.core.designsystem.components.AppTopAppBar
import com.example.expensetracker.presentation.accounts.components.AccountCardItem
import com.example.expensetracker.presentation.accounts.components.AddAccountDialog
import com.example.expensetracker.presentation.accounts.components.TransferMoneyDialog

@Composable
fun AccountsScreen(
    viewModel: AccountViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var showTransferDialog by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessage()
        }
    }

    AppScaffold(
        topBar = {
            AppTopAppBar(
                title = { AppTypography.TitleLarge(text = stringResource(R.string.accounts_title)) },
                navigationIcon = {
                    AppIconButton(onClick = onNavigateBack) {
                        AppIcon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    AppIconButton(
                        onClick = { showTransferDialog = true },
                        enabled = state.accounts.count { !it.isArchived } >= 2
                    ) {
                        AppIcon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = stringResource(R.string.action_transfer)
                        )
                    }
                }
            )
        },
        snackbarHost = { AppSnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            AppFloatingActionButton(onClick = { showAddDialog = true }) {
                AppIcon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_new_account)
                )
            }
        }
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
                Column(
                    modifier = Modifier
                        .weight(0.38f)
                        .padding(vertical = AppDimensions.paddingSmall),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                ) {
                    AppCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(AppDimensions.cornerExtraLarge),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        border = null
                    ) {
                        Column(modifier = Modifier.padding(AppDimensions.paddingLarge)) {
                            AppTypography.LabelSmall(
                                text = stringResource(R.string.total_combined_balance),
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                            AppTypography.DisplayMedium(
                                text = state.totalBalance.formatCurrency(state.currencyCode),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    AppOutlinedButton(
                        onClick = { showTransferDialog = true },
                        enabled = state.accounts.count { !it.isArchived } >= 2,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AppTypography.ButtonText(text = stringResource(R.string.transfer_money_title))
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(0.62f)
                        .fillMaxHeight()
                        .padding(vertical = AppDimensions.paddingSmall)
                ) {
                    AppTypography.TitleMedium(text = "${stringResource(R.string.your_accounts)} (${state.accounts.size})")
                    Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium),
                        verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
                    ) {
                        items(state.accounts, key = { it.id }) { acc ->
                            AccountCardItem(
                                acc = acc,
                                onToggleArchive = { viewModel.archiveAccount(acc.id, !acc.isArchived) }
                            )
                        }
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
                item {
                    AppCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(AppDimensions.cornerExtraLarge),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        border = null
                    ) {
                        Column(modifier = Modifier.padding(AppDimensions.paddingLarge)) {
                            AppTypography.LabelSmall(
                                text = stringResource(R.string.total_combined_balance),
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
                            AppTypography.DisplayMedium(
                                text = state.totalBalance.formatCurrency(state.currencyCode),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppTypography.TitleMedium(text = "${stringResource(R.string.your_accounts)} (${state.accounts.size})")
                        AppOutlinedButton(
                            onClick = { showTransferDialog = true },
                            enabled = state.accounts.count { !it.isArchived } >= 2
                        ) {
                            AppTypography.ButtonText(text = stringResource(R.string.action_transfer))
                        }
                    }
                }

                items(state.accounts, key = { it.id }) { acc ->
                    AccountCardItem(
                        acc = acc,
                        onToggleArchive = { viewModel.archiveAccount(acc.id, !acc.isArchived) }
                    )
                }

                item { Spacer(modifier = Modifier.height(AppDimensions.components.fab * 1.4f)) }
            }
        }
    }

    if (showAddDialog) {
        AddAccountDialog(
            defaultCurrency = state.currencyCode,
            onDismiss = { showAddDialog = false },
            onConfirm = { name, type, balance, currency ->
                viewModel.addOrUpdateAccount(
                    name = name,
                    type = type,
                    initialBalanceString = balance,
                    currency = currency
                )
                showAddDialog = false
            }
        )
    }

    if (showTransferDialog) {
        TransferMoneyDialog(
            accounts = state.accounts,
            onDismiss = { showTransferDialog = false },
            onConfirm = { sourceId, destId, amount, note ->
                viewModel.transferMoney(sourceId, destId, amount, note)
                showTransferDialog = false
            }
        )
    }
}
