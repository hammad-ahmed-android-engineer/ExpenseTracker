package com.example.expensetracker.presentation.transactions.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.expensetracker.R
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard

@Composable
fun TransactionAmountBannerCard(
    amountString: String,
    currencyCode: String,
    compact: Boolean = false,
    modifier: Modifier = Modifier
) {
    AppCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppDimensions.cornerExtraLarge),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        border = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(if (compact) AppDimensions.paddingDefault else AppDimensions.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppTypography.LabelSmall(
                text = stringResource(R.string.amount),
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(AppDimensions.paddingMicro))
            if (compact) {
                AppTypography.DisplayMedium(
                    text = "$amountString $currencyCode",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            } else {
                AppTypography.DisplayLarge(
                    text = "$amountString $currencyCode",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

