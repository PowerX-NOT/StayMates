package com.android.staymates

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.android.staymates.ui.StayMatesTheme
import com.android.staymates.ui.navigation.StayMatesApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            StayMatesTheme {
                StayMatesApp()
            }
        }
    }
}