package com.pixelro.nenoonkiosk.di

import com.harang.data.datasource.SignInRemoteDataSource
import com.harang.data.repository.SignInResultRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideSignInResultRepository(source: SignInRemoteDataSource): SignInResultRepository {
        return SignInResultRepository(source)
    }
}