package com.pixelro.nenoonkiosk.di

import com.harang.data.api.NenoonKioskApi
import com.harang.data.datasource.SignInRemoteDataSource
import com.harang.data.datasource.SurveyRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSourceModule {

    @Provides
    @Singleton
    fun provideSignInRemoteDataSource(api: NenoonKioskApi): SignInRemoteDataSource {
        return SignInRemoteDataSource(api)
    }

    @Provides
    @Singleton
    fun provideSurveyRemoteDataSource(api: NenoonKioskApi): SurveyRemoteDataSource {
        return SurveyRemoteDataSource(api)
    }
}