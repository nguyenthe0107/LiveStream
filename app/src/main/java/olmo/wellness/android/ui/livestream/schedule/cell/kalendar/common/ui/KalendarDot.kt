package olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.ui
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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.himanshoe.kalendar.common.KalendarSelector
import olmo.wellness.android.R
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.theme.Grid
import olmo.wellness.android.ui.theme.Color_gray_6CF
import olmo.wellness.android.ui.theme.White

@Composable
internal fun KalendarDot(
    isSelected: Boolean,
    isToday: Boolean,
    kalendarSelector: KalendarSelector,
    numberEvent: Int = 1,
) {
    if (numberEvent <= 3) {
        Row() {
            repeat(numberEvent) {
                Surface(
                    shape = kalendarSelector.shape,
                    color = getColor(isSelected, isToday, kalendarSelector),
                    modifier = Modifier
                        .size(Grid.Medium)
                        .padding(horizontal = 0.5.dp),
                    content = {}
                )
            }
        }
    } else {
        Row() {
            repeat(2) {
                Surface(
                    shape = kalendarSelector.shape,
                    color = getColor(isSelected, isToday, kalendarSelector),
                    modifier = Modifier
                        .size(Grid.Medium)
                        .padding(horizontal = 0.5.dp)
                    ,
                    content = {}
                )
            }
            Icon(
                tint =( if (isSelected) White else Color_gray_6CF ),
                painter = painterResource(id = R.drawable.ic_add_calendar),
                contentDescription = "Add calendar",
                modifier = Modifier
                    .padding(horizontal = 0.5.dp)
                    .size(Grid.Medium)
            )
        }
    }

}

private fun getColor(
    isSelected: Boolean,
    isToday: Boolean,
    kalendarSelector: KalendarSelector,
): Color {
    return when {
//        isToday -> kalendarSelector.todayColor
        isSelected -> kalendarSelector.selectedTextColor
        else -> kalendarSelector.eventTextColor
    }
}
