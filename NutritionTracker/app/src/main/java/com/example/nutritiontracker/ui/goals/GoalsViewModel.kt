package com.example.nutritiontracker.ui.goals

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutritiontracker.data.RDIRequirements
import com.example.nutritiontracker.data.SettingsRepository
import com.example.nutritiontracker.data.goals.GoalsRepository
import com.example.nutritiontracker.data.local.NutritionDatabase
import kotlinx.coroutines.launch

/* DATA MODELS FOR GOALS UI */

data class DailyGoalsUi(
    val caloriesCurrent: Int,
    val caloriesTarget: Int,
    val progressToCalorieGoal: Float
)

data class WeeklyDayUi(
    val label: String,
    val percentOfGoal: Float
)

data class WeeklyGoalsUi(
    val days: List<WeeklyDayUi>
)

data class MonthlyWeekUi(
    val label: String,
    val daysMetGoal: Int
)

data class MonthlyGoalsUi(
    val totalDaysTracked: Int,
    val daysMetGoal: Int,
    val successRatePercent: Int,
    val weeks: List<MonthlyWeekUi>
)

data class GoalsUiState(
    val daily: DailyGoalsUi,
    val weekly: WeeklyGoalsUi,
    val monthly: MonthlyGoalsUi
)

/**
 * ViewModel that connects Daily, Weekly, and Monthly goals.
 * - RDI calories come from SettingsRepository + RDICalculator
 *   (same values as Your RDI screen).
 * - Daily logs come from Room via GoalsRepository.
 */
class GoalsViewModel(application: Application) : AndroidViewModel(application) {

    // Internal model used only for calculations
    private data class DailyLog(
        val dayIndex: Int,
        val caloriesConsumed: Int,
        val caloriesTarget: Int
    )

    private val goalsRepository: GoalsRepository
    private val settingsRepository: SettingsRepository

    private val _uiState = mutableStateOf(
        GoalsUiState(
            daily = DailyGoalsUi(
                caloriesCurrent = 0,
                caloriesTarget = 0,
                progressToCalorieGoal = 0f
            ),
            weekly = WeeklyGoalsUi(days = emptyList()),
            monthly = MonthlyGoalsUi(
                totalDaysTracked = 0,
                daysMetGoal = 0,
                successRatePercent = 0,
                weeks = emptyList()
            )
        )
    )
    val uiState: State<GoalsUiState> = _uiState

    init {
        val appContext = application.applicationContext
        val db = NutritionDatabase.getInstance(appContext)
        goalsRepository = GoalsRepository(db.dailyLogDao())
        settingsRepository = SettingsRepository(appContext)

        viewModelScope.launch {
            // 1) Get RDI requirements using the SAME logic as Your RDI screen
            val rdi: RDIRequirements = settingsRepository.getCurrentRdiRequirements()
            val calorieTarget = rdi.calories

            // 2) Get last up-to-31 days of REAL logs from DB
            val entities = goalsRepository
                .getLastDailyLogs(maxDays = 31)
                .sortedBy { it.date }  // date is String in DailyLogEntity

            val logs: List<DailyLog> = entities.mapIndexed { index, e ->
                DailyLog(
                    dayIndex = index,
                    caloriesConsumed = e.caloriesConsumed,
                    caloriesTarget = calorieTarget           // always compare to current RDI
                )
            }

            _uiState.value = if (logs.isEmpty()) {
                buildEmptyState(calorieTarget)
            } else {
                buildUiStateFromLogs(logs)
            }
        }
    }

    // When there are no logs yet, show RDI as target but 0 as current
    private fun buildEmptyState(calorieTarget: Int): GoalsUiState {
        val daily = DailyGoalsUi(
            caloriesCurrent = 0,
            caloriesTarget = calorieTarget,
            progressToCalorieGoal = 0f
        )

        return GoalsUiState(
            daily = daily,
            weekly = WeeklyGoalsUi(days = emptyList()),
            monthly = MonthlyGoalsUi(
                totalDaysTracked = 0,
                daysMetGoal = 0,
                successRatePercent = 0,
                weeks = emptyList()
            )
        )
    }

    /* AGGREGATION LOGIC */

    private fun buildUiStateFromLogs(logs: List<DailyLog>): GoalsUiState {
        require(logs.isNotEmpty())

        val latest = logs.last()
        val dailyUi = DailyGoalsUi(
            caloriesCurrent = latest.caloriesConsumed,
            caloriesTarget = latest.caloriesTarget,
            progressToCalorieGoal =
                if (latest.caloriesTarget > 0) {
                    latest.caloriesConsumed.toFloat() / latest.caloriesTarget
                } else 0f
        )

        val weeklyUi = buildWeeklyUi(logs)
        val monthlyUi = buildMonthlyUi(logs)

        return GoalsUiState(
            daily = dailyUi,
            weekly = weeklyUi,
            monthly = monthlyUi
        )
    }

    private fun buildWeeklyUi(logs: List<DailyLog>): WeeklyGoalsUi {
        val last7 = logs.takeLast(7)
        val startIndex = logs.size - last7.size
        val labels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        val days = last7.mapIndexed { index, log ->
            val percent =
                if (log.caloriesTarget > 0) {
                    (log.caloriesConsumed.toFloat() / log.caloriesTarget.toFloat()) * 100f
                } else 0f

            WeeklyDayUi(
                label = labels.getOrNull(index) ?: "D${startIndex + index + 1}",
                percentOfGoal = percent
            )
        }

        return WeeklyGoalsUi(days = days)
    }

    private fun buildMonthlyUi(logs: List<DailyLog>): MonthlyGoalsUi {
        val lastDays = logs.takeLast(31)
        val totalDays = lastDays.size
        if (totalDays == 0) {
            return MonthlyGoalsUi(
                totalDaysTracked = 0,
                daysMetGoal = 0,
                successRatePercent = 0,
                weeks = emptyList()
            )
        }

        // A day "meets goal" if intake is 90–110% of target
        fun DailyLog.goalMet(): Boolean {
            if (caloriesTarget <= 0) return false
            val ratio = caloriesConsumed.toFloat() / caloriesTarget.toFloat()
            return ratio in 0.9f..1.1f
        }

        val daysMet = lastDays.count { it.goalMet() }
        val successRate = (daysMet * 100f / totalDays).toInt()

        val chunks = lastDays.chunked(7)
        val weeks = chunks.mapIndexed { index, weekLogs ->
            MonthlyWeekUi(
                label = "Week ${index + 1}",
                daysMetGoal = weekLogs.count { it.goalMet() }
            )
        }

        return MonthlyGoalsUi(
            totalDaysTracked = totalDays,
            daysMetGoal = daysMet,
            successRatePercent = successRate,
            weeks = weeks
        )
    }
}