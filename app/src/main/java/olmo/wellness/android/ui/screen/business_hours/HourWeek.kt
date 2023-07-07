package olmo.wellness.android.ui.screen.business_hours

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import olmo.wellness.android.ui.common.picker.time.ui.Hours

data class HourWeek(
    val enable: MutableState<Boolean>, val dayName: String,
    var hourStart: MutableState<Hours>? = null,
    var hourEnd: MutableState<Hours>? = null,
    var hourStartEnable: MutableState<Boolean> = mutableStateOf(false),
    var hourEndEnable: MutableState<Boolean> = mutableStateOf(false),
)

var createHourWeeks = mutableListOf<HourWeek>().apply {
    add(HourWeek(enable = mutableStateOf(false), dayName = "Mon"))
    add(HourWeek(enable = mutableStateOf(false), dayName = "Tue"))
    add(HourWeek(enable = mutableStateOf(false), dayName = "Wed"))
    add(HourWeek(enable = mutableStateOf(false), dayName = "Thu"))
    add(HourWeek(enable = mutableStateOf(false), dayName = "Fri"))
    add(HourWeek(enable = mutableStateOf(false), dayName = "Sat"))
    add(HourWeek(enable = mutableStateOf(false), dayName = "Sun"))
}