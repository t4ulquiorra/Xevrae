package com.xevrae.expect

import androidx.compose.ui.Modifier

expect fun Modifier.pressClickable(
    enabled: Boolean = true,
    onClick: () -> Unit,
): Modifier

expect fun Modifier.lightPressClickable(
    enabled: Boolean = true,
    onClick: () -> Unit,
): Modifier
