package com.grensil.carinfo.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.grensil.carinfo.core.navigation.AuthGraph
import com.grensil.carinfo.core.navigation.MainRoute
import com.grensil.carinfo.core.navigation.PhoneVerification
import com.grensil.carinfo.core.navigation.VehicleConfirm
import com.grensil.carinfo.core.navigation.VehicleNumberInput
import com.grensil.carinfo.feature.auth.screen.PhoneVerificationScreen
import com.grensil.carinfo.feature.auth.screen.VehicleConfirmScreen
import com.grensil.carinfo.feature.auth.screen.VehicleNumberInputScreen

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
                    navController.navigate(PhoneVerification)
                },
            )
        }
        composable<PhoneVerification> {
            PhoneVerificationScreen(
                onBack = { navController.popBackStack() },
                onVerified = {
                    navController.navigate(MainRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
    }
}
