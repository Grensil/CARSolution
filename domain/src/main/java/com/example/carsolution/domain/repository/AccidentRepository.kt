package com.example.carsolution.domain.repository

import com.example.carsolution.domain.model.Accident

interface AccidentRepository {
    suspend fun getAccidentList(): List<Accident>

    suspend fun getAccidentById(id: String): Accident?
}
