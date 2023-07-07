package olmo.wellness.android.ui.screen.profile_setting_screen.cell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import olmo.wellness.android.ui.theme.*

@Composable
fun DividerView(paddingStart: Dp? = marginDouble, paddingEnd: Dp? = marginDouble,
                paddingTop: Dp? = marginDouble, paddingBottom: Dp? = marginStandard) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(
                start = paddingStart ?: marginDouble,
                end = paddingEnd?: marginDouble,
                top = paddingTop?: marginDouble,
                bottom = paddingBottom?: marginStandard
            )
    ) {
        Divider(
            modifier = Modifier
                .height(defaultHeightLine)
                .background(Neutral_Gray_3)
        )
    }
}