package com.turkcell.lyraapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.turkcell.lyraapp.ui.login.LoginScreen
import com.turkcell.lyraapp.ui.main.MainScaffold
import com.turkcell.lyraapp.ui.register.RegisterScreen
import kotlinx.serialization.Serializable

// ── Rota Tanımları ─────────────────────────────────────────────────────────
// Her rota, type-safe navigasyon icin @Serializable ile isaretidir.
// BNB sekme rotaları (Home, Search, Library, Favorites, Profile) MainScaffold
// icindeki nested NavController tarafindan yonetilir.
sealed interface LyraRoute {
    // Auth rotaları — üst NavController'a ait
    @Serializable data object Login    : LyraRoute
    @Serializable data object Register : LyraRoute

    // Ana (Main) rota — üst NavController'a ait; içinde BNB barındırır
    @Serializable data object Main     : LyraRoute

    // BNB sekme rotaları — MainScaffold icindeki nested NavController'a ait
    @Serializable data object Home      : LyraRoute
    @Serializable data object Search    : LyraRoute
    @Serializable data object Library   : LyraRoute
    @Serializable data object Favorites : LyraRoute
    @Serializable data object Profile   : LyraRoute
}

// ── NavGraph ───────────────────────────────────────────────────────────────
// Uygulamanin ust duzey navigasyon grafigini tanimlar.
// Auth akisi bu grafik uzerinden yonetilir; giris sonrasi MainScaffold'a gecilir.
// BNB sekme navigasyonu MainScaffold icindeki nested NavController'da gerceklesir.
@Composable
fun LyraNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = LyraRoute.Login,
        modifier = modifier,
    ) {
        composable<LyraRoute.Login> {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(LyraRoute.Main) {
                        popUpTo(LyraRoute.Login) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(LyraRoute.Register)
                },
                onNavigateToForgotPassword = {
                    // Ilerleyen sprintte eklenecektir.
                },
            )
        }

        composable<LyraRoute.Register> {
            RegisterScreen(
                onNavigateToHome = {
                    navController.navigate(LyraRoute.Main) {
                        popUpTo(LyraRoute.Login) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                },
            )
        }

        // MainScaffold: BNB ve tum ana ekranlari barindiran iskelet
        composable<LyraRoute.Main> {
            MainScaffold()
        }
    }
}
