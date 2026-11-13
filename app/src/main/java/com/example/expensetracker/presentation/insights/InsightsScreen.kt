package com.example.expensetracker.presentation.insights

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppIconButton
import com.example.expensetracker.core.designsystem.components.AppScaffold
import com.example.expensetracker.core.designsystem.components.AppTopAppBar
import com.example.expensetracker.presentation.insights.components.InsightCardItem

@Composable
fun InsightsScreen(
    viewModel: InsightsViewModel,
    onNavigateBack: () -> Unit
) {
    val insights by viewModel.insightsState.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    AppScaffold(
        topBar = {
            AppTopAppBar(
                title = { AppTypography.TitleLarge(text = stringResource(R.string.insights_title)) },
                navigationIcon = {
                    AppIconButton(onClick = onNavigateBack) {
                        AppIcon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = AppDimensions.paddingDefault)
        ) {
            AppTypography.Caption(
                text = stringResource(R.string.insights_subtitle),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = AppDimensions.paddingSmall)
            )

            if (isLandscape) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingDefault),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingDefault),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(insights, key = { it.id }) { insight ->
                        InsightCardItem(insight = insight)
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = AppDimensions.paddingSmall),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(insights, key = { it.id }) { insight ->
                        InsightCardItem(insight = insight)
                    }
                }
            }
        }
    }
}
