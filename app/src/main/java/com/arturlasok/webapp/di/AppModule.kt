package com.arturlasok.webapp.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.arturlasok.feature_core.datastore.DataStoreInteraction
import com.arturlasok.feature_core.util.Test
import com.arturlasok.feature_core.util.isOnline
import com.arturlasok.webapp.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }
    @Singleton
    @Provides
    fun provideTest() : Test {
        return Test()
    }

    @Singleton
    @Provides
    fun providesIsOnLine(@ApplicationContext app: Context) : isOnline {
        return isOnline(Contexts.getApplication(app.applicationContext))

    }
    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext app: Context) : DataStore<Preferences> =
       PreferenceDataStoreFactory.create(
           corruptionHandler = ReplaceFileCorruptionHandler(
               produceNewData = { emptyPreferences() }
           ),
            produceFile = { app.preferencesDataStoreFile("ustawienia")},
        )
    @Singleton
    @Provides
    fun provideDataStoreInteraction(dataStore: DataStore<Preferences>) : DataStoreInteraction {
        return DataStoreInteraction(dataStore)
    }

}