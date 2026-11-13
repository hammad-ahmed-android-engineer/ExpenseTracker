package com.example.expensetracker.presentation.categories

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppFloatingActionButton
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.components.AppScaffold
import com.example.expensetracker.core.designsystem.components.AppSegmentedSelector
import com.example.expensetracker.core.designsystem.components.AppSnackbarHost
import com.example.expensetracker.core.designsystem.components.AppTopAppBar
import com.example.expensetracker.domain.model.Category
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.presentation.categories.components.AddCategoryDialog
import com.example.expensetracker.presentation.categories.components.CategoryDetailBottomSheet
import com.example.expensetracker.presentation.categories.components.CategoryGridCard
import com.example.expensetracker.presentation.components.ConfirmationDialog

@Composable
fun CategoriesScreen(
    viewModel: CategoryViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var pendingDeleteCategory by remember { mutableStateOf<Category?>(null) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val columns = if (isLandscape) 3 else 2
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { err ->
            snackbarHostState.showSnackbar(err)
            viewModel.clearError()
        }
    }

    val currentSelectedCategory = selectedCategory?.let { sel ->
        state.categories.find { it.id == sel.id } ?: sel
    }

    AppScaffold(
        topBar = {
            AppTopAppBar(
                title = { AppTypography.TitleLarge(text = stringResource(R.string.category_management)) },
                navigationIcon = {
                    AppIconButton(onClick = onNavigateBack) {
                        AppIcon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { AppSnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            AppFloatingActionButton(onClick = { showAddDialog = true }) {
                AppIcon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.add_category))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = AppDimensions.paddingDefault)
        ) {
            Spacer(modifier = Modifier.height(AppDimensions.paddingSmall))
            AppSegmentedSelector(
                options = listOf(TransactionType.EXPENSE, TransactionType.INCOME),
                selectedOption = state.selectedType,
                onOptionSelected = { viewModel.selectType(it) },
                labelProvider = {
                    if (it == TransactionType.EXPENSE) {
                        "Expense"
                    } else {
                        "Income"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))

            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium),
                contentPadding = PaddingValues(bottom = AppDimensions.components.fab * 1.4f),
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.categories, key = { it.id }) { cat ->
                    CategoryGridCard(
                        category = cat,
                        onClick = { selectedCategory = cat },
                        onDelete = { pendingDeleteCategory = cat }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddCategoryDialog(
            type = state.selectedType,
            onDismiss = { showAddDialog = false },
            onConfirm = { name, icon, color ->
                viewModel.addCategory(name, icon, color)
                showAddDialog = false
            }
        )
    }

    currentSelectedCategory?.let { cat ->
        CategoryDetailBottomSheet(
            category = cat,
            onAddSubcategory = { subName -> viewModel.addSubcategory(cat.id, subName) },
            onDeleteSubcategory = { sub -> viewModel.deleteSubcategory(sub) },
            onDismiss = { selectedCategory = null }
        )
    }

    pendingDeleteCategory?.let { cat ->
        ConfirmationDialog(
            title = "Delete Category",
            message = "Delete custom category \"${cat.name}\"?",
            onConfirm = {
                viewModel.deleteCategory(cat)
                pendingDeleteCategory = null
            },
            onDismiss = { pendingDeleteCategory = null }
        )
    }
}
