package com.example.swapcampus_djuissiange_fomigabriel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.swapcampus_djuissiange_fomigabriel.ui.listings.ListingsScreen
import com.example.swapcampus_djuissiange_fomigabriel.ui.login.LoginScreen
import com.example.swapcampus_djuissiange_fomigabriel.ui.post.PostListingScreen
import androidx.compose.runtime.setValue
import com.example.swapcampus_djuissiange_fomigabriel.ui.navigation.AppNavigation


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }

    }
}
