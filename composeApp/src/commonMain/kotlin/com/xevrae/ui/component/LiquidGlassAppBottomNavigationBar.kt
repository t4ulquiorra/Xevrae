package com.xevrae.ui.component

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.size
import org.jetbrains.compose.resources.painterResource
import androidx.navigation.NavController
import com.xevrae.expect.ui.PlatformBackdrop
import com.xevrae.ui.navigation.destination.home.HomeDestination
import com.xevrae.ui.navigation.destination.library.LibraryDestination
import com.xevrae.ui.navigation.destination.search.SearchDestination
import com.xevrae.viewModel.SharedViewModel
import org.jetbrains.compose.resources.StringResource
import xevrae.composeapp.generated.resources.Res
import xevrae.composeapp.generated.resources.home
import xevrae.composeapp.generated.resources.library
import xevrae.composeapp.generated.resources.search
import xevrae.composeapp.generated.resources.home_filled
import xevrae.composeapp.generated.resources.home_lined
import xevrae.composeapp.generated.resources.search_filled
import xevrae.composeapp.generated.resources.search_lined
import xevrae.composeapp.generated.resources.library_filled
import xevrae.composeapp.generated.resources.library_lined
import kotlin.reflect.KClass

@Composable
expect fun LiquidGlassAppBottomNavigationBar(
    startDestination: Any = HomeDestination,
    navController: NavController,
    backdrop: PlatformBackdrop,
    viewModel: SharedViewModel,
    isScrolledToTop: Boolean = false,
    onOpenNowPlaying: () -> Unit = {},
    reloadDestinationIfNeeded: (KClass<*>) -> Unit = { _ -> },
)

sealed class BottomNavScreen(
    val ordinal: Int,
    val destination: Any,
    val title: StringResource,
    val icon: @Composable (selected: Boolean) -> Unit,
) {
    data object Home : BottomNavScreen(
        ordinal = 0,
        destination = HomeDestination,
        title = Res.string.home,
        icon = { selected ->
            Icon(
                painter = painterResource(if (selected) Res.drawable.home_filled else Res.drawable.home_lined),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        },
    )

    data object Search : BottomNavScreen(
        ordinal = 1,
        destination = SearchDestination,
        title = Res.string.search,
        icon = { selected ->
            Icon(
                painter = painterResource(if (selected) Res.drawable.search_filled else Res.drawable.search_lined),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        },
    )

    data object Library : BottomNavScreen(
        ordinal = 2,
        destination = LibraryDestination,
        title = Res.string.library,
        icon = { selected ->
            Icon(
                painter = painterResource(if (selected) Res.drawable.library_filled else Res.drawable.library_lined),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        },
    )
}