package com.mobbile.paul.mttms.providers

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mobbile.paul.mttms.models.*


@Database(entities = [
    EntityModules::class, EntitySpiners::class, EntityAllOutletsList::class,EntityGetSalesEntry::class
   ], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val appdao: AppDao

    companion object {
        val DATABASE_NAME = "tm_mobiletrader_application"
    }

}