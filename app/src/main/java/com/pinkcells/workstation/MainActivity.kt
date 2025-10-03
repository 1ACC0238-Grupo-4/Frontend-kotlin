package com.pinkcells.workstation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pinkcells.workstation.shared.ui.theme.WorkstationTheme
import com.pinkcells.workstation.shared.presentation.components.AppRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkstationTheme {
AppRoot()
            }
        }
    }
}
