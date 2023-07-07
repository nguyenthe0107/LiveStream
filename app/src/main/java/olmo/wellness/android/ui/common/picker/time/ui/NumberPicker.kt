package olmo.wellness.android.ui.common.picker.time.ui

import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import olmo.wellness.android.ui.common.picker.time.type.PickerSelector
import olmo.wellness.android.ui.common.picker.time.type.PickerShape

@Composable
fun NumberPicker(
    modifier: Modifier = Modifier,
    label: (Int) -> String = {
        it.toString()
    },
    value: Int,
    onValueChange: (Int) -> Unit,
    range: Iterable<Int>,
    pickerSelector: PickerSelector,
    pickerShape: PickerShape,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    ListItemPicker(
        modifier = modifier,
        label = label,
        value = value,
        pickerSelector =pickerSelector,
        pickerShape = pickerShape,
        onValueChange = onValueChange,
        list = range.toList(),
        textStyle = textStyle
    )
}