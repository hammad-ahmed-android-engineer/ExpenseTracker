package com.example.expensetracker.presentation.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppButton
import com.example.expensetracker.core.designsystem.components.AppCard

@Composable
fun RealisticSampleDataCard(
    onSeedSampleData: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(AppDimensions.paddingDefault),
            verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
        ) {
            AppTypography.TitleMedium(
                text = stringResource(R.string.realistic_sample_data),
                fontWeight = FontWeight.SemiBold
            )

            AppTypography.BodySmall(
                text = stringResource(R.string.sample_data_module_desc),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))

            AppButton(
                onClick = onSeedSampleData,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(AppDimensions.cornerLarge),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                AppTypography.ButtonText(
                    text = stringResource(R.string.seed_sample_data),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

