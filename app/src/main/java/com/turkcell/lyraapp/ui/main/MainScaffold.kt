package com.turkcell.lyraapp.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.turkcell.lyraapp.navigation.LyraRoute
import com.turkcell.lyraapp.ui.favorites.FavoritesScreen
import com.turkcell.lyraapp.ui.home.HomeScreen
import com.turkcell.lyraapp.ui.library.LibraryScreen
import com.turkcell.lyraapp.ui.profile.ProfileScreen
import com.turkcell.lyraapp.ui.search.SearchScreen

// ── MainScaffold ───────────────────────────────────────────────────────────
// Giris sonrasi gorunen ana iskelet. Scaffold icine yerlesik bir NavHost ile
// tum BNB sekmelerini barindirır. BNB her sekmede sabit olarak gorünür.
// Auth ekranlari (Login, Register) bu composable'in disinda kalir.
@Composable
fun MainScaffold(modifier: Modifier = Modifier) {
    val mainNavController = rememberNavController()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            LyraBnb(navController = mainNavController)
        },
    ) { innerPadding ->
        NavHost(
            navController = mainNavController,
            startDestination = LyraRoute.Home,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<LyraRoute.Home> {
                HomeScreen(modifier = Modifier.fillMaxSize())
            }
            composable<LyraRoute.Search> {
                SearchScreen(modifier = Modifier.fillMaxSize())
            }
            composable<LyraRoute.Library> {
                LibraryScreen(modifier = Modifier.fillMaxSize())
            }
            composable<LyraRoute.Favorites> {
                FavoritesScreen(modifier = Modifier.fillMaxSize())
            }
            composable<LyraRoute.Profile> {
                ProfileScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
