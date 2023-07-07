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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.himanshoe.kalendar.common.KalendarSelector
import com.himanshoe.kalendar.ui.oceanic.KalendarOceanButton
import com.himanshoe.kalendar.ui.oceanic.getNext7Dates
import com.himanshoe.kalendar.ui.oceanic.getPrevious7Dates
import olmo.wellness.android.R
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.theme.Grid
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.util.validateMaxDate
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.util.validateMinDate
import olmo.wellness.android.ui.theme.White

@Composable
internal fun KalendarHeader(
    text: String,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    kalendarSelector: KalendarSelector,
    isBack: Boolean = true,
    isDrop: Boolean = true,
    onBack: () -> Unit,
    onDrop: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Grid.OneHalf),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (isBack) {
            Icon(painter = painterResource(id = R.drawable.ic_back_calendar),
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        onBack.invoke()
                    }
                    .padding()
                    .clip(CircleShape),
                tint = kalendarSelector.textTitle,
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
            Icon(painter = painterResource(id = R.drawable.ic_up),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        onDrop.invoke()
                    }
                    .padding(10.dp)
                    .clip(CircleShape),
                tint = kalendarSelector.textTitle,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(painter = painterResource(id = R.drawable.ic_previous_month),
                contentDescription = "Previous Month",
                tint = kalendarSelector.textTitle,
                modifier = Modifier
                    .clickable {
                        onPreviousMonthClick.invoke()
                    }
                    .clip(CircleShape)
                    .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
            )

            Icon(painter = painterResource(id = R.drawable.ic_previous_month),
                contentDescription = "Next Month",
                tint = kalendarSelector.textTitle,
                modifier = Modifier
                    .rotate(180f)
                    .clickable {
                        onNextMonthClick.invoke()
                    }
                    .padding(end = 20.dp, top = 10.dp, bottom = 10.dp)
                    .clip(CircleShape)
            )


        }
    }

}

@Composable
private fun KalendarButton(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    kalendarSelector: KalendarSelector,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(Grid.Three)
            .clip(CircleShape)
            .background(kalendarSelector.todayColor)
    ) {
        Icon(
            modifier = Modifier
                .padding(Grid.Half)
                .alpha(0.6F),
            imageVector = imageVector,
            contentDescription = contentDescription
        )
    }
}
