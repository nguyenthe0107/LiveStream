package olmo.wellness.android.ui.screen.playback_video.common

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import olmo.wellness.android.extension.noRippleClickable

@Composable
fun ImageWithBorder(avatar : String? = null,imageId: Int, modifier: Modifier, onClickAction: (() -> Unit)?=null) {
    val rememberResource = rememberAsyncImagePainter(model = imageId)
    if(avatar != null && avatar.isNotEmpty()){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(avatar)
                .crossfade(true)
                .build(),
            error = rememberResource,
            contentDescription = "image-avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(40.dp).clip(CircleShape).noRippleClickable {
                onClickAction?.invoke()
            }
        )
    }else{
        AsyncImage(
            model = imageId,
            error = rememberResource,
            contentDescription = "image-avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(40.dp).clip(CircleShape).noRippleClickable {
                onClickAction?.invoke()
            }
        )
    }
    
}