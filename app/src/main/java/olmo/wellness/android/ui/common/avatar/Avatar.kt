package olmo.wellness.android.ui.common.avatar

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import olmo.wellness.android.R
import olmo.wellness.android.ui.chat.conversation_list.cell.avatar.ImageAvatar
import olmo.wellness.android.util.rememberStreamImagePainter

@Composable
fun Avatar(
    imageUrl: String?,
    name: String?,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    textStyle: TextStyle = MaterialTheme.typography.subtitle1,
    placeholderPainter: Painter? = null,
    contentDescription: String? = null,
    initialsAvatarOffset: DpOffset = DpOffset(0.dp, 0.dp),
    onClick: (() -> Unit)? = null,
) {
    if (LocalInspectionMode.current && imageUrl?.isNotBlank()==true){
        ImageAvatar(
            modifier = modifier,
            shape = shape,
            painter = painterResource(id = R.drawable.olmo_ic_group_default_place_holder),
            contentDescription = contentDescription,
            onClick = onClick
        )
        return
    }
    if (imageUrl?.isBlank()==true) {
        ImageAvatar(
            modifier = modifier,
            shape = shape,
            painter = painterResource(id = R.drawable.olmo_ic_group_default_place_holder),
            contentDescription = contentDescription,
            onClick = onClick
        )
        return
    }

    val painter = rememberStreamImagePainter(
        data = imageUrl,
        placeholderPainter = painterResource(id = R.drawable.olmo_ic_group_default_place_holder)
    )

    if (painter.state is AsyncImagePainter.State.Error) {
        ImageAvatar(
            modifier = modifier,
            shape = shape,
            painter = painterResource(id = R.drawable.olmo_ic_group_default_place_holder),
            contentDescription = contentDescription,
            onClick = onClick
        )
    } else if (painter.state is AsyncImagePainter.State.Loading && placeholderPainter != null) {
        ImageAvatar(
            modifier = modifier,
            shape = shape,
            painter = placeholderPainter,
            contentDescription = contentDescription,
            onClick = onClick
        )
    } else {
        ImageAvatar(
            modifier = modifier,
            shape = shape,
            painter = painter,
            contentDescription = contentDescription,
            onClick = onClick
        )
    }
}

@Preview(showBackground = true, name = "Avatar Preview (With image URL)")
@Composable
private fun AvatarWithImageUrlPreview() {
    AvatarPreview(
        imageUrl = "https://picsum.photos/200",
        initials = "JC"
    )
}

@Composable
private fun AvatarPreview(
    imageUrl: String,
    initials: String,
) {
        Avatar(
            modifier = Modifier.size(36.dp),
            imageUrl = imageUrl,
            name = initials
        )
}

