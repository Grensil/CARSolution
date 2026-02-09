package com.example.carsolution.feature.insurance.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.carsolution.core.navigation.InsuranceDetail
import com.example.carsolution.core.navigation.InsuranceGraph
import com.example.carsolution.core.navigation.InsuranceHome
import com.example.carsolution.core.navigation.InsuranceList
import com.example.carsolution.core.navigation.VehicleDetail
import com.example.carsolution.core.navigation.vehicleDetailScreen
import com.example.carsolution.feature.insurance.screen.InsuranceDetailScreen
import com.example.carsolution.feature.insurance.screen.InsuranceHomeScreen
import com.example.carsolution.feature.insurance.screen.InsuranceListScreen

fun NavGraphBuilder.insuranceNavGraph(navController: NavController) {
    navigation<InsuranceGraph>(startDestination = InsuranceHome) {
        composable<InsuranceHome> {
            InsuranceHomeScreen(
                onNavigateToList = { navController.navigate(InsuranceList) },
                onNavigateToDetail = { id -> navController.navigate(InsuranceDetail(id)) },
                onNavigateToVehicle = { id -> navController.navigate(VehicleDetail(id)) },
            )
        }
        composable<InsuranceList> {
            InsuranceListScreen(
                onBack = { navController.popBackStack() },
                onNavigateToDetail = { id -> navController.navigate(InsuranceDetail(id)) },
            )
        }
        composable<InsuranceDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<InsuranceDetail>()
            InsuranceDetailScreen(
                insuranceId = route.insuranceId,
                onBack = { navController.popBackStack() },
                onNavigateToVehicle = { id -> navController.navigate(VehicleDetail(id)) },
            )
        }
        vehicleDetailScreen(onBack = { navController.popBackStack() })
    }
}
