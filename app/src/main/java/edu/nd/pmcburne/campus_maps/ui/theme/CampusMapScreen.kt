package edu.nd.pmcburne.campus_maps.ui.theme


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampusMapScreen(viewModel: MapViewModel,
                    modifier: Modifier = Modifier // <--- ADD THIS LINE HERE

) {
    val selectedTag by viewModel.selectedTag.collectAsState()
    val uniqueTags by viewModel.uniqueTags.collectAsState()
    val locations by viewModel.filteredLocations.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    // UVA approximate center for the camera
    val uvaCenter = LatLng(38.0336, -78.5080)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(uvaCenter, 14f)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // --- DROPDOWN MENU ---
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = selectedTag,
                onValueChange = {},
                readOnly = true,
                label = { Text("Filter by Tag") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                uniqueTags.forEach { tag ->
                    DropdownMenuItem(
                        text = { Text(tag) },
                        onClick = {
                            viewModel.updateSelectedTag(tag)
                            expanded = false
                        }
                    )
                }
            }
        }

        // --- GOOGLE MAP ---
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            // Draw markers for filtered locations
            locations.forEach { location ->
                val position = LatLng(location.visual_center.latitude, location.visual_center.longitude)
                Marker(
                    state = MarkerState(position = position),
                    title = location.name,
                    snippet = location.description
                )
            }
        }
    }
}