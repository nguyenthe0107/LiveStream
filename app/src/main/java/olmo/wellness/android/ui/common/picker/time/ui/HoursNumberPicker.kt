package olmo.wellness.android.ui.common.picker.time.ui

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
import kotlin.math.abs

sealed interface Hours {
    val hours: Int
    val minutes: Int
}

data class FullHours(
    override val hours: Int,
    override val minutes: Int,
) : Hours

data class AMPMHours(
    override val hours: Int,
    override val minutes: Int,
    val dayTime: DayTime
) : Hours {
    enum class DayTime {
        AM,
        PM;
    }
}

@Composable
fun HoursNumberPicker(
    modifier: Modifier = Modifier,
    value: Hours,
    leadingZero: Boolean = true,
    hoursRange: Iterable<Int> = when (value) {
        is FullHours -> (0..23)
        is AMPMHours -> (1..12)
    },
    minutesRange: Iterable<Int> = (0..59),
    hoursDivider: (@Composable () -> Unit)? = null,
    minutesDivider: (@Composable () -> Unit)? = null,
    onValueChange: (Hours) -> Unit,
    pickerSelector: PickerSelector,
    pickerShape : PickerShape = PickerShape(),
    textStyle: TextStyle = LocalTextStyle.current.copy(),
) {
    when (value) {
        is FullHours ->
            FullHoursNumberPicker(
                modifier = modifier,
                value = value,
                leadingZero = leadingZero,
                hoursRange = hoursRange,
                minutesRange = minutesRange,
                hoursDivider = hoursDivider,
                minutesDivider = minutesDivider,
                onValueChange = onValueChange,
                pickerSelector = pickerSelector,
                pickerShape = pickerShape,
                textStyle = textStyle,
            )
        is AMPMHours ->
            AMPMHoursNumberPicker(
                modifier = modifier,
                value = value,
                leadingZero = leadingZero,
                hoursRange = hoursRange,
                minutesRange = minutesRange,
                hoursDivider = hoursDivider,
                minutesDivider = minutesDivider,
                onValueChange = onValueChange,
                pickerSelector = pickerSelector,
                pickerShape = pickerShape,
                textStyle = textStyle,
            )
    }
}

@Composable
fun FullHoursNumberPicker(
    modifier: Modifier = Modifier,
    value: FullHours,
    leadingZero: Boolean = true,
    hoursRange: Iterable<Int>,
    minutesRange: Iterable<Int> = (0..59),
    hoursDivider: (@Composable () -> Unit)? = null,
    minutesDivider: (@Composable () -> Unit)? = null,
    onValueChange: (Hours) -> Unit,
    pickerSelector: PickerSelector,
    pickerShape : PickerShape,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NumberPicker(
            modifier = Modifier.weight(1f),
            label = {
                "${if (leadingZero && abs(it) < 10) "0" else ""}$it"
            },
            value = value.hours,
            onValueChange = {
                onValueChange(value.copy(hours = it))
            },
            pickerShape = pickerShape.copy(radius = 20.dp,position = PositionItem.Left),
            pickerSelector = pickerSelector,
            textStyle = textStyle,
            range = hoursRange
        )

        hoursDivider?.invoke()

        NumberPicker(
            modifier = Modifier.weight(1f),
            label = {
                "${if (leadingZero && abs(it) < 10) "0" else ""}$it"
            },
            value = value.minutes,
            onValueChange = {
                onValueChange(value.copy(minutes = it))
            },
            pickerShape = pickerShape.copy(radius = 20.dp,position = PositionItem.Right),
            pickerSelector = pickerSelector,
            textStyle = textStyle,
            range = minutesRange
        )

        minutesDivider?.invoke()
    }
}

@Composable
fun AMPMHoursNumberPicker(
    modifier: Modifier = Modifier,
    value: AMPMHours,
    leadingZero: Boolean = true,
    hoursRange: Iterable<Int>,
    minutesRange: Iterable<Int> = (0..59),
    hoursDivider: (@Composable () -> Unit)? = null,
    minutesDivider: (@Composable () -> Unit)? = null,
    onValueChange: (Hours) -> Unit,
    pickerSelector: PickerSelector,
    pickerShape : PickerShape,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NumberPicker(
            modifier = Modifier.weight(1f),
            value = value.hours,
            label = {
                "${if (leadingZero && abs(it) < 10) "0" else ""}$it"
            },
            onValueChange = {
                onValueChange(value.copy(hours = it))
            },
            pickerShape = pickerShape.copy(radius = 20.dp,position = PositionItem.Left),
            pickerSelector = pickerSelector,
            textStyle = textStyle,
            range = hoursRange
        )

        hoursDivider?.invoke()

        NumberPicker(
            modifier = Modifier.weight(1f),
            label = {
                "${if (leadingZero && abs(it) < 10) "0" else ""}$it"
            },
            value = value.minutes,
            onValueChange = {
                onValueChange(value.copy(minutes = it))
            },
            pickerShape = pickerShape.copy(position = PositionItem.Mid),
            pickerSelector = pickerSelector,
            textStyle = textStyle,
            range = minutesRange
        )

        minutesDivider?.invoke()

        NumberPicker(
            modifier = Modifier.weight(1f),
            value = when (value.dayTime) {
                AMPMHours.DayTime.AM -> 0
                else -> 1
            },
            label = {
                when (it) {
                    0 -> "AM"
                    else -> "PM"
                }
            },
            onValueChange = {
                onValueChange(
                    value.copy(
                        dayTime = when (it) {
                            0 -> AMPMHours.DayTime.AM
                            else -> AMPMHours.DayTime.PM
                        }
                    )
                )
            },
            pickerShape = pickerShape.copy(radius = 20.dp, position = PositionItem.Right),
            pickerSelector = pickerSelector,
            textStyle = textStyle,
            range = (0..1)
        )
    }
}