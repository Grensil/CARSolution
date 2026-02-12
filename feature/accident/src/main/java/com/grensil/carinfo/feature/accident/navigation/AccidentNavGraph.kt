package com.grensil.carinfo.feature.accident.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.grensil.carinfo.core.navigation.AccidentDetail
import com.grensil.carinfo.core.navigation.AccidentGraph
import com.grensil.carinfo.core.navigation.AccidentHome
import com.grensil.carinfo.core.navigation.AccidentReport
import com.grensil.carinfo.core.navigation.VehicleDetail
import com.grensil.carinfo.core.navigation.vehicleDetailScreen
import com.grensil.carinfo.feature.accident.screen.AccidentDetailScreen
import com.grensil.carinfo.feature.accident.screen.AccidentHomeScreen
import com.grensil.carinfo.feature.accident.screen.AccidentReportScreen

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
