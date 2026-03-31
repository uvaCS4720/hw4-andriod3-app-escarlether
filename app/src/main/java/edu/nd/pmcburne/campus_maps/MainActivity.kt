package edu.nd.pmcburne.campus_maps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.nd.pmcburne.campus_maps.data.AppDatabase
import edu.nd.pmcburne.campus_maps.data.LocationRepository
import edu.nd.pmcburne.campus_maps.ui.theme.CampusMapScreen
import edu.nd.pmcburne.campus_maps.ui.theme.MapViewModel
import edu.nd.pmcburne.campus_maps.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Initialize Database and Repository
        val database = AppDatabase.getDatabase(this)
        val repository = LocationRepository(database.locationDao())

        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                // 2. Use a Factory to pass the repository into the ViewModel
                val mapViewModel: MapViewModel = viewModel(
                    factory = MapViewModelFactory(repository)
                )

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // 3. Call your actual Campus Map UI
                    CampusMapScreen(
                        viewModel = mapViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}