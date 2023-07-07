package olmo.wellness.android.ui.livestream.schedule.cell.kalendar.ui
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import com.himanshoe.kalendar.common.KalendarKonfig
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.KalendarStyle
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarEvent
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarMoney
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.theme.KalendarShape
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.ui.KalendarType.Firey
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.ui.KalendarType.Oceanic
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.ui.firey.KalendarFirey
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.ui.oceanic.KalendarOceanic
import java.time.LocalDate

/**
 * [KalendarType] is used to distinguish the type of calendar
 * [Firey] represents the Monthly View
 * [Oceanic] represents the Week View
 */
@SuppressLint("NewApi", "LocalSuppress")
sealed class KalendarType {
    data class Firey(val shape: Shape = KalendarShape.SelectedShape) : KalendarType()
    data class Oceanic constructor(
        val shape: Shape = KalendarShape.ButtomCurvedShape,
        val startDate: LocalDate = LocalDate.now(),
    ) : KalendarType()
}

/**
 * [Kalendar] is exposed to be used as composable
 * @param kalendarType is the type of calendar.See [KalendarType]
 * @param kalendarStyle sets the style of calendar.See [KalendarStyle]
 * @param kalendarKonfig is user to setup config needed for rendering calendar.See [KalendarKonfig]
 * @param selectedDay is representation for selected marker.
 * @param onCurrentDayClick gives the day click listener
 * @param errorMessage gives the error message if any
 */
@SuppressLint("NewApi")
@Composable
fun Kalendar(
    kalendarType: KalendarType,
    kalendarKonfig: KalendarKonfig = KalendarKonfig(),
    kalendarStyle: KalendarStyle = KalendarStyle(),
    selectedDay: LocalDate = LocalDate.now(),
    kalendarEvents: List<KalendarEvent> = emptyList(),
    kalendarMoneys: List<KalendarMoney> = emptyList(),
    onCurrentDayClick: (LocalDate, KalendarEvent?, KalendarMoney?) -> Unit,
    errorMessage: (String) -> Unit = {},
    onBackMonth :((startMonth : LocalDate, endMonth : LocalDate)->Unit)?=null,
    isBack: Boolean = true,
    isDrop : Boolean=true,
    onBack: () -> Unit,
    onDrop: () -> Unit,
) {
//    val shape =
//        if (kalendarStyle.hasRadius) KalendarShape.SelectedShape else KalendarShape.DefaultRectangle


    when (kalendarType) {
        is Firey -> KalendarFirey(
            kalendarKonfig,
            kalendarStyle.copy(shape = kalendarStyle.shape),
            selectedDay,
            kalendarEvents,
            kalendarMoneys,
            onCurrentDayClick,
            errorMessage,
            onBackMonth,
            isBack,
            isDrop,
            onBack,
            onDrop,
        )
        is Oceanic -> KalendarOceanic(
            kalendarKonfig,
            kalendarStyle.copy(shape = kalendarStyle.shape),
            selectedDay,
            kalendarEvents,
            onCurrentDayClick,
            errorMessage,
            isBack,
            isDrop,
            onBack,
            onDrop
        )
    }
}
