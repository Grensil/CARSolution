package com.grensil.carinfo.data.di

import com.grensil.carinfo.data.repository.FakeAccidentRepository
import com.grensil.carinfo.data.repository.FakeInsuranceRepository
import com.grensil.carinfo.data.repository.FakeVehicleRepository
import com.grensil.carinfo.data.repository.NhtsaVehicleSpecRepository
import com.grensil.carinfo.data.repository.OpinetFuelStationRepository
import com.grensil.carinfo.domain.repository.AccidentRepository
import com.grensil.carinfo.domain.repository.FuelStationRepository
import com.grensil.carinfo.domain.repository.InsuranceRepository
import com.grensil.carinfo.domain.repository.VehicleRepository
import com.grensil.carinfo.domain.repository.VehicleSpecRepository
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
    abstract fun bindFuelStationRepository(impl: OpinetFuelStationRepository): FuelStationRepository

    @Binds
    @Singleton
    abstract fun bindVehicleSpecRepository(impl: NhtsaVehicleSpecRepository): VehicleSpecRepository

    @Binds
    @Singleton
    abstract fun bindAccidentRepository(impl: FakeAccidentRepository): AccidentRepository

    @Binds
    @Singleton
    abstract fun bindVehicleRepository(impl: FakeVehicleRepository): VehicleRepository
}
