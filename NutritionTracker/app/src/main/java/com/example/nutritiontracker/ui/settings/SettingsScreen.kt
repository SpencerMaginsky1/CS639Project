// ui/settings/SettingsScreen.kt
@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.nutritiontracker.ui.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.ui.components.HeaderSection
import com.example.nutritiontracker.ui.theme.GrayBackground

@Composable
fun SettingsScreen(
    onCustomizeClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground)
    ) {
        HeaderSection(
            title = "Settings",
            showSettings = false
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PersonalInformationSection()
            PhysicalDetailsSection()
            HealthGoalsSection()
        }
    }
}

@Composable
fun PersonalInformationSection() {
    var expanded by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var genderExpanded by remember { mutableStateOf(false) }

    val genderOptions = listOf("Male", "Female", "Other", "Prefer not to say")

    ExpandableSection(
        title = "Personal Information",
        expanded = expanded,
        onExpandToggle = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color(0xFFCBD5E1)
                )
            )

            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color(0xFFCBD5E1)
                )
            )

            ExposedDropdownMenuBox(
                expanded = genderExpanded,
                onExpandedChange = { genderExpanded = !genderExpanded }
            ) {
                OutlinedTextField(
                    value = selectedGender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Gender") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color(0xFFCBD5E1)
                    )
                )
                ExposedDropdownMenu(
                    expanded = genderExpanded,
                    onDismissRequest = { genderExpanded = false }
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedGender = option
                                genderExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PhysicalDetailsSection() {
    var expanded by remember { mutableStateOf(false) }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var bodyFat by remember { mutableStateOf("") }
    var selectedActivityLevel by remember { mutableStateOf("") }
    var activityLevelExpanded by remember { mutableStateOf(false) }

    val activityLevels = listOf(
        "Sedentary (little or no exercise)",
        "Lightly Active (1-3 days/week)",
        "Moderately Active (3-5 days/week)",
        "Very Active (6-7 days/week)",
        "Extra Active (very intense exercise)"
    )

    ExpandableSection(
        title = "Physical Details",
        expanded = expanded,
        onExpandToggle = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = height,
                onValueChange = { height = it },
                label = { Text("Height (cm)") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color(0xFFCBD5E1)
                )
            )

            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight (kg)") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color(0xFFCBD5E1)
                )
            )

            OutlinedTextField(
                value = bodyFat,
                onValueChange = { bodyFat = it },
                label = { Text("Body Fat (%)") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color(0xFFCBD5E1)
                )
            )

            ExposedDropdownMenuBox(
                expanded = activityLevelExpanded,
                onExpandedChange = { activityLevelExpanded = !activityLevelExpanded }
            ) {
                OutlinedTextField(
                    value = selectedActivityLevel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Activity Level") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = activityLevelExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color(0xFFCBD5E1)
                    )
                )
                ExposedDropdownMenu(
                    expanded = activityLevelExpanded,
                    onDismissRequest = { activityLevelExpanded = false }
                ) {
                    activityLevels.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option, style = MaterialTheme.typography.bodyMedium) },
                            onClick = {
                                selectedActivityLevel = option
                                activityLevelExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HealthGoalsSection() {
    var expanded by remember { mutableStateOf(false) }
    var selectedGoalType by remember { mutableStateOf("") }
    var goalTypeExpanded by remember { mutableStateOf(false) }
    var selectedDiet by remember { mutableStateOf("") }
    var dietExpanded by remember { mutableStateOf(false) }
    var allergies by remember { mutableStateOf("") }

    val goalTypes = listOf(
        "Weight Loss",
        "Weight Gain",
        "Muscle Building",
        "Maintain Weight",
        "General Health"
    )

    val dietTypes = listOf(
        "No Restrictions",
        "Vegetarian",
        "Vegan",
        "Keto",
        "Paleo",
        "Mediterranean",
        "Low Carb",
        "Gluten Free"
    )

    ExpandableSection(
        title = "Health Goals",
        expanded = expanded,
        onExpandToggle = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = goalTypeExpanded,
                onExpandedChange = { goalTypeExpanded = !goalTypeExpanded }
            ) {
                OutlinedTextField(
                    value = selectedGoalType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Goal Type") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = goalTypeExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color(0xFFCBD5E1)
                    )
                )
                ExposedDropdownMenu(
                    expanded = goalTypeExpanded,
                    onDismissRequest = { goalTypeExpanded = false }
                ) {
                    goalTypes.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedGoalType = option
                                goalTypeExpanded = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = dietExpanded,
                onExpandedChange = { dietExpanded = !dietExpanded }
            ) {
                OutlinedTextField(
                    value = selectedDiet,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Diet Type") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = dietExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color(0xFFCBD5E1)
                    )
                )
                ExposedDropdownMenu(
                    expanded = dietExpanded,
                    onDismissRequest = { dietExpanded = false }
                ) {
                    dietTypes.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedDiet = option
                                dietExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = allergies,
                onValueChange = { allergies = it },
                label = { Text("Allergies (comma separated)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color(0xFFCBD5E1)
                )
            )
        }
    }
}

@Composable
fun ExpandableSection(
    title: String,
    expanded: Boolean,
    onExpandToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpandToggle() }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1E293B)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                   // modifier = Modifier.rotate(if (expanded) 180f else 0f),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                content()
            }
        }
    }
}