package com.grensil.carinfo.feature.vehiclespec.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.grensil.carinfo.core.navigation.VehicleDetail
import com.grensil.carinfo.core.navigation.VehicleSpecDetail
import com.grensil.carinfo.core.navigation.VehicleSpecGraph
import com.grensil.carinfo.core.navigation.VehicleSpecHome
import com.grensil.carinfo.core.navigation.VehicleSpecSearch
import com.grensil.carinfo.core.navigation.vehicleDetailScreen
import com.grensil.carinfo.feature.vehiclespec.screen.VehicleSpecDetailScreen
import com.grensil.carinfo.feature.vehiclespec.screen.VehicleSpecHomeScreen
import com.grensil.carinfo.feature.vehiclespec.screen.VehicleSpecSearchScreen

fun NavGraphBuilder.vehicleSpecNavGraph(navController: NavController) {
    navigation<VehicleSpecGraph>(startDestination = VehicleSpecHome) {
        composable<VehicleSpecHome> {
            VehicleSpecHomeScreen(
                onNavigateToSearch = { navController.navigate(VehicleSpecSearch) },
                onNavigateToDetail = { vin -> navController.navigate(VehicleSpecDetail(vin)) },
            )
        }
        composable<VehicleSpecSearch> {
            VehicleSpecSearchScreen(
                onBack = { navController.popBackStack() },
                onNavigateToDetail = { vin -> navController.navigate(VehicleSpecDetail(vin)) },
            )
        }
        composable<VehicleSpecDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<VehicleSpecDetail>()
            VehicleSpecDetailScreen(
                vin = route.vin,
                onBack = { navController.popBackStack() },
                onNavigateToVehicle = { id -> navController.navigate(VehicleDetail(id)) },
            )
        }
        vehicleDetailScreen(onBack = { navController.popBackStack() })
    }
}
