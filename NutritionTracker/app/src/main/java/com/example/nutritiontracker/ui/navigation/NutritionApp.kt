// ui/navigation/NutritionApp.kt
package com.example.nutritiontracker.ui.navigation

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
import com.example.nutritiontracker.ui.addfood.AddFoodScreen
import com.example.nutritiontracker.ui.goals.GoalsScreen
import com.example.nutritiontracker.ui.home.HomeScreen
import com.example.nutritiontracker.ui.settings.SettingsScreen

@Composable
fun NutritionApp() {
    var selectedScreen by remember { mutableStateOf<Screen>(Screen.Home) }

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
                onSettingsClick = { selectedScreen = Screen.Settings }
            )
            Screen.AddFood -> AddFoodScreen()
            Screen.Goals -> GoalsScreen()
            Screen.Settings -> SettingsScreen()
        }
    }
}