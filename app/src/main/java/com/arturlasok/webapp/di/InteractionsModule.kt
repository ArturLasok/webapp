package com.arturlasok.webapp.di

import com.arturlasok.webapp.feature_auth.data.repository.ApiInteraction
import com.arturlasok.webapp.feature_auth.data.repository.RoomInteraction
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.HttpClient

@Module
@InstallIn(ViewModelComponent::class)
object InteractionsModule {

    @ViewModelScoped
    @Provides
    fun provideRoomInteraction() : RoomInteraction {
        return RoomInteraction()
    }
    @ViewModelScoped
    @Provides
    fun provideApiInteraction(
        ktorClient: HttpClient) : ApiInteraction {
        return ApiInteraction(ktorClient = ktorClient)
    }
}