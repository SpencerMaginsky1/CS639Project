// ui/navigation/NutritionApp.kt
package com.example.nutritiontracker.ui.navigation

import android.util.Log
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.nutritiontracker.camera.CameraController
import com.example.nutritiontracker.camera.CameraScreen
import com.example.nutritiontracker.data.RDIRequirements
import com.example.nutritiontracker.ui.goals.GoalsScreen
import com.example.nutritiontracker.ui.home.AddFoodSection
import com.example.nutritiontracker.ui.home.HomeScreen
import com.example.nutritiontracker.ui.rdi.RDIResultsScreen
import com.example.nutritiontracker.ui.settings.SettingsScreen

@Composable
fun NutritionApp(cameraController: CameraController) {   // still passed from MainActivity
    var selectedScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var rdiRequirements by remember { mutableStateOf<RDIRequirements?>(null) }
    var userName by remember { mutableStateOf("") }
    var scannedBarcode by remember { mutableStateOf<String?>(null) }

    cameraController.setOnBarcodeScannedListener { barcode ->
        scannedBarcode = barcode
        selectedScreen = Screen.Home

        Log.i("MLKit", "Barcode received in callback: $scannedBarcode")

        //TODO: Call the FoodData Center API here using scannedBarcode
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = selectedScreen.route == item.screen.route,
                        onClick = { selectedScreen = item.screen },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.screen.label
                            )
                        },
                        label = { Text(item.screen.label) }
                    )
                }
            }
        }
    ) { _ ->
        when (selectedScreen) {
            Screen.Home -> HomeScreen(
                onSettingsClick = { selectedScreen = Screen.Settings },
                modifier = Modifier,
                cameraController = cameraController,
                onScanClick = { selectedScreen = Screen.Camera }
            )
            
            Screen.AddFood -> AddFoodSection(
                cameraController = TODO(),
                onScanClick = TODO()
            )

            Screen.Camera -> CameraScreen(cameraController)

            Screen.Goals -> GoalsScreen(
                onSettingsClick = { selectedScreen = Screen.Settings }
            )

            Screen.Settings -> SettingsScreen(
                onRDICalculate = { rdi, name ->
                    rdiRequirements = rdi
                    userName = name
                    selectedScreen = Screen.RDIResults
                }
            )

            Screen.RDIResults -> {
                rdiRequirements?.let { rdi ->
                    RDIResultsScreen(
                        rdiRequirements = rdi,
                        userName = userName,
                        onBack = { selectedScreen = Screen.Settings }
                    )
                }
            }
        }
    }
}