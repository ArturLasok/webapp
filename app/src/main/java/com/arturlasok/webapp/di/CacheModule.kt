package com.arturlasok.webapp.di

import androidx.room.Room
import com.arturlasok.feature_core.data.datasource.room.CoreDatabase
import com.arturlasok.feature_core.data.datasource.room.MessageDao
import com.arturlasok.webapp.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideDb(app: BaseApplication): CoreDatabase {
        return Room
            .databaseBuilder(app, CoreDatabase::class.java, CoreDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }



    @Singleton
    @Provides
    fun provideMessageDao(db: CoreDatabase): MessageDao {
        return db.messageDao()
    }



}