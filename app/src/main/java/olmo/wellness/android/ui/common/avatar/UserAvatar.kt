package olmo.wellness.android.ui.common.avatar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import olmo.wellness.android.ui.chat.conversation_list.cell.avatar.OnlineIndicatorAlignment

@Composable
fun UserAvatar(
    name: String?, urlAvatar: String?,
    isOnline: Boolean = false,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    contentDescription: String? = null,
    showOnlineIndicator: Boolean = true,
    onlineIndicatorAliment: OnlineIndicatorAlignment = OnlineIndicatorAlignment.TopEnd,
    initialsAvatarOffset: DpOffset = DpOffset(0.dp, 0.dp),
    onlineIndicator: @Composable BoxScope.() -> Unit = {
        DefaultOnlineIndicator(onlineIndicatorAliment)
    },
    onClick: (() -> Unit)? = null,
) {
    Box(modifier = modifier) {

        Avatar(
            modifier = Modifier.fillMaxSize(),
            imageUrl = urlAvatar,
            name = name,
            shape = shape,
            contentDescription = contentDescription,
            onClick = onClick,
            initialsAvatarOffset = initialsAvatarOffset
        )

        if (showOnlineIndicator && isOnline) {
            onlineIndicator()
        }
    }
}


@Composable
internal fun BoxScope.DefaultOnlineIndicator(onlineIndicatorAlignment: OnlineIndicatorAlignment) {
    OnlineIndicator(modifier = Modifier.align(onlineIndicatorAlignment.alignment))
}