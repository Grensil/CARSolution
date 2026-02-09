package com.example.carsolution.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carsolution.core.navigation.MainRoute
import com.example.carsolution.core.navigation.SplashRoute
import com.example.carsolution.feature.auth.navigation.authNavGraph
import com.example.carsolution.feature.auth.screen.SplashScreen
import com.example.carsolution.ui.MainScreen

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SplashRoute,
        modifier = modifier,
    ) {
        composable<SplashRoute> {
            SplashScreen(
                onNavigateToAuth = {
                    navController.navigate(com.example.carsolution.core.navigation.AuthGraph) {
                        popUpTo(SplashRoute) { inclusive = true }
                    }
                },
            )
        }
        authNavGraph(navController)
        composable<MainRoute> {
            MainScreen()
        }
    }
}
