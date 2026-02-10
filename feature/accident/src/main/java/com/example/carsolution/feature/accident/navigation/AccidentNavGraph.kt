package com.example.carsolution.feature.accident.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.carsolution.core.navigation.AccidentDetail
import com.example.carsolution.core.navigation.AccidentGraph
import com.example.carsolution.core.navigation.AccidentHome
import com.example.carsolution.core.navigation.AccidentReport
import com.example.carsolution.core.navigation.VehicleDetail
import com.example.carsolution.core.navigation.vehicleDetailScreen
import com.example.carsolution.feature.accident.screen.AccidentDetailScreen
import com.example.carsolution.feature.accident.screen.AccidentHomeScreen
import com.example.carsolution.feature.accident.screen.AccidentReportScreen

fun NavGraphBuilder.accidentNavGraph(navController: NavController) {
    navigation<AccidentGraph>(startDestination = AccidentHome) {
        composable<AccidentHome> {
            AccidentHomeScreen(
                onNavigateToReport = { navController.navigate(AccidentReport) },
                onNavigateToDetail = { id -> navController.navigate(AccidentDetail(id)) },
            )
        }
        composable<AccidentReport> {
            AccidentReportScreen(
                onBack = { navController.popBackStack() },
                onNavigateToVehicle = { id -> navController.navigate(VehicleDetail(id)) },
            )
        }
        composable<AccidentDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<AccidentDetail>()
            AccidentDetailScreen(
                accidentId = route.accidentId,
                onBack = { navController.popBackStack() },
                onNavigateToVehicle = { id -> navController.navigate(VehicleDetail(id)) },
            )
        }
        vehicleDetailScreen(onBack = { navController.popBackStack() })
    }
}
