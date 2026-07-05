package com.xevrae.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.layer.GraphicsLayer
import com.xevrae.expect.ui.PlatformBackdrop

@Composable
actual fun Modifier.liquidGlass(
    backdrop: PlatformBackdrop,
    layer: GraphicsLayer,
    luminanceAnimation: Float,
    shape: Shape,
    interactive: Boolean,
): Modifier = this.clip(shape)
