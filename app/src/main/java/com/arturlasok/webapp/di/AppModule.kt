package com.arturlasok.webapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.arturlasok.feature_core.datastore.DataStoreInteraction
import com.arturlasok.feature_core.util.isOnline
import com.arturlasok.webapp.BaseApplication
import com.arturlasok.webapp.feature_auth.model.MessageListGlobalState
import com.arturlasok.webapp.util.LocalObjectIdSerializer
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.bson.codecs.kotlinx.ObjectIdSerializer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    fun provideKtorClient() : HttpClient {
       return HttpClient {
           install(ContentNegotiation) {
               json(Json {
                  // serializersModule = SerializersModule {  contextual( ObjectIdSerializer) }
                   prettyPrint = true
                   ignoreUnknownKeys = true
                   //useAlternativeNames = false
                   encodeDefaults = true

               })

           }
       }
    }


    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
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


    @Singleton
    @Provides
    fun providesFireAuth(@ApplicationContext app: Context) : FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
    @Singleton
    @Provides
    fun provideMessagesListGlobalState(dataStoreInteraction: DataStoreInteraction) : MessageListGlobalState {
        return MessageListGlobalState(dataStoreInteraction)
    }

}