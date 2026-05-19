package com.xevrae.expect

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier

actual fun Modifier.pressClickable(
    enabled: Boolean = true,
    onClick: () -> Unit,
): Modifier = this.clickable(enabled = enabled, onClick = onClick)
