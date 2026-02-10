package com.example.carsolution.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.carsolution.core.designsystem.component.CARSolutionBottomBar
import com.example.carsolution.core.navigation.InsuranceGraph
import com.example.carsolution.core.navigation.TopLevelDestination
import com.example.carsolution.feature.accident.navigation.accidentNavGraph
import com.example.carsolution.feature.fuel.navigation.fuelNavGraph
import com.example.carsolution.feature.insurance.navigation.insuranceNavGraph
import com.example.carsolution.feature.usedcar.navigation.usedCarNavGraph

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentTopLevel = TopLevelDestination.entries.find { dest ->
        currentDestination?.hierarchy?.any { it.hasRoute(dest.route::class) } == true
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            CARSolutionBottomBar(
                destinations = TopLevelDestination.entries,
                currentDestination = currentTopLevel,
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = InsuranceGraph,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            insuranceNavGraph(navController)
            fuelNavGraph(navController)
            usedCarNavGraph(navController)
            accidentNavGraph(navController)
        }
    }
}
