package com.example.cricketscorecompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                var status by remember { mutableStateOf("Waiting for Wear request...") }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Phone App", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = status)

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Note: This app runs mostly in the background.\nThe Wear app will request data via MessageClient.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
