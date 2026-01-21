package com.danielvilha.wubba_lubba

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.danielvilha.wubba_lubba.ui.route.Destinations
import com.danielvilha.wubba_lubba.ui.route.WubbaLubbaNavGraph
import com.danielvilha.wubba_lubba.ui.theme.WubbaLubbaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WubbaLubbaTheme {
                WubbaLubbaNavGraph(Destinations.CHARACTERS_ROUTE)
            }
        }
    }
}