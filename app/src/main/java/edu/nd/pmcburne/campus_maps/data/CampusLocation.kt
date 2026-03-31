package edu.nd.pmcburne.campus_maps.data

import androidx.room3.PrimaryKey
import androidx.room3.Embedded
import androidx.room3.Entity

@Entity(tableName = "locations")
data class CampusLocation(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val tag_list: List<String>,
    @Embedded val visual_center: VisualCenter
)

data class VisualCenter(
    val latitude: Double,
    val longitude: Double
)