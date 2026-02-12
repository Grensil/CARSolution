package com.grensil.carinfo.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CarCrash
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.CarCrash
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val label: String,
    val route: Any,
) {
    INSURANCE(
        selectedIcon = Icons.Filled.Shield,
        unselectedIcon = Icons.Outlined.Shield,
        label = "보험",
        route = InsuranceGraph,
    ),
    FUEL(
        selectedIcon = Icons.Filled.LocalGasStation,
        unselectedIcon = Icons.Outlined.LocalGasStation,
        label = "주유",
        route = FuelGraph,
    ),
    VEHICLE_SPEC(
        selectedIcon = Icons.Filled.DirectionsCar,
        unselectedIcon = Icons.Outlined.DirectionsCar,
        label = "차량스펙",
        route = VehicleSpecGraph,
    ),
    ACCIDENT(
        selectedIcon = Icons.Filled.CarCrash,
        unselectedIcon = Icons.Outlined.CarCrash,
        label = "사고",
        route = AccidentGraph,
    ),
}
