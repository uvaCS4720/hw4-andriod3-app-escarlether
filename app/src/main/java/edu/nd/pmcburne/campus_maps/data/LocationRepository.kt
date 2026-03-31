package edu.nd.pmcburne.campus_maps.data

import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Define the API Service
interface CampusApi {
    @GET("~wxt4gm/placemarks.json")
    suspend fun getPlacemarks(): List<CampusLocation>
}

class LocationRepository(private val locationDao: LocationDao) {

    // Retrofit setup for the UVA API
    private val api = Retrofit.Builder()
        .baseUrl("https://www.cs.virginia.edu/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CampusApi::class.java)

    // Pulls data from the Local SQLite Database
    fun getLocations(): Flow<List<CampusLocation>> = locationDao.getAllLocations()

    // The Sync Logic: Pull from API and save to Room
    suspend fun syncLocationsFromApi() {
        try {
            val networkLocations = api.getPlacemarks()
            // Room's OnConflictStrategy.REPLACE handles the "no duplicates" rule automatically
            locationDao.insertLocations(networkLocations)
        } catch (e: Exception) {
            // Log error or handle offline state
            e.printStackTrace()
        }
    }
}