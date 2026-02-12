package com.grensil.carinfo.data.di

import com.grensil.carinfo.data.auth.datasource.AuthRemoteDataSource
import com.grensil.carinfo.data.auth.datasource.FirebaseAuthDataSource
import com.grensil.carinfo.data.auth.repository.AuthRepositoryImpl
import com.grensil.carinfo.domain.repository.AuthRepository
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
