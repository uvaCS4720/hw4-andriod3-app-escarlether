package edu.nd.pmcburne.campus_maps.data

import android.annotation.SuppressLint
import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.room3.TypeConverters

@SuppressLint("RestrictedApi")
@Database(entities = [CampusLocation::class], version = 1, exportSchema = false)
@TypeConverters(TagListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "campus_map_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}