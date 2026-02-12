package com.grensil.carinfo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grensil.carinfo.core.navigation.MainRoute
import com.grensil.carinfo.core.navigation.SplashRoute
import com.grensil.carinfo.feature.auth.navigation.authNavGraph
import com.grensil.carinfo.feature.auth.screen.SplashScreen
import com.grensil.carinfo.ui.MainScreen

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
                    navController.navigate(com.grensil.carinfo.core.navigation.AuthGraph) {
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
