package olmo.wellness.android.ui.common.picker.date.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import olmo.wellness.android.ui.common.picker.time.type.PickerSelector
import olmo.wellness.android.ui.common.picker.time.type.PickerShape
import olmo.wellness.android.ui.common.picker.time.type.PositionItem
import olmo.wellness.android.ui.common.picker.time.ui.NumberPicker
import kotlin.math.abs

data class Dates(
    var day: Int,
    var month: Int,
    var year: Int,
)

val monthsNumber = (1..12)
val years = (1950..2050).map { it }

val monthsNames = listOf(
    "Jan",
    "Feb",
    "Mar",
    "Apr",
    "May",
    "Jun",
    "Jul",
    "Aug",
    "Sep",
    "Oct",
    "Nov",
    "Dec"
)

fun getMonthName(month: Int) = monthsNames[month - 1]

fun getMaxDayOfMonth(month: Int, year: Int): Int {
    return when (month) {
        1, 3, 5, 7, 8, 10, 12 -> {
            31
        }
        2 -> {
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                29
            } else {
                28
            }
        }
        3, 6, 9, 11 -> {
            30
        }
        else -> {
            31
        }
    }
}

fun getDaysOfMonth(month: Int, year: Int): List<Int> {
    return when (month) {
        1, 3, 5, 7, 8, 10, 12 -> {
            (1..31).map { it }
        }
        2 -> {
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                (1..29).map { it }
            } else {
                (1..28).map { it }
            }
        }
        3, 6, 9, 11 -> {
            (1..30).map { it }
        }
        else -> {
            (1..31).map { it }
        }
    }
}


@Composable
fun DatesNumberPicker(
    modifier: Modifier = Modifier,
    value: Dates,
    monthRange: Iterable<Int> = monthsNumber,
    yearRange: Iterable<Int> = years,
    pickerSelector: PickerSelector,
    pickerShape: PickerShape = PickerShape(),
    onValueChange: (Dates) -> Unit,
    textStyle: TextStyle = LocalTextStyle.current.copy()) {
    FullDatePicker(
        modifier = modifier,
        datesSelect = value,
        monthRange = monthRange,
        yearRange = yearRange,
        pickerShape = pickerShape,
        pickerSelector = pickerSelector,
        textStyle = textStyle,
        onValueChange = onValueChange
    )
}

@Composable
private fun FullDatePicker(
    modifier: Modifier = Modifier,
    datesSelect: Dates,
    monthRange: Iterable<Int> = monthsNumber,
    yearRange: Iterable<Int> = years,
    pickerSelector: PickerSelector,
    pickerShape: PickerShape = PickerShape(),
    onValueChange: (Dates) -> Unit,
    textStyle: TextStyle = LocalTextStyle.current.copy()
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NumberPicker(
            modifier = Modifier.weight(1f),
            label = {
                "${if (abs(it) < 10) "0" else ""}$it"
            },
            value = datesSelect.day,
            onValueChange = {
                onValueChange.invoke(datesSelect.copy(day = it))
            },
            pickerShape = pickerShape.copy(radius = 20.dp, position = PositionItem.Left),
            pickerSelector = pickerSelector,
            textStyle = textStyle,
            range = getDaysOfMonth(datesSelect.month, datesSelect.year)
        )

        NumberPicker(
            modifier = Modifier.weight(1f),
            label = {
                getMonthName(it)
            },
            value = datesSelect.month,
            onValueChange = {
                val maxDay = getMaxDayOfMonth(it, datesSelect.year)
                if (maxDay < datesSelect.day) {
                    onValueChange.invoke(datesSelect.copy(month = it, day = maxDay))
                } else {
                    onValueChange.invoke(datesSelect.copy(month = it))
                }

            },
            pickerShape = pickerShape.copy(radius = 20.dp, position = PositionItem.Mid),
            pickerSelector = pickerSelector,
            textStyle = textStyle,
            range = monthRange
        )

        NumberPicker(
            modifier = Modifier.weight(1f),
            label = {
                "${if (abs(it) < 10) "0" else ""}$it"
            },
            value = datesSelect.year,
            onValueChange = {
//                datesSelect.year = it
                onValueChange.invoke(datesSelect.copy(year = it))

            },
            pickerShape = pickerShape.copy(radius = 20.dp, position = PositionItem.Right),
            pickerSelector = pickerSelector,
            textStyle = textStyle,
            range = yearRange
        )

    }
}

