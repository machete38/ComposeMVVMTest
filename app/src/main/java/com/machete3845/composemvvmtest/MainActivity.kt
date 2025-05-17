package com.machete3845.composemvvmtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.machete3845.news_main.NewsMainScreen
import com.machete3845.news_uikit.ComposeMVVMTestTheme
import com.machete3845.news_uikit.ComposeMVVMTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeMVVMTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = ComposeMVVMTheme.colorScheme.background
                ) {
                        NewsMainScreen()
                }
            }
        }
    }
}


