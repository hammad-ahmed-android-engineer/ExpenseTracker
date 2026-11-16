package com.example.expensetracker.presentation.accounts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.domain.model.Account

fun LazyListScope.accountsGridItems(
    accounts: List<Account>,
    keyPrefix: String
) {
    val chunkedAccounts = accounts.chunked(2)
    chunkedAccounts.forEachIndexed { rowIndex, rowAccounts ->
        item(key = "${keyPrefix}_$rowIndex") {
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

