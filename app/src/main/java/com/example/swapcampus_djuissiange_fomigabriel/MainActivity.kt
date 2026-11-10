package com.example.swapcampus_djuissiange_fomigabriel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import com.isep.composeapp.ui.theme.SwapCampusTheme

/**
 * Entry point of the Android app.
 * setContent { } is where the Compose screens are displayed.
 */
class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SwapCampusTheme {
                SwapCampusApp()
            }
        }
    }
}