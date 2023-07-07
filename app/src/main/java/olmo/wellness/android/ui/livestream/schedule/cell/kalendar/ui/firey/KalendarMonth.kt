package olmo.wellness.android.ui.livestream.schedule.cell.kalendar.ui.firey

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.himanshoe.kalendar.common.KalendarKonfig
import com.himanshoe.kalendar.common.KalendarSelector
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarEvent
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarMoney
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.theme.Grid
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.ui.common.KalendarWeekDayNames
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.util.validateMaxDate
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.util.validateMinDate
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


private const val DAYS_IN_WEEK = 7

@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun KalendarMonth(
    selectedDay: LocalDate,
    yearMonth: YearMonth = YearMonth.now(),
    kalendarKonfig: KalendarKonfig,
    onCurrentDayClick: (LocalDate, KalendarEvent?, KalendarMoney?) -> Unit,
    errorMessageLogged: (String) -> Unit,
    kalendarSelector: KalendarSelector,
    kalendarEvents: List<KalendarEvent>,
    kalendarMoneys: List<KalendarMoney>,
    isBack: Boolean=true,
    isDrop : Boolean=true,
    onBackMonth :((startMonth : LocalDate, endMonth : LocalDate)->Unit)?=null,
    onBack: () -> Unit,
    onDrop: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Grid.Two),
//        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        val haptic = LocalHapticFeedback.current

        val monthState = remember {
            mutableStateOf(yearMonth)
        }
        val clickedDay = remember {
            mutableStateOf(selectedDay)
        }
        val formatter = DateTimeFormatter.ofPattern("MMMM, yyyy", kalendarKonfig.locale)

        LaunchedEffect(Unit){
            changeMonth(monthState, onBackMonth)
        }

        KalendarHeader(
            kalendarSelector = kalendarSelector,
            text = monthState.value.format(formatter),
            isBack = isBack,
            isDrop = isDrop,
            onPreviousMonthClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                val year = monthState.value.year
                val isLimitAttached = year.validateMinDate(kalendarKonfig.yearRange.min)
                if (isLimitAttached) {
                    monthState.value = monthState.value.minusMonths(1)
                    changeMonth(monthState, onBackMonth)
                } else {
                    errorMessageLogged("Minimum year limit reached")
                }
            },
            onNextMonthClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                val year = monthState.value.year
                val isLimitAttached = year.validateMaxDate(kalendarKonfig.yearRange.max)
                if (isLimitAttached) {
                    monthState.value = monthState.value.plusMonths(1)
                    changeMonth(monthState, onBackMonth)
                } else {
                    errorMessageLogged("Minimum year limit reached")
                }
            },
            onBack = onBack,
            onDrop = onDrop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Grid.OneHalf)
                .background(color = kalendarSelector.todayColor, shape = RoundedCornerShape(30.dp))
                .padding(10.dp)
        ) {

            KalendarWeekDayNames(
                kalendarKonfig = kalendarKonfig,
                kalendarSelector = kalendarSelector
            )

            val days: List<LocalDate> = getDays(monthState)

            days.chunked(DAYS_IN_WEEK).forEach { weekDays ->
                BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                    val size = (maxWidth / DAYS_IN_WEEK)
                    Row(horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                        weekDays.forEach { localDate ->
                            val isFromCurrentMonth = YearMonth.from(localDate) == monthState.value
                            if (isFromCurrentMonth) {
                                val isSelected = monthState.value.month == clickedDay.value.month &&
                                        monthState.value.year == clickedDay.value.year &&
                                        localDate == clickedDay.value

                                KalendarDay(
                                    size = size,
                                    date = localDate,
                                    isSelected = isSelected,
                                    isToday = localDate == LocalDate.now(),
                                    kalendarSelector = kalendarSelector,
                                    kalendarEvents = kalendarEvents,
                                    kalendarMoneys = kalendarMoneys,
                                    onDayClick = { date, event ->
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        clickedDay.value = date
                                        val calendarMoney = kalendarMoneys.firstOrNull{ it.date == date}
                                        onCurrentDayClick(date, event, calendarMoney)
                                    }
                                )
                            } else {
                                KalendarEmptyDay(modifier = Modifier.size(size))
                            }
                        }
                    }
                }
            }

        }
    }
}

@SuppressLint("NewApi")
private fun changeMonth(
    monthState: MutableState<YearMonth>,
    onBackMonth: ((startMonth: LocalDate, endMonth: LocalDate) -> Unit)?,
) {
    val start = monthState.value.atDay(1)
    val end = monthState.value.atEndOfMonth()
    onBackMonth?.invoke(start, end)
}

@RequiresApi(Build.VERSION_CODES.O)
private fun getDays(monthState: MutableState<YearMonth>): List<LocalDate> {
    return mutableListOf<LocalDate>().apply {
        val firstDay = monthState.value.atDay(1)
        val firstSunday = if (firstDay.dayOfWeek == java.time.DayOfWeek.SUNDAY) {
            firstDay
        } else {
            firstDay.minusDays(firstDay.dayOfWeek.value.toLong())
        }
        repeat(6) { weekIndex ->
            (0..6).forEach { dayIndex ->
                add(firstSunday.plusDays((7 * weekIndex + dayIndex).toLong()))
            }
        }
    }
}
