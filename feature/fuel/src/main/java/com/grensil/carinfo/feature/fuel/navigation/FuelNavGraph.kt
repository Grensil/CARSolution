package com.grensil.carinfo.feature.fuel.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.grensil.carinfo.core.navigation.FuelGraph
import com.grensil.carinfo.core.navigation.FuelHome
import com.grensil.carinfo.core.navigation.FuelStationDetail
import com.grensil.carinfo.core.navigation.FuelStationList
import com.grensil.carinfo.core.navigation.VehicleDetail
import com.grensil.carinfo.core.navigation.vehicleDetailScreen
import com.grensil.carinfo.feature.fuel.screen.FuelHomeScreen
import com.grensil.carinfo.feature.fuel.screen.FuelStationDetailScreen
import com.grensil.carinfo.feature.fuel.screen.FuelStationListScreen

fun NavGraphBuilder.fuelNavGraph(navController: NavController) {
    navigation<FuelGraph>(startDestination = FuelHome) {
        composable<FuelHome> {
            FuelHomeScreen(
                onNavigateToList = { navController.navigate(FuelStationList) },
                onNavigateToDetail = { id -> navController.navigate(FuelStationDetail(id)) },
            )
        }
        composable<FuelStationList> {
            FuelStationListScreen(
                onBack = { navController.popBackStack() },
                onNavigateToDetail = { id -> navController.navigate(FuelStationDetail(id)) },
            )
        }
        composable<FuelStationDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<FuelStationDetail>()
            FuelStationDetailScreen(
                stationId = route.stationId,
                onBack = { navController.popBackStack() },
                onNavigateToVehicle = { id -> navController.navigate(VehicleDetail(id)) },
            )
        }
        vehicleDetailScreen(onBack = { navController.popBackStack() })
    }
}
