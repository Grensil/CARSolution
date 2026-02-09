package com.example.carsolution.domain.repository

import com.example.carsolution.domain.model.UsedCar

interface UsedCarRepository {
    suspend fun getUsedCarList(): List<UsedCar>
    suspend fun getUsedCarById(id: String): UsedCar?
}
