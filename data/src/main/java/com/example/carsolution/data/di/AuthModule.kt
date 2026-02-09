package com.example.carsolution.data.di

import com.example.carsolution.data.auth.datasource.AuthRemoteDataSource
import com.example.carsolution.data.auth.datasource.FirebaseAuthDataSource
import com.example.carsolution.data.auth.repository.AuthRepositoryImpl
import com.example.carsolution.domain.repository.AuthRepository
import com.example.carsolution.domain.usecase.GetCurrentUserUseCase
import com.example.carsolution.domain.usecase.SendVerificationCodeUseCase
import com.example.carsolution.domain.usecase.VerifyPhoneCodeUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(impl: FirebaseAuthDataSource): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    companion object {
        @Provides
        fun provideSendVerificationCodeUseCase(repository: AuthRepository): SendVerificationCodeUseCase =
            SendVerificationCodeUseCase(repository)

        @Provides
        fun provideVerifyPhoneCodeUseCase(repository: AuthRepository): VerifyPhoneCodeUseCase =
            VerifyPhoneCodeUseCase(repository)

        @Provides
        fun provideGetCurrentUserUseCase(repository: AuthRepository): GetCurrentUserUseCase =
            GetCurrentUserUseCase(repository)
    }
}
