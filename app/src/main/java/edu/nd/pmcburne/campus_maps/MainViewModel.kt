package edu.nd.pmcburne.campus_maps

import androidx.lifecycle.ViewModel
import edu.nd.pmcburne.campus_maps.data.LocationRepository
import edu.nd.pmcburne.campus_maps.ui.theme.MapViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MainUIState(
    val counterValue: Int
)
// Add this to the bottom of ui/MapViewModel.kt
class MapViewModelFactory(private val repository: LocationRepository) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}