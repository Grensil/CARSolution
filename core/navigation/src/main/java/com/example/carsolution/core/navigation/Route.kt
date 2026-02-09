package com.example.carsolution.core.navigation

import kotlinx.serialization.Serializable

// ── Graph Routes ──────────────────────────────────────────────
@Serializable data object InsuranceGraph
@Serializable data object FuelGraph
@Serializable data object UsedCarGraph
@Serializable data object AccidentGraph

// ── Insurance ─────────────────────────────────────────────────
@Serializable data object InsuranceHome
@Serializable data object InsuranceList
@Serializable data class InsuranceDetail(val insuranceId: String)

// ── Fuel ──────────────────────────────────────────────────────
@Serializable data object FuelHome
@Serializable data object FuelStationList
@Serializable data class FuelStationDetail(val stationId: String)

// ── UsedCar ───────────────────────────────────────────────────
@Serializable data object UsedCarHome
@Serializable data object UsedCarList
@Serializable data class UsedCarDetail(val carId: String)

// ── Accident ──────────────────────────────────────────────────
@Serializable data object AccidentHome
@Serializable data object AccidentReport
@Serializable data class AccidentDetail(val accidentId: String)

// ── Shared ────────────────────────────────────────────────────
@Serializable data class VehicleDetail(val vehicleId: String)
