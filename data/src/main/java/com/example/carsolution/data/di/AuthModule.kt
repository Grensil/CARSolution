package com.example.carsolution.data.di

import com.example.carsolution.data.auth.datasource.AuthRemoteDataSource
import com.example.carsolution.data.auth.datasource.FirebaseAuthDataSource
import com.example.carsolution.data.auth.repository.AuthRepositoryImpl
import com.example.carsolution.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("AbstractClassCanBeInterface")
@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(impl: FirebaseAuthDataSource): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}
