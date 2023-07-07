package olmo.wellness.android.ui.common.picker.time.type

import androidx.compose.ui.graphics.Color
import olmo.wellness.android.ui.theme.Color_BLUE_7F4
import olmo.wellness.android.ui.theme.Neutral_Gray_5
import olmo.wellness.android.ui.theme.White

sealed class PickerSelector(
    open val backgroundColor: Color,
    open val textSelectColor: Color,
    open val textNormal: Color,
) {
    data class PickerDefault(
        override val textNormal: Color = Neutral_Gray_5,
        override val textSelectColor: Color = White,
        override val backgroundColor: Color = Color_BLUE_7F4
    ) : PickerSelector(
        backgroundColor = backgroundColor,
        textNormal = textNormal,
        textSelectColor = textSelectColor
    )
}