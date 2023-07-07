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

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.himanshoe.kalendar.common.KalendarKonfig
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.KalendarStyle
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarEvent
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarMoney
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.theme.Grid
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.theme.KalendarTheme
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun KalendarOceanic(
    kalendarKonfig: KalendarKonfig = KalendarKonfig(),
    kalendarStyle: KalendarStyle = KalendarStyle(),
    selectedDay: LocalDate = LocalDate.now(),
    kalendarEvents: List<KalendarEvent>,
    onCurrentDayClick: (LocalDate, KalendarEvent?, KalendarMoney?) -> Unit,
    errorMessageLogged: (String) -> Unit,
    isBack: Boolean?=true,
    isDrop : Boolean=true,
    onBack: () -> Unit,
    onDrop: () -> Unit
) {
    val startDate = DateTimeHelper.getDateMonday(selectedDay)
    KalendarTheme {
        val color = kalendarStyle.kalendarBackgroundColor ?: KalendarTheme.colors.selectedColor
        Box(
            modifier = Modifier
                .background(color)
                .padding(bottom = Grid.Three, top = Grid.Two),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Grid.OneHalf),
                verticalArrangement = Arrangement.SpaceAround
            ) {

                KalendarOceanWeek(
                    kalendarSelector = kalendarStyle.kalendarSelector,
                    kalendarKonfig = kalendarKonfig,
                    startDate = startDate,
                    selectedDay = selectedDay,
                    kalendarEvents = kalendarEvents,
                    onCurrentDayClick = onCurrentDayClick,
                    errorMessageLogged = errorMessageLogged,
                    isBack = isBack,
                    isDrop = isDrop,
                    onBack = onBack,
                    onDrop = onDrop
                )
            }
        }
    }
}