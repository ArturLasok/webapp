package com.arturlasok.webapp.di

import com.arturlasok.feature_core.data.datasource.room.MessageDao
import com.arturlasok.feature_core.data.repository.ApiInteraction
import com.arturlasok.feature_core.data.repository.RoomInteraction
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
    fun provideRoomInteraction(messageDao: MessageDao) : RoomInteraction {
        return RoomInteraction(messageDao = messageDao)
    }
    @ViewModelScoped
    @Provides
    fun provideApiInteraction(
        ktorClient: HttpClient) : ApiInteraction {
        return ApiInteraction(ktorClient = ktorClient)
    }

}