package com.example.android_steps.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.android_steps.workers.SeedDatabaseWorker

/**
 * author: Elemen
 * Time: 2019-11-29
 * Description:
 */
@Database(entities = [GardenPlanting::class, Plant::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
	abstract fun gardenPlantingDao(): GardenPlantingDao
	abstract fun plantDao(): PlantDao
	
	companion object {
		@Volatile
		private var instance: AppDatabase? = null
		
		fun getInstance(context: Context): AppDatabase {
			return instance ?: synchronized(this) {
				instance ?: buildDatabase(context).also { instance = it }
			}
		}
		
		private fun buildDatabase(context: Context): AppDatabase {
			return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
				.addCallback(object : RoomDatabase.Callback() {
					override fun onCreate(db: SupportSQLiteDatabase) {
						super.onCreate(db)
						val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
						WorkManager.getInstance(context).enqueue(request)
					}
				}).build()
		}
	}
}