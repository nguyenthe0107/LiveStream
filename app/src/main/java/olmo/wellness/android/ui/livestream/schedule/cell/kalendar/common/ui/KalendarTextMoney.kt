package olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.himanshoe.kalendar.common.KalendarSelector
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarMoney
import olmo.wellness.android.util.getConvertKCurrency

@Composable
fun KalendarTextMoney(
    isSelected: Boolean,
    isToday: Boolean,
    kalendarSelector: KalendarSelector,
    kalendarMoney: KalendarMoney
) {
    Text(
        text = getConvertKCurrency(kalendarMoney.money),
        style = MaterialTheme.typography.caption.copy(
            fontSize = 8.sp, lineHeight = 24.sp, color = getTextColor(isSelected,kalendarSelector)
        ),
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        textAlign = TextAlign.Center
    )
}


private fun getTextColor(
    isSelected: Boolean,
    kalendarSelector: KalendarSelector,
): Color {
    return when {
//        isEvent -> kalendarSelector.eventTextColor
//        else -> when {
        isSelected -> kalendarSelector.selectedTextColor
        else -> kalendarSelector.selectedColor
//        }
    }
}


