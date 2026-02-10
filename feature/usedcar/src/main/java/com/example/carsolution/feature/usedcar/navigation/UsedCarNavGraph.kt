package com.example.carsolution.feature.usedcar.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.carsolution.core.navigation.UsedCarDetail
import com.example.carsolution.core.navigation.UsedCarGraph
import com.example.carsolution.core.navigation.UsedCarHome
import com.example.carsolution.core.navigation.UsedCarList
import com.example.carsolution.core.navigation.VehicleDetail
import com.example.carsolution.core.navigation.vehicleDetailScreen
import com.example.carsolution.feature.usedcar.screen.UsedCarDetailScreen
import com.example.carsolution.feature.usedcar.screen.UsedCarHomeScreen
import com.example.carsolution.feature.usedcar.screen.UsedCarListScreen

fun NavGraphBuilder.usedCarNavGraph(navController: NavController) {
    navigation<UsedCarGraph>(startDestination = UsedCarHome) {
        composable<UsedCarHome> {
            UsedCarHomeScreen(
                onNavigateToList = { navController.navigate(UsedCarList) },
                onNavigateToDetail = { id -> navController.navigate(UsedCarDetail(id)) },
            )
        }
        composable<UsedCarList> {
            UsedCarListScreen(
                onBack = { navController.popBackStack() },
                onNavigateToDetail = { id -> navController.navigate(UsedCarDetail(id)) },
            )
        }
        composable<UsedCarDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<UsedCarDetail>()
            UsedCarDetailScreen(
                carId = route.carId,
                onBack = { navController.popBackStack() },
                onNavigateToVehicle = { id -> navController.navigate(VehicleDetail(id)) },
            )
        }
        vehicleDetailScreen(onBack = { navController.popBackStack() })
    }
}
