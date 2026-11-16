package com.example.expensetracker.core.utils

import android.os.SystemClock
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

@Composable
fun rememberDebouncedClick(
    debounceTimeMillis: Long = 500L,
    onClick: () -> Unit
): () -> Unit {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    return remember(debounceTimeMillis, onClick) {
        {
            val now = SystemClock.elapsedRealtime()
            if (now - lastClickTime >= debounceTimeMillis) {
                lastClickTime = now
                onClick()
            }
        }
    }
}

@Composable
fun <T> rememberDebouncedCallback(
    debounceTimeMillis: Long = 500L,
    onAction: (T) -> Unit
): (T) -> Unit {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    return remember(debounceTimeMillis, onAction) {
        { value ->
            val now = SystemClock.elapsedRealtime()
            if (now - lastClickTime >= debounceTimeMillis) {
                lastClickTime = now
                onAction(value)
            }
        }
    }
}

fun Modifier.clickableDebounced(
    debounceTimeMillis: Long = 500L,
    enabled: Boolean = true,
    indication: Indication? = null,
    onClick: () -> Unit
): Modifier = composed {
    val debouncedClick = rememberDebouncedClick(
        debounceTimeMillis = debounceTimeMillis,
        onClick = onClick
    )
    if (indication != null) {
        val interactionSource = remember { MutableInteractionSource() }
        this.clickable(
            interactionSource = interactionSource,
            indication = indication,
            enabled = enabled,
            onClick = debouncedClick
        )
    } else {
        this.clickable(
            enabled = enabled,
            onClick = debouncedClick
        )
    }
}
