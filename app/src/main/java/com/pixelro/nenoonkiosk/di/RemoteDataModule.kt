package com.pixelro.nenoonkiosk.di

import com.harang.data.api.NenoonKioskApi
import com.harang.data.datasource.SignInRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataModule {

    @Provides
    fun provideSignInRemoteDataSource(api: NenoonKioskApi): SignInRemoteDataSource {
        return SignInRemoteDataSource(api)
    }
}