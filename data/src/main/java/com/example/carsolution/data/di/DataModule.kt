package com.example.carsolution.data.di

import com.example.carsolution.data.repository.FakeAccidentRepository
import com.example.carsolution.data.repository.FakeFuelStationRepository
import com.example.carsolution.data.repository.FakeInsuranceRepository
import com.example.carsolution.data.repository.FakeUsedCarRepository
import com.example.carsolution.data.repository.FakeVehicleRepository
import com.example.carsolution.domain.repository.AccidentRepository
import com.example.carsolution.domain.repository.FuelStationRepository
import com.example.carsolution.domain.repository.InsuranceRepository
import com.example.carsolution.domain.repository.UsedCarRepository
import com.example.carsolution.domain.repository.VehicleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("AbstractClassCanBeInterface")
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindInsuranceRepository(impl: FakeInsuranceRepository): InsuranceRepository

    @Binds
    @Singleton
    abstract fun bindFuelStationRepository(impl: FakeFuelStationRepository): FuelStationRepository

    @Binds
    @Singleton
    abstract fun bindUsedCarRepository(impl: FakeUsedCarRepository): UsedCarRepository

    @Binds
    @Singleton
    abstract fun bindAccidentRepository(impl: FakeAccidentRepository): AccidentRepository

    @Binds
    @Singleton
    abstract fun bindVehicleRepository(impl: FakeVehicleRepository): VehicleRepository
}
