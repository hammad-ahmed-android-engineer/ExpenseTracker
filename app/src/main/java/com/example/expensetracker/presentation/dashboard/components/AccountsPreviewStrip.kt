package com.example.expensetracker.presentation.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.expensetracker.R
import com.example.expensetracker.core.common.formatCurrency
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.AppTypography
import com.example.expensetracker.core.designsystem.components.AppCard
import com.example.expensetracker.core.designsystem.components.AppTextButton
import com.example.expensetracker.domain.model.Account
import com.example.expensetracker.presentation.components.CategoryIcon

@Composable
fun AccountsPreviewStrip(
    accounts: List<Account>,
    onAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppTypography.TitleMedium(text = stringResource(R.string.your_accounts))
            AppTextButton(onClick = onAccountClick) {
                AppTypography.ButtonText(text = stringResource(R.string.see_all))
            }
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingMedium)
        ) {
            items(accounts, key = { it.id }) { account ->
                AppCard(
                    onClick = onAccountClick,
                    shape = RoundedCornerShape(AppDimensions.cornerLarge),
                    modifier = Modifier.widthIn(min = AppDimensions.components.emptyStateIconContainer * 2.5f)
                ) {
                    Column(modifier = Modifier.padding(AppDimensions.paddingDefault)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CategoryIcon(
                                iconName = account.iconName,
                                colorHex = account.colorHex,
                                size = AppDimensions.icons.extraLarge,
                                iconSize = AppDimensions.icons.small
                            )
                            Spacer(modifier = Modifier.widthIn(min = AppDimensions.paddingSmall))
                            Column {
                                AppTypography.LabelLarge(
                                    text = account.name,
                                    maxLines = 1,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                AppTypography.Caption(text = account.type.displayName)
                            }
                        }
                        Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))
                        AppTypography.TitleMedium(
                            text = account.currentBalance.formatCurrency(account.currency),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

