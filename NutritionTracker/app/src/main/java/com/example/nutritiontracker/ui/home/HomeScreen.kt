// ui/home/HomeScreen.kt
package com.example.nutritiontracker.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.ui.components.HeaderSection
import com.example.nutritiontracker.ui.theme.GrayBackground
import com.example.nutritiontracker.ui.theme.GreenPrimary
import com.example.nutritiontracker.ui.theme.TextPrimary
import com.example.nutritiontracker.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit = {}   // gets callback from NutritionApp
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

        Spacer(Modifier.height(16.dp))

        TodayProgressCard()
        Spacer(Modifier.height(24.dp))
        AddFoodSection()
        Spacer(Modifier.height(24.dp))
        FoodActionsRow()
        Spacer(Modifier.height(24.dp))
        TodaysLogCard()
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
fun AddFoodSection() {
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
            onClick = { /* TODO: open scan screen */ },
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
fun FoodActionsRow() {
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
            modifier = Modifier.weight(1f)
        )
        FoodActionCard(
            title = "Manual Entry",
            subtitle = "Enter facts",
            icon = Icons.Filled.Description,
            modifier = Modifier.weight(1f)
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
fun TodaysLogCard() {
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
                    text = "340 kcal",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            Spacer(Modifier.height(8.dp))
            Divider(color = Color(0xFFE3E5ED), thickness = 1.dp)
            Spacer(Modifier.height(12.dp))

            LogItemRow(
                title = "Greek Yogurt with",
                subtitle = "1 cup",
                kcal = "180 kcal",
                macros = "P: 15g · C: 25g · F: 3g"
            )
            Spacer(Modifier.height(12.dp))
            LogItemRow(
                title = "Whole Grain",
                subtitle = "2 slices",
                kcal = "160 kcal",
                macros = "P: 8g · C: 28g · F: 2g"
            )
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