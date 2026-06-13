package com.turkcell.lyraapp.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

// ── Home Ekrani Placeholder ────────────────────────────────────────────────
// Bu composable, Home ekraninin tam tasarimi tamamlanana kadar
// yer tutucu (placeholder) olarak kullanilir.
// MVI altyapisi ilerleyen sprintte eklenecektir.
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Ana Sayfa",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}
