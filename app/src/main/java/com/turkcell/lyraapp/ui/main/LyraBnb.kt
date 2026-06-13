package com.turkcell.lyraapp.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.turkcell.lyraapp.navigation.LyraRoute
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.runtime.getValue

// ── BNB Sekme Modeli ───────────────────────────────────────────────────────
// Her sekmenin rota, etiketi ve ikon ciftini bir arada tutar.
private data class BnbTab(
    val route: LyraRoute,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

private val bnbTabs = listOf(
    BnbTab(
        route = LyraRoute.Home,
        label = "Ana sayfa",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    BnbTab(
        route = LyraRoute.Search,
        label = "Ara",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search,
    ),
    BnbTab(
        route = LyraRoute.Library,
        label = "Kütüphane",
        selectedIcon = Icons.Filled.LibraryMusic,
        unselectedIcon = Icons.Outlined.LibraryMusic,
    ),
    BnbTab(
        route = LyraRoute.Favorites,
        label = "Favoriler",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder,
    ),
    BnbTab(
        route = LyraRoute.Profile,
        label = "Profil",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
    ),
)

// ── LyraBnb ────────────────────────────────────────────────────────────────
// Uygulamanin alt navigasyon cubugu.
// navController: BNB'nin hangi sekmenin aktif oldugunu belirlemek icin kullanilir.
// Navigasyon eylemleri, mevcut sekmeyi kaldirmadan back stack'i yonetir.
@Composable
fun LyraBnb(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
        bnbTabs.forEach { tab ->
            val isSelected = currentDestination?.route
                ?.contains(tab.route::class.qualifiedName ?: "") == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(tab.route) {
                        // Ana sekmeye geri donuldugunde back stack'i temizle
                        popUpTo(LyraRoute.Home) {
                            saveState = true
                        }
                        // Ayni sekmeye tekrar tiklandiginda kopya olusturma
                        launchSingleTop = true
                        // Onceki state'i geri yukle
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                        contentDescription = tab.label,
                    )
                },
                label = {
                    Text(
                        text = tab.label,
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
        }
    }
}
