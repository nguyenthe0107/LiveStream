package olmo.wellness.android.ui.livestream.schedule.cell.kalendar.ui.firey
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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.himanshoe.kalendar.common.KalendarSelector
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarEvent
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarMoney
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.theme.Grid
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.theme.KalendarShape
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.ui.KalendarDot
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.ui.KalendarTextMoney
import java.time.LocalDate

@SuppressLint("NewApi")
@Composable
internal fun KalendarDay(
    size: Dp,
    modifier: Modifier = Modifier,
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    kalendarEvents: List<KalendarEvent>,
    kalendarMoneys: List<KalendarMoney>,
    kalendarSelector: KalendarSelector,
    onDayClick: (LocalDate, KalendarEvent?) -> Unit,
) {
    val isDot = kalendarSelector is KalendarSelector.Dot
    val event = kalendarEvents.find { it.date == date }
    val money = kalendarMoneys.find { it.date==date }

    val tempModifier =if (kalendarMoneys.isNotEmpty()){
        modifier.size(width = size, height = size*1.4f)
    }else{
        modifier.size(size)
    }
    Surface(
        color = if (isSelected && !isDot) kalendarSelector.selectedColor else kalendarSelector.defaultColor,
        shape = if (!isDot) kalendarSelector.shape else KalendarShape.DefaultRectangle
    ) {
        var localModifier = tempModifier
            .clickable { onDayClick(date, event) }

//        if (isToday && !isDot) {
//            localModifier = localModifier.border(
//                width = 2.dp,
//                color = kalendarSelector.todayColor,
//                shape = kalendarSelector.shape
//            )
//        }
        Column(
            modifier = localModifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                maxLines = 1,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 16.sp
                ),
                color = getTextColor(
                    isSelected,
                    kalendarSelector,
                    event != null
                )
            )
            if (isDot) {
                KalendarDot(
                    kalendarSelector = kalendarSelector,
                    isSelected = isSelected,
                    isToday = isToday
                )
            }

            if (event!=null){
                KalendarDot(
                    kalendarSelector = kalendarSelector,
                    isSelected = isSelected,
                    isToday = date == LocalDate.now(),
                    numberEvent = event.number
                )
            }
            if (money!=null){
                KalendarTextMoney(
                    isSelected = isSelected,
                    isToday =date ==LocalDate.now() ,
                    kalendarSelector = kalendarSelector,
                    kalendarMoney = money
                )
            }else{
                KalendarTextMoney(
                    isSelected = isSelected,
                    isToday =date ==LocalDate.now() ,
                    kalendarSelector = kalendarSelector,
                    kalendarMoney = KalendarMoney(LocalDate.now(),null)
                )
            }
//            else{
//                Spacer(modifier = Modifier.size(Grid.Medium))
//            }
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
