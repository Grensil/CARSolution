package com.example.carsolution.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.carsolution.core.navigation.InsuranceGraph
import com.example.carsolution.feature.accident.navigation.accidentNavGraph
import com.example.carsolution.feature.fuel.navigation.fuelNavGraph
import com.example.carsolution.feature.insurance.navigation.insuranceNavGraph
import com.example.carsolution.feature.usedcar.navigation.usedCarNavGraph

@Composable
fun CARSolutionNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = InsuranceGraph,
        modifier = modifier,
    ) {
        insuranceNavGraph(navController)
        fuelNavGraph(navController)
        usedCarNavGraph(navController)
        accidentNavGraph(navController)
    }
}
