package com.example.expensetracker.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.expensetracker.core.designsystem.AppDimensions
import com.example.expensetracker.core.designsystem.components.AppIcon
import com.example.expensetracker.core.designsystem.components.AppSurface

@Composable
fun NumericKeypad(
    onDigitClick: (String) -> Unit,
    onBackspaceClick: () -> Unit,
    onClearClick: () -> Unit,
    onDecimalClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keys = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf(".", "0", "C")
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
    ) {
        keys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppDimensions.paddingSmall)
            ) {
                row.forEach { key ->
                    KeypadButton(
                        text = key,
                        isAccent = key == "C",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            when (key) {
                                "C" -> onClearClick()
                                "." -> onDecimalClick()
                                else -> onDigitClick(key)
                            }
                        }
                    )
                }
            }
        }
        AppSurface(
            onClick = onBackspaceClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(AppDimensions.components.buttonSmallHeight),
            shape = RoundedCornerShape(AppDimensions.cornerMedium),
            color = MaterialTheme.colorScheme.surfaceContainerHigh
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                AppIcon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = "Backspace",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                )
            }
        }
    }
}
