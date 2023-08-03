package com.pixelro.nenoonkiosk.di

import com.harang.data.datasource.SharedPreferencesDataSource
import com.harang.data.datasource.SignInRemoteDataSource
import com.harang.data.repository.ScreenSaverRepository
import com.harang.data.repository.SignInRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideSignInResultRepository(
        remoteDataSource: SignInRemoteDataSource,
        sharedPreferencesDataSource: SharedPreferencesDataSource
    ): SignInRepository {
        return SignInRepository(
            remoteDataSource = remoteDataSource,
            sharedPreferencesDataSource = sharedPreferencesDataSource
        )
    }

    @Provides
    fun provideScreenSaverRepository(
        sharedPreferencesDataSource: SharedPreferencesDataSource
    ): ScreenSaverRepository {
        return ScreenSaverRepository(
            sharedPreferencesDataSource = sharedPreferencesDataSource
        )
    }
}