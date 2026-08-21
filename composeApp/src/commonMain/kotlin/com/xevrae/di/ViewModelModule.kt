package com.xevrae.di

import com.xevrae.viewModel.AlbumViewModel
import com.xevrae.viewModel.AnalyticsViewModel
import com.xevrae.viewModel.ArtistViewModel
import com.xevrae.viewModel.HomeViewModel
import com.xevrae.viewModel.LibraryDynamicPlaylistViewModel
import com.xevrae.viewModel.LibraryViewModel
import com.xevrae.viewModel.LocalPlaylistViewModel
import com.xevrae.viewModel.LogInViewModel
import com.xevrae.viewModel.MoodViewModel
import com.xevrae.viewModel.MoreAlbumsViewModel
import com.xevrae.viewModel.NotificationViewModel
import com.xevrae.viewModel.NowPlayingBottomSheetViewModel
import com.xevrae.viewModel.PlaylistViewModel
import com.xevrae.viewModel.PodcastViewModel
import com.xevrae.viewModel.RecentlySongsViewModel
import com.xevrae.viewModel.SearchViewModel
import com.xevrae.viewModel.SettingsViewModel
import com.xevrae.viewModel.SharedViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        single {
            SharedViewModel(
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
            )
        }
        single {
            SearchViewModel(
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            NowPlayingBottomSheetViewModel(
                get(),
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            LibraryViewModel(
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            LibraryDynamicPlaylistViewModel(
                get(),
                get(),
            )
        }
        viewModel {
            AlbumViewModel(
                get(),
                get(),
            )
        }
        viewModel {
            HomeViewModel(
                get(),
                get(),
            )
        }
        viewModel {
            SettingsViewModel(
                get(),
                get(),
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            ArtistViewModel(
                get(),
                get(),
            )
        }
        viewModel {
            PlaylistViewModel(
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            LogInViewModel(
                get(),
            )
        }
        viewModel {
            PodcastViewModel(
                get(),
            )
        }
        viewModel {
            MoreAlbumsViewModel(
                get(),
            )
        }
        viewModel {
            RecentlySongsViewModel(
                get(),
            )
        }
        viewModel {
            LocalPlaylistViewModel(
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            NotificationViewModel(
                get(),
            )
        }
        viewModel {
            MoodViewModel(
                get(),
                get(),
            )
        }
        viewModel {
            AnalyticsViewModel(
                get(),
                get(),
                get(),
                get(),
                get(),
            )
        }
    }