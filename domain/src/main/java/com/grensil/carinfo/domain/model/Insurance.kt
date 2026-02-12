package com.grensil.carinfo.domain.model

data class Insurance(
    val id: String,
    val name: String,
    val provider: String,
    val monthlyPremium: Int,
)
