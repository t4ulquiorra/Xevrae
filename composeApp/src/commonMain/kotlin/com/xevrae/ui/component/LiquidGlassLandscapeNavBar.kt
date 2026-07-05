package com.xevrae.ui.component

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.xevrae.expect.ui.PlatformBackdrop
import com.xevrae.viewModel.SharedViewModel
import kotlin.reflect.KClass

@Composable
expect fun LiquidGlassLandscapeNavBar(
    navController: NavController,
    backdrop: PlatformBackdrop,
    viewModel: SharedViewModel,
    reloadDestinationIfNeeded: (KClass<*>) -> Unit
)
