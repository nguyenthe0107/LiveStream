package com.himanshoe.kalendar.ui.oceanic
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
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.himanshoe.kalendar.common.KalendarSelector
import com.himanshoe.kalendar.common.YearRange
import olmo.wellness.android.R
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.theme.KalendarColor
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.util.validateMaxDate
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.util.validateMinDate
import olmo.wellness.android.ui.theme.Color_Green_Main
import olmo.wellness.android.ui.theme.White
import java.time.LocalDate
import java.time.Period
import kotlin.time.Duration.Companion.days

@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun KalendarOceanHeader(
    text: String,
    isBack: Boolean?=null,
    isDrop : Boolean=true,
    kalendarSelector: KalendarSelector,
    onBack: () -> Unit,
    onDrop: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (isBack!=false) {
            Image(painter = painterResource(id = R.drawable.ic_back_calendar),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        onBack.invoke()
                    }
                    .size(28.dp)
                    .padding()
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
        }

        Text(
            text = text,
            style = MaterialTheme.typography.subtitle2.copy(
                fontSize = 20.sp, color = kalendarSelector.textTitle
            )
        )
        if (isDrop) {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_up),
                contentDescription = "Down",
                modifier = Modifier
                    .rotate(180f)
                    .clickable {
                        onDrop.invoke()
                    }
                    .padding(10.dp)
                    .clip(CircleShape),
                tint = kalendarSelector.textTitle)

        }
    }
}

@Composable
internal fun KalendarOceanButton(
    modifier: Modifier,
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
//            .clip(CircleShape)
    ) {
        Icon(
            modifier = Modifier
                .alpha(0.5F),
            imageVector = imageVector,
            contentDescription = contentDescription
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
internal fun LocalDate.getNext7Dates(): List<LocalDate> {
    val dates = mutableListOf<LocalDate>()
    repeat(7) { day ->
        dates.add(this.plusDays(day.toLong()))
    }
    return dates
}

@RequiresApi(Build.VERSION_CODES.O)
internal fun LocalDate.getPrevious7Dates(): List<LocalDate> {
    val dates = mutableListOf<LocalDate>()
    repeat(7) { day ->
        dates.add(this.minusDays(day.toLong()))
    }
    return dates.reversed()
}
