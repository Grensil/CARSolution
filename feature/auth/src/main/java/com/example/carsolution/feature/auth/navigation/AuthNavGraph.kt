package com.example.carsolution.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.carsolution.core.navigation.AuthGraph
import com.example.carsolution.core.navigation.MainRoute
import com.example.carsolution.core.navigation.VehicleConfirm
import com.example.carsolution.core.navigation.VehicleNumberInput
import com.example.carsolution.feature.auth.screen.VehicleConfirmScreen
import com.example.carsolution.feature.auth.screen.VehicleNumberInputScreen

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation<AuthGraph>(startDestination = VehicleNumberInput) {
        composable<VehicleNumberInput> {
            VehicleNumberInputScreen(
                onLookup = { plateNumber ->
                    navController.navigate(VehicleConfirm(plateNumber))
                },
            )
        }
        composable<VehicleConfirm> { backStackEntry ->
            val route = backStackEntry.toRoute<VehicleConfirm>()
            VehicleConfirmScreen(
                plateNumber = route.plateNumber,
                onBack = { navController.popBackStack() },
                onConfirm = {
                    navController.navigate(MainRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
    }
}
