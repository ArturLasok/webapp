package com.arturlasok.feature_core.data.datasource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arturlasok.feature_core.data.datasource.room.model.MessageEntity

@Database(entities = arrayOf(
MessageEntity::class
),
    version = 1,
    exportSchema =false,
    //autoMigrations = [ AutoMigration(from = 18, to = 19, spec = AppDatabase.MyAutoMig::class)]


)

abstract class CoreDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao

    companion object{
        const val DATABASE_NAME: String = "webapp_core_db"
    }

}