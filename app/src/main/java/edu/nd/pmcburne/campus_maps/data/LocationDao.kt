package edu.nd.pmcburne.campus_maps.data

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM locations")
    fun getAllLocations(): Flow<List<CampusLocation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocations(locations: List<CampusLocation>)
}
