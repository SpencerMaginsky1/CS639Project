// ui/home/HomeScreen.kt
package com.example.nutritiontracker.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.camera.CameraController
import com.example.nutritiontracker.ui.components.HeaderSection
import com.example.nutritiontracker.ui.theme.GrayBackground
import com.example.nutritiontracker.ui.theme.GreenPrimary
import com.example.nutritiontracker.ui.theme.TextPrimary
import com.example.nutritiontracker.ui.theme.TextSecondary
import com.example.nutritiontracker.R
import com.example.nutritiontracker.data.fdc.NutritionSummary


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    cameraController: CameraController,
    onScanClick: () -> Unit,
    onBarcodeEntered: (String) -> Unit,
    onManualEntry: (NutritionSummary) -> Unit,
    onSettingsClick: () -> Unit = {},   // gets callback from NutritionApp
    foodLog: List<NutritionSummary> = emptyList()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GrayBackground)
            .verticalScroll(rememberScrollState())
    ) {
        HeaderSection(
            title = "Nutrition Tracker",
            showSettings = true,
            onSettingsClick = onSettingsClick   // forwards to gear icon
        )

        Spacer(Modifier.height(8.dp))

        TodayProgressCard()
        Spacer(Modifier.height(24.dp))
        AddFoodSection(cameraController, onScanClick)
        Spacer(Modifier.height(24.dp))
        FoodActionsRow(onBarcodeEntered, onManualEntry)
        Spacer(Modifier.height(24.dp))
        TodaysLogCard(foodLog)
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun TodayProgressCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Today's Progress",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))

            NutrientProgressRow(
                label = "Macro-nutrients",
                valueText = "120 / 228 g",
                remainingText = "160 g remaining",
                progress = 120f / 228f,
                barColor = Color(0xFF4285F4)
            )
            Spacer(modifier = Modifier.height(12.dp))

            NutrientProgressRow(
                label = "Vitamins",
                valueText = "8200 / 9080 mg",
                remainingText = "880 mg remaining",
                progress = 8200f / 9080f,
                barColor = Color(0xFFF9A825)
            )
            Spacer(modifier = Modifier.height(12.dp))

            NutrientProgressRow(
                label = "Minerals",
                valueText = "525 / 608 mg",
                remainingText = "83 mg remaining",
                progress = 525f / 608f,
                barColor = Color(0xFFE53935)
            )
        }
    }
}

@Composable
private fun NutrientProgressRow(
    label: String,
    valueText: String,
    remainingText: String,
    progress: Float,
    barColor: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )
            Text(
                text = valueText,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = progress.coerceIn(0f, 1f),
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = barColor,
            trackColor = Color(0xFFEFF1F5)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = remainingText,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}

@Composable
fun AddFoodSection(cameraController: CameraController, onScanClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Add Food",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onScanClick() },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = RoundedCornerShape(32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.CameraAlt,
                    contentDescription = "Scan",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Scan Food",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Text(
                        text = "Use camera to scan barcode",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }
    }
}

@Composable
fun FoodActionsRow(onBarcodeEntered: (String) -> Unit, onManualEntry: (NutritionSummary) -> Unit) {
    var showEnterCodeDialog by remember { mutableStateOf(false) }
    var showManualEntryDialog by remember { mutableStateOf(false) }
    var codeInput by remember { mutableStateOf("") }

    var description by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var totalCarbs by remember { mutableStateOf("") }
    var totalFat by remember { mutableStateOf("") }
    var fiber by remember { mutableStateOf("") }
    var vitaminC by remember { mutableStateOf("") }
    var vitaminD by remember { mutableStateOf("") }
    var calcium by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FoodActionCard(
            title = "Enter Code",
            subtitle = "UPC/GTIN",
            icon = Icons.Filled.Tag,
            modifier = Modifier
                .weight(1f)
                .clickable{showEnterCodeDialog = true}
        )
        FoodActionCard(
            title = "Manual Entry",
            subtitle = "Enter facts",
            icon = Icons.Filled.Description,
            modifier = Modifier
                .weight(1f)
                .clickable{showManualEntryDialog = true}
        )
    }
    if(showEnterCodeDialog){
        AlertDialog(
            onDismissRequest = {
                showEnterCodeDialog = false
                codeInput = ""
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onBarcodeEntered(codeInput)
                        codeInput = ""
                        showEnterCodeDialog = false
                    },
                ){
                    Text("Submit")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showEnterCodeDialog = false
                        codeInput = ""
                    },
                ){
                    Text("Cancel")
                }
            },
            title = {
                Text("Enter UPC/GTIN")
                    },
            text = {
                Column {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        painter = painterResource(R.drawable.barcode_gtin_example),
                        contentDescription = "Barcode GTIN number example",
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Enter the Barcode number below:")
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = codeInput,
                        onValueChange = {codeInput = it},
                        placeholder = {"123456789123"}
                    )
                }
            },

        )
    }
    if(showManualEntryDialog){
        AlertDialog(
            onDismissRequest = {
                showManualEntryDialog = false
                description = ""
                calories = ""
                protein = ""
                totalCarbs = ""
                totalFat = ""
                fiber = ""
                vitaminC = ""
                vitaminD = ""
                calcium = ""
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val summary = NutritionSummary(
                            description = description,
                            calories = calories.toDoubleOrNull(),
                            protein = protein.toDoubleOrNull(),
                            totalCarbs = totalCarbs.toDoubleOrNull(),
                            totalFat = totalFat.toDoubleOrNull(),
                            fiber = fiber.toDoubleOrNull(),
                            vitaminC = vitaminC.toDoubleOrNull(),
                            vitaminD = vitaminD.toDoubleOrNull(),
                            calcium = calcium.toDoubleOrNull()
                        )
                        onManualEntry(summary)

                        description = ""
                        calories = ""
                        protein = ""
                        totalCarbs = ""
                        totalFat = ""
                        fiber = ""
                        vitaminC = ""
                        vitaminD = ""
                        calcium = ""

                        showManualEntryDialog = false
                    },
                ){
                    Text("Submit")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showManualEntryDialog = false
                        description = ""
                        calories = ""
                        protein = ""
                        totalCarbs = ""
                        totalFat = ""
                        fiber = ""
                        vitaminC = ""
                        vitaminD = ""
                        calcium = ""
                    },
                ){
                    Text("Cancel")
                }
            },
            title = {
                Text("Manual Entry")
            },
            text = {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Enter the Nutritional Facts Below:")
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = description,
                        onValueChange = {description = it},
                        label = {Text("Food Name")}
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = calories,
                        onValueChange = {calories = it},
                        label = {Text("Calories")}
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = protein,
                        onValueChange = {protein = it},
                        label = {Text("Protein")}
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = totalCarbs,
                        onValueChange = {totalCarbs = it},
                        label = {Text("Carbohydrates")}
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = totalFat,
                        onValueChange = {totalFat = it},
                        label = {Text("Total Fat")}
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = vitaminC,
                        onValueChange = {vitaminC = it},
                        label = {Text("VitaminC")}
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = vitaminD,
                        onValueChange = {vitaminD = it},
                        label = {Text("VitaminD")}
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = calcium,
                        onValueChange = {calcium = it},
                        label = {Text("Calcium")}
                    )
                }
            },
        )
    }
}

@Composable
fun FoodActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(96.dp)
            .border(1.dp, Color(0xFFE3E5ED), RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = TextPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}

@Composable
fun TodaysLogCard(foodLog: List<NutritionSummary> = emptyList()) {
    val totalCalories = foodLog.sumOf { it.calories ?: 0.0 }.toInt()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Today's Log",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
                Text(
                    text = "$totalCalories kcal",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            if (foodLog.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Divider(color = Color(0xFFE3E5ED), thickness = 1.dp)
                Spacer(Modifier.height(12.dp))

                foodLog.forEach { food ->
                    LogItemRow(
                        title = food.description ?: "Unknown Food",
                        subtitle = "1 serving",
                        kcal = "${food.calories?.toInt() ?: 0} kcal",
                        macros = "P: ${food.protein?.toInt() ?: 0}g · C: ${food.totalCarbs?.toInt() ?: 0}g · F: ${food.totalFat?.toInt() ?: 0}g"
                    )
                    Spacer(Modifier.height(12.dp))
                }
            } else {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "No food logged yet today",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun LogItemRow(
    title: String,
    subtitle: String,
    kcal: String,
    macros: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = kcal,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
                Text(
                    text = macros,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "Details",
                tint = TextSecondary
            )
        }
    }
}