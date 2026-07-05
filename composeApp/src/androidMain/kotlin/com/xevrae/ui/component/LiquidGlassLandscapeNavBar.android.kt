package com.xevrae.ui.component

import android.graphics.Bitmap
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.Modifier
import androidx.core.graphics.scale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.xevrae.expect.ui.PlatformBackdrop
import com.xevrae.logger.Logger
import com.xevrae.viewModel.SharedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.nio.IntBuffer
import kotlin.reflect.KClass
import kotlin.time.Duration.Companion.seconds

private const val TAG = "LiquidGlassLandscapeNavBar"

@Composable
actual fun LiquidGlassLandscapeNavBar(
    navController: NavController,
    backdrop: PlatformBackdrop,
    viewModel: SharedViewModel,
    reloadDestinationIfNeeded: (KClass<*>) -> Unit
) {
    val layer = rememberGraphicsLayer()
    val luminanceAnimation = remember { Animatable(0f) }

    LaunchedEffect(layer) {
        val buffer = IntBuffer.allocate(25)
        while (isActive) {
            try {
                withContext(Dispatchers.IO) {
                    val imageBitmap = layer.toImageBitmap()
                    val thumbnail =
                        imageBitmap
                            .asAndroidBitmap()
                            .scale(5, 5, false)
                            .copy(Bitmap.Config.ARGB_8888, false)
                    buffer.rewind()
                    thumbnail.copyPixelsToBuffer(buffer)
                }
            } catch (e: Exception) {
                Logger.e(TAG, "Error getting pixels from layer: ${e.localizedMessage}")
            }
            val averageLuminance =
                (0 until 25).sumOf { index ->
                    val color = buffer.get(index)
                    val r = (color shr 16 and 0xFF) / 255f
                    val g = (color shr 8 and 0xFF) / 255f
                    val b = (color and 0xFF) / 255f
                    0.2126 * r + 0.7152 * g + 0.0722 * b
                } / 25
            luminanceAnimation.animateTo(
                averageLuminance.coerceIn(0.3, 0.8).toFloat(),
                tween(500),
            )
            delay(1.seconds)
        }
    }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val bottomNavScreens = listOf(BottomNavScreen.Home, BottomNavScreen.Search, BottomNavScreen.Library)

    // FIX: Use official hierarchy check to find the current selected tab
    val selectedIndex = remember(currentBackStackEntry) {
        bottomNavScreens.indexOfFirst { screen ->
            currentBackStackEntry?.destination?.hierarchy?.any { it.hasRoute(screen.destination::class) } == true
        }.let { if (it == -1) 0 else it }
    }

    fun selectTab(index: Int) {
        val screen = bottomNavScreens[index]
        if (selectedIndex == index) {
            reloadDestinationIfNeeded(screen.destination::class)
        } else {
            navController.navigate(screen.destination) {
                popUpTo(navController.graph.startDestinationId) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    LiquidGlassTabBar(
        modifier = Modifier.fillMaxWidth(), // FIX: Make the tab bar stretch to fill the screen
        tabs = bottomNavScreens,
        selectedTab = selectedIndex,
        backdrop = backdrop,
        layer = layer,
        luminance = luminanceAnimation.value,
        onTabSelected = { position -> selectTab(position) }
    )
}
