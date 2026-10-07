package com.example.swapcampus_djuissiange_fomigabriel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme

/**
 * Entry point of the Android app.
 * setContent { } is where the Compose screens are displayed.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SwapCampusApp()
            }
        }
    }
}
