package com.example.shuat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.shuat.presentation.navigation.ShuaTiNavHost
import com.example.shuat.presentation.theme.ShuaTiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShuaTiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ShuaTiNavHost(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
