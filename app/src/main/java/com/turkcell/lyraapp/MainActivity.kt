package com.turkcell.lyraapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.turkcell.lyraapp.navigation.LyraNavGraph
import com.turkcell.lyraapp.ui.theme.LyraAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LyraAppTheme {
                // Surface, arka plan rengini MaterialTheme uzerinden yonetir.
                // Navigasyon grafigi tum rota ve ekranlari barindirir.
                Surface(modifier = Modifier.fillMaxSize()) {
                    LyraNavGraph()
                }
            }
        }
    }
}