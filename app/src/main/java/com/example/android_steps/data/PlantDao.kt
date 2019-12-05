package com.example.android_steps.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * author: Elemen
 * Time: 2019-12-03
 * Description:
 */
@Dao
interface PlantDao {
	@Query("SELECT * FROM plants ORDER BY name")
	fun getPlants(): LiveData<List<Plant>>
	
	@Query("SELECT * FROM plants WHERE growZoneNumber = :growZoneNumber ORDER BY name")
	fun getPlantsWithGrowZoneNumber(growZoneNumber: Int): LiveData<List<Plant>>
	
	@Query("SELECT * FROM plants WHERE id = :plantId")
	fun getPlant(plantId: String): LiveData<Plant>
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertAll(plants: List<Plant>)
}