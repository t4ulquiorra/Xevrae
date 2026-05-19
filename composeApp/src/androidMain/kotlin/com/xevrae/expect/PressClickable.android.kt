package com.xevrae.expect

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

actual fun Modifier.pressClickable(
    enabled: Boolean = true,
    onClick: () -> Unit,
): Modifier = composed {
    val scale = remember { Animatable(1f) }
    this
        .graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
        }
        .pointerInput(enabled) {
            detectTapGestures(
                onPress = {
                    if (!enabled) return@detectTapGestures
                    scale.animateTo(0.94f, tween(80))
                    tryAwaitRelease()
                    scale.animateTo(1f, tween(80))
                    onClick()
                }
            )
        }
}
