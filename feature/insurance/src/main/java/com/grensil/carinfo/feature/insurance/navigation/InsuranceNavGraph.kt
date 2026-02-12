package com.grensil.carinfo.feature.insurance.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.grensil.carinfo.core.navigation.InsuranceDetail
import com.grensil.carinfo.core.navigation.InsuranceGraph
import com.grensil.carinfo.core.navigation.InsuranceHome
import com.grensil.carinfo.core.navigation.InsuranceList
import com.grensil.carinfo.core.navigation.VehicleDetail
import com.grensil.carinfo.core.navigation.vehicleDetailScreen
import com.grensil.carinfo.feature.insurance.screen.InsuranceDetailScreen
import com.grensil.carinfo.feature.insurance.screen.InsuranceHomeScreen
import com.grensil.carinfo.feature.insurance.screen.InsuranceListScreen

fun NavGraphBuilder.insuranceNavGraph(navController: NavController) {
    navigation<InsuranceGraph>(startDestination = InsuranceHome) {
        composable<InsuranceHome> {
            InsuranceHomeScreen(
                onNavigateToList = { navController.navigate(InsuranceList) },
                onNavigateToDetail = { id -> navController.navigate(InsuranceDetail(id)) },
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
