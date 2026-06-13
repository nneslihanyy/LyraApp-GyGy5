package com.turkcell.lyraapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.turkcell.lyraapp.ui.home.HomeScreen
import com.turkcell.lyraapp.ui.login.LoginScreen
import com.turkcell.lyraapp.ui.register.RegisterScreen
import kotlinx.serialization.Serializable

// ── Rota Tanımları ─────────────────────────────────────────────────────────
// Her rota, type-safe navigasyon icin @Serializable ile isaretidir.
sealed interface LyraRoute {
    @Serializable data object Login    : LyraRoute
    @Serializable data object Register : LyraRoute
    @Serializable data object Home     : LyraRoute
}

// ── NavGraph ───────────────────────────────────────────────────────────────
// Uygulamanin tum navigasyon grafigini tanimlar.
// Ekranlar navigasyon kutuphanesine dogrudan bagimli degildir;
// navigasyon eylemleri lambda olarak enjekte edilir.
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
                    navController.navigate(LyraRoute.Home) {
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
                    navController.navigate(LyraRoute.Home) {
                        popUpTo(LyraRoute.Login) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                },
            )
        }

        composable<LyraRoute.Home> {
            HomeScreen()
        }
    }
}
