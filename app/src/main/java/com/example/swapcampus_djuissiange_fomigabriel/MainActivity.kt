package com.example.swapcampus_djuissiange_fomigabriel

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.isep.composeapp.App

/**
 * Entry point of the Android app: it only hosts the common App().
 * AppCompatActivity lets the in-app language selector (AppCompatDelegate)
 * work on every Android version.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App() }
    }
}