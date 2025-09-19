package com.dietapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.dietapplication.ui.theme.DietApplicationTheme
import com.dietapplication.view.navigation.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DietApplicationTheme {
                val navController = rememberNavController()

                // Şimdilik sabit. Sonra DataStore/Auth ile besleyeceğiz.
                val isFirstLaunchFromStore = true
                val isLoggedInFromAuth = false

                Scaffold { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding) // <- content padding kullanıldı
                    ) {
                        AppNavHost(
                            navController = navController,
                            isFirstLaunch = isFirstLaunchFromStore,
                            isLoggedIn = isLoggedInFromAuth
                        )
                    }
                }
            }
        }
    }
}
