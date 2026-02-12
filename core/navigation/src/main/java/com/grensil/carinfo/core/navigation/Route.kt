package com.grensil.carinfo.core.navigation

import kotlinx.serialization.Serializable

// ── App-level ────────────────────────────────────────────────
@Serializable data object SplashRoute

@Serializable data object AuthGraph

@Serializable data object VehicleNumberInput

@Serializable data class VehicleConfirm(
    val plateNumber: String,
)

@Serializable data object PhoneVerification

@Serializable data object MainRoute

// ── Graph Routes ──────────────────────────────────────────────
@Serializable data object InsuranceGraph

@Serializable data object FuelGraph

@Serializable data object VehicleSpecGraph

@Serializable data object AccidentGraph

// ── Insurance ─────────────────────────────────────────────────
@Serializable data object InsuranceHome

@Serializable data object InsuranceList

@Serializable data class InsuranceDetail(
    val insuranceId: String,
)

// ── Fuel ──────────────────────────────────────────────────────
@Serializable data object FuelHome

@Serializable data object FuelStationList

@Serializable data class FuelStationDetail(
    val stationId: String,
)

// ── VehicleSpec ──────────────────────────────────────────────
@Serializable data object VehicleSpecHome

@Serializable data object VehicleSpecSearch

@Serializable data class VehicleSpecDetail(
    val vin: String,
)

// ── Accident ──────────────────────────────────────────────────
@Serializable data object AccidentHome

@Serializable data object AccidentReport

@Serializable data class AccidentDetail(
    val accidentId: String,
)

// ── Shared ────────────────────────────────────────────────────
@Serializable data class VehicleDetail(
    val vehicleId: String,
)
