package olmo.wellness.android.ui.livestream.schedule.cell.kalendar.ui.oceanic
/*
* MIT License
*
* Copyright (c) 2022 Himanshu Singh
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.himanshoe.kalendar.common.KalendarKonfig
import com.himanshoe.kalendar.common.KalendarSelector
import com.himanshoe.kalendar.ui.oceanic.KalendarOceanHeader
import com.himanshoe.kalendar.ui.oceanic.getNext7Dates
import com.himanshoe.kalendar.ui.oceanic.getPrevious7Dates
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarEvent
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarMoney
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.theme.Grid
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.theme.KalendarShape
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.ui.KalendarDot
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.util.validateMaxDate
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.util.validateMinDate
import olmo.wellness.android.ui.theme.marginMinimum
import java.time.LocalDate
import java.time.format.TextStyle
import kotlin.math.abs

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalPagerApi::class, ExperimentalSnapperApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun KalendarOceanWeek(
    startDate: LocalDate = LocalDate.now(),
    selectedDay: LocalDate = startDate,
    kalendarKonfig: KalendarKonfig,
    onCurrentDayClick: (LocalDate, KalendarEvent?, KalendarMoney?) -> Unit,
    errorMessageLogged: (String) -> Unit,
    kalendarSelector: KalendarSelector,
    kalendarEvents: List<KalendarEvent>,
    isBack: Boolean? = null,
    isDrop : Boolean=true,
    onBack: () -> Unit,
    onDrop: () -> Unit,
) {
    val isDot = kalendarSelector is KalendarSelector.Dot
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    val displayWeek = remember {
        mutableStateOf(getDisplayWeekCurrent())
    }


    val clickedDate = remember {
        mutableStateOf(selectedDay)
    }

    clickedDate.value = selectedDay

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        val size = ((maxWidth - 48.dp) / 7)
        val monthName = displayWeek.value.last().month.getDisplayName(
            TextStyle.FULL,
            kalendarKonfig.locale
        ) + ", " + displayWeek.value.last().year

        Column(Modifier.fillMaxWidth()) {
            KalendarOceanHeader(
                monthName,
                isBack,
                isDrop,
                kalendarSelector,
                onBack,
                onDrop
            )

            UiWeek(
                scope,
                haptic,
                kalendarKonfig,
                errorMessageLogged,
                clickedDate,
                kalendarEvents,
                size,
                isDot,
                kalendarSelector,
                onCurrentDayClick,
                onChangeWeek = {
                    displayWeek.value = it
                }
            )
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition", "MutableCollectionMutableState")
@OptIn(ExperimentalPagerApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun UiWeek(
    scope: CoroutineScope,
    haptic: HapticFeedback,
    kalendarKonfig: KalendarKonfig,
    errorMessageLogged: (String) -> Unit,
    clickedDate: MutableState<LocalDate>,
    kalendarEvents: List<KalendarEvent>,
    size: Dp,
    isDot: Boolean,
    kalendarSelector: KalendarSelector,
    onCurrentDayClick: (LocalDate, KalendarEvent?, KalendarMoney?) -> Unit,
    onChangeWeek: (List<LocalDate>) -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = 50)

    val oldDate = remember {
        mutableStateOf(clickedDate.value)
    }

    val listAllWeek by remember {
        mutableStateOf<MutableList<List<LocalDate>>>(mutableListOf())
    }

    val isFirst = remember {
        mutableStateOf(true)
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (listAllWeek.isNotEmpty()) {
                oldDate.value = clickedDate.value
                onChangeWeek.invoke(listAllWeek[page])
            }
        }
    }

    LaunchedEffect(isFirst) {
        for (i in 0 until 104) {
            var tempDisplayWeek = getDisplayWeekCurrent()
            if (i > 50) {
                var firstDayOfWeek = tempDisplayWeek.first()
                firstDayOfWeek = firstDayOfWeek.plusDays((7 * (i - 50)).toLong())
                val isLimitAttached =
                    firstDayOfWeek.year.validateMinDate(kalendarKonfig.yearRange.min)
                if (isLimitAttached) {
                    tempDisplayWeek = firstDayOfWeek.getNext7Dates()
                } else {
                    errorMessageLogged("Minimum year limit reached")
                }
            } else if (i < 50) {
                var lastDayOfWeek = tempDisplayWeek.last()
                lastDayOfWeek = lastDayOfWeek.minusDays(7 * (50 - i).toLong())
                val isLimitAttached =
                    lastDayOfWeek.year.validateMaxDate(kalendarKonfig.yearRange.max)
                if (isLimitAttached) {
                    tempDisplayWeek = lastDayOfWeek.getPrevious7Dates()
                } else {
                    errorMessageLogged("Maximum year limit reached")
                }
            }
            listAllWeek.add(tempDisplayWeek)
            isFirst.value=false
        }
    }

    if (!isFirst.value){
        HorizontalPager(count = 104, state = pagerState) { page ->
            ItemWeek(
                haptic,
                listAllWeek[page],
                kalendarKonfig,
                clickedDate,
                kalendarEvents,
                size,
                isDot,
                kalendarSelector,
                onCurrentDayClick
            )

        }
    }

    if (listAllWeek.isNotEmpty()) {
        if (oldDate.value != clickedDate.value && (clickedDate.value < listAllWeek[pagerState.currentPage].first() || clickedDate.value > listAllWeek[pagerState.currentPage].last())) {
            var index = -1
            for (i in 0 until listAllWeek.size) {
                val week = listAllWeek[i]
                for (element in week) {
                    if (element == clickedDate.value) {
                        index = i
                    }
                }
            }
            if (index > 0) {
                oldDate.value = clickedDate.value
                scope.launch {
                    pagerState.scrollToPage(index)
                }
            }
        }
    }


}


@RequiresApi(Build.VERSION_CODES.O)
private fun getDisplayWeekCurrent(): List<LocalDate> {
    val currentDay = LocalDate.now()
    val startDate = DateTimeHelper.getDateMonday(currentDay)
    val currentDisplayWeek = startDate.getNext7Dates()
    return currentDisplayWeek

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ItemWeek(
    haptic: HapticFeedback,
    displayWeek: List<LocalDate>,
    kalendarKonfig: KalendarKonfig,
    clickedDate: MutableState<LocalDate>,
    kalendarEvents: List<KalendarEvent>,
    size: Dp,
    isDot: Boolean,
    kalendarSelector: KalendarSelector,
    onCurrentDayClick: (LocalDate, KalendarEvent?, KalendarMoney?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val sizeWeek = displayWeek.size
        for (i in 0 until sizeWeek) {
            val date = displayWeek[i]
            val isSelected = date == clickedDate.value
            val event =  kalendarEvents.find { it.date == date }

            Surface(elevation =5.dp, color =   getBackgroundColor(
                kalendarSelector = kalendarSelector,
                selectedDate = clickedDate.value,
                providedDate = date
            ), shape =if (isDot) KalendarShape.DefaultRectangle else kalendarSelector.shape,
            modifier = Modifier  .padding(
                    start = (if (i == 0) {
                        2.dp
                    } else {
                        4.dp
                    }), end = (if (i == sizeWeek - 1) {
                    2.dp
                } else {
                    4.dp
                })
            )) {
                Column(
                    modifier = Modifier
                        .width(size)
                        .height(80.dp)
                        .clip(if (isDot) KalendarShape.DefaultRectangle else kalendarSelector.shape)
                        .background(
                            if (!isDot) {
                                getBackgroundColor(
                                    kalendarSelector = kalendarSelector,
                                    selectedDate = clickedDate.value,
                                    providedDate = date
                                )
                            } else Color.White
                        )
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            clickedDate.value = date
                            onCurrentDayClick(date, event, null)
                        },
                            verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 16.sp
                        ),
                        color = getTextDayColor(isSelected, kalendarSelector),
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(top = 10.dp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.padding(vertical = 3.dp))

                    Text(
                        text = date.dayOfWeek.getDisplayName(
                            TextStyle.FULL,
                            kalendarKonfig.locale
                        ).toString().subSequence(0, kalendarKonfig.weekCharacters).toString(),
                        style = (if (isSelected) {
                            MaterialTheme.typography.subtitle2.copy(
                                fontSize = 14.sp
                            )
                        } else {
                            MaterialTheme.typography.subtitle1.copy(
                                fontSize = 12.sp
                            )
                        }),
                        color = getTextColor(isSelected, kalendarSelector, event != null),
                        modifier = Modifier
                            .wrapContentHeight(),
                        textAlign = TextAlign.Center
                    )
                    if (event!=null) {
                        KalendarDot(
                            kalendarSelector = kalendarSelector,
                            isSelected = isSelected,
                            isToday = date == LocalDate.now(),
                            numberEvent = event.number
                        )
                    } else {
                        Spacer(modifier = Modifier.size(Grid.Medium))
                    }

                    Spacer(modifier = Modifier.padding(vertical = marginMinimum))
                }
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun getBackgroundColor(
    selectedDate: LocalDate,
    providedDate: LocalDate,
    kalendarSelector: KalendarSelector,
): Color {
    return if (providedDate == LocalDate.now()) {
        when (selectedDate) {
            providedDate -> kalendarSelector.selectedColor
            else -> kalendarSelector.todayColor
        }
    } else {
        when (selectedDate) {
            providedDate -> kalendarSelector.selectedColor
            else -> kalendarSelector.defaultColor
        }
    }
}

private fun getTextColor(
    isSelected: Boolean,
    kalendarSelector: KalendarSelector,
    isEvent: Boolean,
): Color {
    return when {
//        isEvent -> kalendarSelector.eventTextColor
//        else -> when {
        isSelected -> kalendarSelector.selectedTextColor
        else -> kalendarSelector.defaultTextColor
//        }
    }
}

private fun getTextDayColor(isSelected: Boolean, kalendarSelector: KalendarSelector): Color {
    return when {
        isSelected -> kalendarSelector.selectedTextColor
        else -> kalendarSelector.defaultDayColor
    }
}


@RequiresApi(Build.VERSION_CODES.O)
private fun getDateWeek(
    rank: Int,
    haptic: HapticFeedback,
    displayWeek: List<LocalDate>,
    kalendarKonfig: KalendarKonfig,
    errorMessageLogged: (String) -> Unit,
): List<LocalDate> {
    if (rank > 0) {
        val newRank = abs(rank)
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        var lastDayOfWeek = displayWeek.last()
        lastDayOfWeek = lastDayOfWeek.plusDays((if (newRank == 1) 1L else (1 + newRank * 7L)))
        val isLimitAttached =
            lastDayOfWeek.year.validateMaxDate(kalendarKonfig.yearRange.max)
        if (isLimitAttached) {
            return lastDayOfWeek.getNext7Dates()
        } else {
            errorMessageLogged("Maximum year limit reached")
        }
    } else if (rank < 0) {
        val newRank = abs(rank)
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        var firstDayOfWeek = displayWeek.first()
        firstDayOfWeek = firstDayOfWeek.minusDays((if (newRank == 1) 1L else (1 + newRank * 7L)))
        val isLimitAttached =
            firstDayOfWeek.year.validateMinDate(kalendarKonfig.yearRange.min)
        if (isLimitAttached) {
            return firstDayOfWeek.getPrevious7Dates()
        } else {
            errorMessageLogged("Minimum year limit reached")
        }
    }
    return displayWeek
}


