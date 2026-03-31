package edu.nd.pmcburne.campus_maps.data

import androidx.room.PrimaryKey
import androidx.room.Embedded
import androidx.room.Entity

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