package edu.nd.pmcburne.campus_maps.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import edu.nd.pmcburne.campus_maps.data.CampusLocation

// --- FIX 1: HTML DECODER ---
fun String.decodeHtml(): String {
    return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampusMapScreen(
    viewModel: MapViewModel,
    modifier: Modifier = Modifier
) {
    val selectedTag by viewModel.selectedTag.collectAsState()
    val uniqueTags by viewModel.uniqueTags.collectAsState()
    val locations by viewModel.filteredLocations.collectAsState()

    // State to track which location the user clicked on
    var clickedLocation by remember { mutableStateOf<CampusLocation?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val uvaCenter = LatLng(38.0336, -78.5080)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(uvaCenter, 15f)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // --- DROPDOWN MENU ---
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
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
                                clickedLocation = null // Reset selection on filter
                                expanded = false
                            }
                        )
                    }
                }
            }

            // --- GOOGLE MAP ---
            GoogleMap(
                modifier = Modifier.weight(1f), // Allow space for info card if needed
                cameraPositionState = cameraPositionState,
                onMapClick = { clickedLocation = null } // Clear selection when clicking map
            ) {
                locations.forEach { location ->
                    val pos = LatLng(location.visual_center.latitude, location.visual_center.longitude)
                    Marker(
                        state = MarkerState(position = pos),
                        title = location.name.decodeHtml(),
                        onClick = {
                            clickedLocation = location
                            false // Allow default behavior (center on marker)
                        }
                    )
                }
            }
        }

        // --- FIX 2: READABLE DESCRIPTION CARD ---
        clickedLocation?.let { location ->
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = location.name.decodeHtml(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = location.description.decodeHtml(),
                        style = MaterialTheme.typography.bodyMedium,
                        softWrap = true,       // ALLOW WRAPPING
                        maxLines = 6,          // MULTI-LINE SUPPORT
                        overflow = TextOverflow.Ellipsis
                    )
                    TextButton(
                        onClick = { clickedLocation = null },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}