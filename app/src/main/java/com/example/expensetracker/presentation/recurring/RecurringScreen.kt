package com.example.expensetracker.presentation.recurring

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCircularProgressIndicator
import com.example.expensetracker.core.designsystem.components.AppAlertDialog
import com.example.expensetracker.core.designsystem.components.AppFloatingActionButton
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.components.AppScaffold
import com.example.expensetracker.core.designsystem.components.AppSnackbarHost
import com.example.expensetracker.core.designsystem.components.AppTopAppBar
import com.example.expensetracker.domain.model.RecurringTransaction
import com.example.expensetracker.presentation.components.EmptyStateView
import com.example.expensetracker.presentation.recurring.components.AddRecurringDialog
import com.example.expensetracker.presentation.recurring.components.RecurringCardItem

@Composable
fun RecurringScreen(
    viewModel: RecurringViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var pendingDelete by remember { mutableStateOf<RecurringTransaction?>(null) }
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
                title = { AppTypography.TitleLarge(text = stringResource(R.string.recurring_transactions_title)) },
                navigationIcon = {
                    AppIconButton(onClick = onNavigateBack) {
                        AppIcon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    AppIconButton(onClick = { viewModel.triggerProcessDueNow() }) {
                        AppIcon(imageVector = Icons.Default.PlayArrow, contentDescription = "Run Due Rules")
                    }
                }
            )
        },
        snackbarHost = { AppSnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            AppFloatingActionButton(onClick = { showAddDialog = true }) {
                AppIcon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.add_recurring_rule))
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
        } else if (state.recurringList.isEmpty()) {
            EmptyStateView(
                title = stringResource(R.string.no_recurring_title),
                description = stringResource(R.string.no_recurring_desc),
                actionText = stringResource(R.string.add_recurring_rule),
                onActionClick = { showAddDialog = true },
                modifier = Modifier.padding(padding)
            )
        } else if (isLandscape) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = AppDimensions.paddingDefault),
                contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingDefault),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingDefault)
            ) {
                items(state.recurringList, key = { it.id }) { item ->
                    RecurringCardItem(
                        item = item,
                        currencyCode = state.currencyCode,
                        onToggle = { viewModel.toggleEnable(item) },
                        onDelete = { pendingDelete = item }
                    )
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
                items(state.recurringList, key = { it.id }) { item ->
                    RecurringCardItem(
                        item = item,
                        currencyCode = state.currencyCode,
                        onToggle = { viewModel.toggleEnable(item) },
                        onDelete = { pendingDelete = item }
                    )
                }
                item { Spacer(modifier = Modifier.height(AppDimensions.components.fab * 1.4f)) }
            }
        }
    }

    if (showAddDialog) {
        AddRecurringDialog(
            categories = state.categories,
            accounts = state.accounts,
            onDismiss = { showAddDialog = false },
            onConfirm = { title, amount, type, freq, catId, accId ->
                viewModel.addRecurring(title, amount, type, freq, catId, accId)
                showAddDialog = false
            }
        )
    }

    pendingDelete?.let { item ->
        AppAlertDialog(
            onDismissRequest = { pendingDelete = null },
            title = stringResource(R.string.confirm_delete_recurring),
            text = { AppTypography.BodyMedium(text = stringResource(R.string.delete_recurring_warning, item.title)) },
            confirmButtonText = stringResource(R.string.action_delete),
            onConfirm = {
                viewModel.deleteRecurring(item)
                pendingDelete = null
            },
            dismissButtonText = stringResource(R.string.action_cancel),
            onDismiss = { pendingDelete = null },
            isDestructive = true
        )
    }
}
