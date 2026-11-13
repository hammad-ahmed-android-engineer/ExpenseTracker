package com.example.expensetracker.presentation.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard

@Composable
fun AboutAppCard(modifier: Modifier = Modifier) {
    AppCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(AppDimensions.paddingDefault),
            verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingMicro)
        ) {
            AppTypography.TitleSmall(text = stringResource(R.string.about_application))
            AppTypography.BodySmall(
                text = stringResource(R.string.version_info),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            AppTypography.BodySmall(
                text = stringResource(R.string.arch_info),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            AppTypography.Caption(
                text = stringResource(R.string.offline_storage_info)
            )
        }
    }
}

