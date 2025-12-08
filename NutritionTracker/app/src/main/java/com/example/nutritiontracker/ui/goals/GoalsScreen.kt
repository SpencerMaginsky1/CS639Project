// ui/goals/GoalsScreen.kt
package com.example.nutritiontracker.ui.goals

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.ui.components.HeaderSection
import com.example.nutritiontracker.ui.theme.GrayBackground
import com.example.nutritiontracker.ui.theme.GreenPrimary
import com.example.nutritiontracker.ui.theme.TextPrimary
import com.example.nutritiontracker.ui.theme.TextSecondary

/* TABS MODEL */

private enum class GoalsTab(val label: String) {
    Daily("Daily Goals"),
    Weekly("Weekly Goals"),
    Monthly("Monthly Goals")
}

/*  ROOT SCREEN  */

@Composable
fun GoalsScreen(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(GoalsTab.Daily) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GrayBackground)
    ) {
        // Green header
        HeaderSection(
            title = "Goals",
            onSettingsClick = onSettingsClick
        )

        Spacer(Modifier.height(16.dp))

        // Pill navigation directly under header (always visible)
        GoalsTabSwitcher(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )

        Spacer(Modifier.height(16.dp))

        // Scrollable content below the pill nav.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 96.dp)
        ) {
            when (selectedTab) {
                GoalsTab.Daily   -> DailyGoalsContent()
                GoalsTab.Weekly  -> WeeklyGoalsContent()
                GoalsTab.Monthly -> MonthlyGoalsContent()
            }

            Spacer(Modifier.height(24.dp))

            GoalsProgressSummary(selectedTab)

            Spacer(Modifier.height(32.dp))
        }
    }
}

/* PROGRESS SUMMARY */

@Composable
private fun GoalsProgressSummary(selectedTab: GoalsTab) {
    val text = when (selectedTab) {
        GoalsTab.Daily   -> "Daily goals progress overview"
        GoalsTab.Weekly  -> "Weekly intake vs daily goals"
        GoalsTab.Monthly -> "Monthly consistency overview"
    }

    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = TextSecondary,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    )
}

/*  TAB SWITCHER  */

@Composable
private fun GoalsTabSwitcher(
    selectedTab: GoalsTab,
    onTabSelected: (GoalsTab) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            GoalsTab.values().forEach { tab ->
                val isSelected = tab == selectedTab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .padding(horizontal = 4.dp)
                        .background(
                            color = if (isSelected) GreenPrimary else Color.Transparent,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .clickable { onTabSelected(tab) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab.label.substringBefore(" "),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isSelected) Color.White else TextPrimary,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}


@Composable
private fun CircularRing(
    progress: Float,            // 0f..1f
    ringColor: Color,           // dark arc (progress)
    backgroundColor: Color,     // light track (100%)
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 10.dp
) {
    Canvas(modifier = modifier) {
        // Full 360° circle. 0° is at 3 o'clock, so we start at -90°
        // to have the ring start at the top.
        val sweepAngleMax = 360f
        val startAngle = -90f

        val stroke = Stroke(
            width = strokeWidth.toPx(),
            cap = StrokeCap.Round
        )

        // Background track = full circle (represents 100%)
        drawArc(
            color = backgroundColor,
            startAngle = startAngle,
            sweepAngle = sweepAngleMax,
            useCenter = false,
            style = stroke
        )

        // Foreground progress arc = progress % of full circle
        drawArc(
            color = ringColor,
            startAngle = startAngle,
            sweepAngle = sweepAngleMax * progress.coerceIn(0f, 1f),
            useCenter = false,
            style = stroke
        )
    }
}

/* DAILY GOALS */

@Composable
private fun DailyGoalsContent() {
    // TODO: Replace all placeholder goal + intake values with real user data:
    //  - Pull today's intake from DB / API (logged meals + nutrients)
    //  - Pull RDI / target values from the RDI data layer
    //  - Compute progress = current / target for each ring + RDI bar

    // Main goals card (all values are placeholder data)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Daily Goals",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )

            Spacer(Modifier.height(16.dp))

            // Top three big rings
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BigRingStat(
                    value = "1850",
                    unit = "kcal",
                    label = "Calories",
                    subtitle = "1850 / 2500 kcal",
                    progress = 1850f / 2500f
                )
                BigRingStat(
                    value = "3",
                    unit = "",
                    label = "Fruits",
                    subtitle = "3 / 4\nservings",
                    progress = 3f / 4f
                )
                BigRingStat(
                    value = "4",
                    unit = "",
                    label = "Veggies",
                    subtitle = "4 / 5\nservings",
                    progress = 4f / 5f
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Macronutrients",
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SmallRingStat(
                    label = "Protein",
                    value = "78/120",
                    unit = "g",
                    ringColor = Color(0xFF5C9DFF),
                    progress = 78f / 120f
                )
                SmallRingStat(
                    label = "Carbs",
                    value = "195/280",
                    unit = "g",
                    ringColor = Color(0xFFFFB74D),
                    progress = 195f / 280f
                )
                SmallRingStat(
                    label = "Fat",
                    value = "52/70",
                    unit = "g",
                    ringColor = Color(0xFFBA68C8),
                    progress = 52f / 70f
                )
            }

            Spacer(Modifier.height(20.dp))
            Divider(color = Color(0xFFE5E7F0))
            Spacer(Modifier.height(16.dp))

            Text(
                text = "Today's Intake vs RDI",
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )

            Spacer(Modifier.height(16.dp))

            RdiProgressRow("Fiber",     "18/30",       18f / 30f,      GreenPrimary)
            Spacer(Modifier.height(10.dp))
            RdiProgressRow("Vitamin C", "65/90",       65f / 90f,      Color(0xFFFFA726))
            Spacer(Modifier.height(10.dp))
            RdiProgressRow("Vitamin D", "12/20",       12f / 20f,      Color(0xFFAB47BC))
            Spacer(Modifier.height(10.dp))
            RdiProgressRow("Calcium",   "750/1000 mg", 750f / 1000f,   Color(0xFF42A5F5))
            Spacer(Modifier.height(10.dp))
            RdiProgressRow("Iron",      "11/18 mg",    11f / 18f,      Color(0xFFE53935))
        }
    }

    Spacer(Modifier.height(16.dp))

    // “Great Progress!” card – 74% is a placeholder for now.
    // TODO: Replace 74% with a real value from your data layer (e.g., ViewModel).
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(Color(0xFFF1FAF5)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowUpward,
                    contentDescription = null,
                    tint = GreenPrimary
                )
            }

            Spacer(Modifier.width(16.dp))

            Column {
                Text(
                    text = "Great Progress!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
                Text(
                    text = "You're 74% to your calorie goal", // placeholder
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
    }
}

/*  DAILY HELPERS */

@Composable
private fun BigRingStat(
    value: String,
    unit: String,
    label: String,
    subtitle: String,
    progress: Float
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(96.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularRing(
                progress = progress,                     // e.g. 2f/4f = half ring
                ringColor = GreenPrimary,               // dark arc
                backgroundColor = Color(0xFFE0F5EA),    // light track
                strokeWidth = 12.dp,
                modifier = Modifier.fillMaxSize()       // ensure Canvas has size
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                if (unit.isNotBlank()) {
                    Text(
                        text = unit,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(label, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SmallRingStat(
    label: String,
    value: String,
    unit: String,
    ringColor: Color,
    progress: Float
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(72.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularRing(
                progress = progress,
                ringColor = ringColor,                              // dark arc
                backgroundColor = ringColor.copy(alpha = 0.18f),    // light track
                strokeWidth = 8.dp,
                modifier = Modifier.fillMaxSize()                   // ensure Canvas has size
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
                Text(
                    text = unit,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(label, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
    }
}

@Composable
private fun RdiProgressRow(
    label: String,
    current: String,
    progress: Float,
    barColor: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
            Text(current, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(Color(0xFFEFF1F5), RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .background(barColor, RoundedCornerShape(4.dp))
            )
        }
    }
}

/*  WEEKLY GOALS  */

@Composable
private fun WeeklyGoalsContent() {
    // Main weekly card
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Title + “This Week” chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Weekly Performance",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Total nutrient intake vs daily goals",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
                WeeklyRangeChip()
            }

            Spacer(Modifier.height(16.dp))

            WeeklyPerformanceChart()

            Spacer(Modifier.height(16.dp))

            // Legend – slightly bigger dots & spacing
            LegendDotRow(color = Color(0xFF22A865), label = "Goal Met (100%+)")
            Spacer(Modifier.height(6.dp))
            LegendDotRow(color = Color(0xFFFF9800), label = "Close (80–99%)")
            Spacer(Modifier.height(6.dp))
            LegendDotRow(color = Color(0xFFE53935), label = "Below Target (<80%)")

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun WeeklyRangeChip() {
    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color(0xFFE0E3EE),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowUpward,
            contentDescription = null,
            tint = GreenPrimary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = "This Week",
            style = MaterialTheme.typography.bodySmall,
            color = TextPrimary
        )
    }
}

@Composable
private fun WeeklyPerformanceChart() {
    // TODO: Replace placeholder weekly percentages with real values computed from:
    //  - Aggregated daily intake (sum of nutrients per day)
    //  - Daily goals / RDI targets
    //  - percent = totalForDay / dailyTarget

    val percentages = listOf(
        88f,  // Mon – close
        95f,  // Tue – close / near goal
        75f,  // Wed – below
        110f, // Thu – above goal (green)
        92f,  // Fri – close
        90f,  // Sat – close
        70f   // Sun – below
    )
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    // Y-axis scale: 0 → 0%, 120 → 120%
    val maxPercent = 120f
    val targetPercent = 95f          // dashed goal line position
    val maxBarHeight = 140.dp

    val metGreen    = Color(0xFF22A865)
    val closeOrange = Color(0xFFFF9800)
    val belowRed    = Color(0xFFE53935)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(maxBarHeight + 32.dp) // space for dashed line + labels
            .padding(top = 4.dp)
    ) {
        // LEFT: Y-axis with 0–120 labels
        WeeklyYAxis()

        Spacer(Modifier.width(8.dp))

        // RIGHT: chart area (bars + dashed target line)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            // dashed goal line at ~95% of scale
            val targetFraction = (targetPercent / maxPercent).coerceIn(0f, 1f)

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = -(maxBarHeight * targetFraction))
                    .fillMaxWidth()
            ) {
                DashedTargetLine(
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                percentages.forEachIndexed { index, percent ->
                    val fractionOfScale =
                        (percent / maxPercent).coerceIn(0f, 1f)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        // full-scale box + inner bar
                        Box(
                            modifier = Modifier
                                .height(maxBarHeight)
                                .width(26.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .height(maxBarHeight * fractionOfScale)
                                    .fillMaxWidth()
                                    .background(
                                        color = when {
                                            percent >= 100f -> metGreen
                                            percent >= 80f  -> closeOrange
                                            else            -> belowRed
                                        },
                                        shape = RoundedCornerShape(
                                            topStart = 8.dp,
                                            topEnd = 8.dp
                                        )
                                    )
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = days[index],
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeeklyYAxis() {

    val labels = listOf(0, 30, 60, 90, 120)

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(start = 4.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.End
    ) {
        // top → bottom: 120, 90, 60, 30, 0
        labels.reversed().forEach { value ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Spacer(Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .width(10.dp)
                        .height(1.dp)
                        .background(Color(0xFFCFD8DC))
                )
            }
        }
    }
}

@Composable
private fun DashedTargetLine(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(16) {
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .height(3.dp)
                    .background(
                        color = Color(0xFFB0BEC5).copy(alpha = 0.9f),
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }
    }
}

@Composable
private fun LegendDotRow(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}

/*  MONTHLY GOALS  */

@Composable
private fun MonthlyGoalsContent() {
    // TODO:
    //  - Replace ALL placeholder monthly values with real data:
    //      * totalDaysTrackedThisMonth (e.g., 22/28)
    //      * successRate = daysMetGoal / totalDays (e.g., 79%)
    //      * daysPerWeek list (Week 1–4) computed from the user's logs
    //  - Pull this from the same data layer as Daily/Weekly:
    //      * user input (logged goals met / not met)
    //      * RDI / target configuration
    //      * any API-backed nutrition data if needed

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {

            /* HEADER */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Monthly Consistency",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Days per week you met your daily goal",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
                MonthlyRangeChip()
            }

            Spacer(Modifier.height(12.dp))

            /* SUMMARY CARD */
            // TODO: Replace with real monthly aggregate data.
            MonthlySummaryCard(
                totalDaysText = "22/28",
                successRateText = "79%"
            )

            Spacer(Modifier.height(16.dp))

            /* CHART */
            MonthlyConsistencyChart()

            Spacer(Modifier.height(16.dp))

            /* LEGEND */
            MonthlyLegendItem(
                color = Color(0xFF7C4DFF),
                label = "Perfect (7 days)"
            )
            Spacer(Modifier.height(4.dp))

            MonthlyLegendItem(
                color = Color(0xFF9575CD),
                label = "Great (5–6 days)"
            )
            Spacer(Modifier.height(4.dp))

            MonthlyLegendItem(
                color = Color(0xFFB39DDB),
                label = "Good (3–4 days)"
            )
            Spacer(Modifier.height(4.dp))

            MonthlyLegendItem(
                color = Color(0xFFD1C4E9),
                label = "Needs Work (<3 days)"
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}


/* MONTHLY SUBCOMPONENTS */

@Composable
private fun MonthlyRangeChip() {
    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color(0xFFE0E3EE),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowUpward,
            contentDescription = null,
            tint = GreenPrimary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = "This Month",
            style = MaterialTheme.typography.bodySmall,
            color = TextPrimary
        )
    }
}

@Composable
private fun MonthlySummaryCard(
    totalDaysText: String,
    successRateText: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFF3F2FF),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Total Days",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Text(
                    text = totalDaysText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Success Rate",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Text(
                    text = successRateText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF7C4DFF)
                )
            }
        }
    }
}

@Composable
private fun MonthlyLegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, CircleShape)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}


/* MONTHLY Y-AXIS */

@Composable
private fun MonthlyYAxis(maxDays: Int) {
    // Display a clean vertical axis from 0..7
    val labels = (0..maxDays).toList()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(start = 4.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.End
    ) {
        labels.reversed().forEach { value ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = value.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Spacer(Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .width(10.dp)
                        .height(1.dp)
                        .background(Color(0xFFCFD8DC))
                )
            }
        }
    }
}


/* MONTHLY BAR CHART */

@Composable
private fun MonthlyConsistencyChart() {

    // TODO:
    //  Replace with REAL values based on user logs (days they met their daily goal).
    val daysPerWeek = listOf(5, 6, 4, 7)
    val weeks = listOf("Week 1", "Week 2", "Week 3", "Week 4")

    val maxDays = 7f
    val targetDays = 7f
    val maxBarHeight = 140.dp

    val barColors = listOf(
        Color(0xFF7C4DFF),  // Week 1
        Color(0xFF9575CD),  // Week 2
        Color(0xFFB39DDB),  // Week 3
        Color(0xFF7E57C2)   // Week 4
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(maxBarHeight + 32.dp)
            .padding(top = 4.dp)
    ) {
        MonthlyYAxis(maxDays = maxDays.toInt())

        Spacer(Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {

            // dashed line (7 days target)
            val targetFraction = (targetDays / maxDays).coerceIn(0f, 1f)

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = -(maxBarHeight * targetFraction))
                    .fillMaxWidth()
            ) {
                DashedTargetLine(
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            // Bars
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {

                daysPerWeek.forEachIndexed { index, days ->
                    val fraction = (days / maxDays).coerceIn(0f, 1f)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .height(maxBarHeight)
                                .width(30.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .height(maxBarHeight * fraction)
                                    .fillMaxWidth()
                                    .background(
                                        color = barColors[index],
                                        shape = RoundedCornerShape(
                                            topStart = 10.dp,
                                            topEnd = 10.dp
                                        )
                                    )
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = weeks[index],
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}