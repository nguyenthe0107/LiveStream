/*
 * Copyright (c) 2014-2022 Stream.io Inc. All rights reserved.
 *
 * Licensed under the Stream License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://github.com/GetStream/stream-chat-android/blob/main/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package olmo.wellness.android.ui.common.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Represents a special avatar case when we need to show the initials instead of an image. Usually happens when there
 * are no images to show in the avatar.
 *
 * @param initials The initials to show.
 * @param modifier Modifier for styling.
 * @param shape The shape of the avatar.
 * @param textStyle The [TextStyle] that will be used for the initials.
 * @param avatarOffset The initials offset to apply to the avatar.
 * @param onClick The handler when the user clicks on the avatar.
 */
@Composable
public fun InitialsAvatar(
    initials: String?,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    textStyle: TextStyle = MaterialTheme.typography.subtitle1,
    avatarOffset: DpOffset = DpOffset(0.dp, 0.dp),
    onClick: (() -> Unit)? = null,
) {
    val clickableModifier: Modifier = if (onClick != null) {
        modifier.clickable(
            onClick = onClick,
            indication = rememberRipple(bounded = false),
            interactionSource = remember { MutableInteractionSource() }
        )
    } else {
        modifier
    }

    val initialsGradient = initialsGradient(initials = initials)

    Box(
        modifier = clickableModifier
            .clip(shape)
            .background(brush = initialsGradient)
    ) {
        initials?.let {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(avatarOffset.x, avatarOffset.y),
                text = it,
                style = textStyle,
                color = Color.White
            )
        }
    }
}


@Composable
@ReadOnlyComposable
internal fun initialsGradient(initials: String?): Brush {
    val gradientBaseColors = LocalContext.current.resources.getIntArray(R.array.stream_compose_avatar_gradient_colors)

    val baseColorIndex = abs(initials.hashCode()) % gradientBaseColors.size
    val baseColor = gradientBaseColors[baseColorIndex]

    return Brush.linearGradient(
        listOf(
            Color(adjustColorBrightness(baseColor, GradientDarkerColorFactor)),
            Color(adjustColorBrightness(baseColor, GradientLighterColorFactor)),
        )
    )
}

private const val GradientDarkerColorFactor = 1.3f
private const val GradientLighterColorFactor = 0.7f

internal fun adjustColorBrightness(color: Int, factor: Float): Int {
    val a = android.graphics.Color.alpha(color)
    val r = (android.graphics.Color.red(color) * factor).roundToInt()
    val g = (android.graphics.Color.green(color) * factor).roundToInt()
    val b = (android.graphics.Color.blue(color) * factor).roundToInt()
    return android.graphics.Color.argb(
        a,
        r.coerceAtMost(255),
        g.coerceAtMost(255),
        b.coerceAtMost(255)
    )
}


