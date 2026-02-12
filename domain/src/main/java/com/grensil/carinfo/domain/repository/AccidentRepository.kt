package com.grensil.carinfo.domain.repository

import com.grensil.carinfo.domain.model.Accident

interface AccidentRepository {
    suspend fun getAccidentList(): List<Accident>

    suspend fun getAccidentById(id: String): Accident?
}
