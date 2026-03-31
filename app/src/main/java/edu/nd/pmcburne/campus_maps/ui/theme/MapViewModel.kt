package edu.nd.pmcburne.campus_maps.ui.theme


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.nd.pmcburne.campus_maps.data.LocationRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MapViewModel(private val repository: LocationRepository) : ViewModel() {

    // Default to "core" per requirements
    private val _selectedTag = MutableStateFlow("core")
    val selectedTag = _selectedTag.asStateFlow()

    // Get all locations from Room as a Flow
    val allLocations = repository.getLocations()

    // Dynamically calculate unique, alphabetized tags whenever locations update
    val uniqueTags = allLocations.map { locations ->
        locations.flatMap { it.tag_list }
            .distinct()
            .sorted()
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Filter locations based on the selected tag
    val filteredLocations = combine(allLocations, selectedTag) { locations, tag ->
        locations.filter { it.tag_list.contains(tag) }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        // Trigger network sync on startup
        viewModelScope.launch {
            repository.syncLocationsFromApi()
        }
    }

    fun updateSelectedTag(newTag: String) {
        _selectedTag.value = newTag
    }
}