package olmo.wellness.android.ui.common.picker.time.type

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class PickerShape(
    val radius: Dp = 0.dp,
    val position: PositionItem = PositionItem.Mid,
    val padding: Dp = 8.dp
)