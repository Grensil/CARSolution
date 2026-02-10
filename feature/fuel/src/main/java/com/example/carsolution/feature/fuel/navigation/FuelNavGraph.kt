package com.example.carsolution.feature.fuel.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.carsolution.core.navigation.FuelGraph
import com.example.carsolution.core.navigation.FuelHome
import com.example.carsolution.core.navigation.FuelStationDetail
import com.example.carsolution.core.navigation.FuelStationList
import com.example.carsolution.core.navigation.VehicleDetail
import com.example.carsolution.core.navigation.vehicleDetailScreen
import com.example.carsolution.feature.fuel.screen.FuelHomeScreen
import com.example.carsolution.feature.fuel.screen.FuelStationDetailScreen
import com.example.carsolution.feature.fuel.screen.FuelStationListScreen

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
