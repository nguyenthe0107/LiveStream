package olmo.wellness.android.ui.screen.playback_video.bottom_sheet.voucher_service

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

class RoundedCutoutShape(
    private val offsetY: Float?,
    private val cornerRadiusDp: Dp,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ) = Outline.Generic(run path@{
        val cornerRadius = with(density) { cornerRadiusDp.toPx() }
        val rect = Rect(Offset.Zero, size)
        val mainPath = Path().apply {
            addRoundRect(RoundRect(rect, CornerRadius(cornerRadius)))
        }
        if (offsetY == null) return@path mainPath
        val cutoutPath = Path().apply {
            val circleSize = Size(cornerRadius, cornerRadius) * 2f
            val visiblePart = 0.25f
            val leftOval = Rect(
                offset = Offset(
                    x = 0 - circleSize.width * (1 - visiblePart),
                    y = offsetY - circleSize.height / 2
                ),
                size = circleSize
            )
            val rightOval = Rect(
                offset = Offset(
                    x = rect.width - circleSize.width * visiblePart,
                    y = offsetY - circleSize.height / 2
                ),
                size = circleSize
            )
            addOval(leftOval)
            addOval(rightOval)
        }
        return@path Path().apply {
            op(mainPath, cutoutPath, PathOperation.Difference)
        }
    })
}