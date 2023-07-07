package olmo.wellness.android.ui.common

import android.graphics.drawable.shapes.Shape
import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import olmo.wellness.android.R


@Composable
fun RoundedAsyncImage(
    imageUrl: String,
    cornerRadius: Dp,
    size : Dp,
    shape: RoundedCornerShape? = null,
    contentScale: ContentScale = ContentScale.Crop,
    resDefault : Int=R.drawable.olmo_bg_thumnail_livestream,
    modifier: Modifier = Modifier
) {
    val rememberResource = rememberAsyncImagePainter(model = resDefault)
    val scale = remember(contentScale) {
        mutableStateOf(ContentScale.Crop)
    }
    val request = remember {
        mutableStateOf<ImageRequest?>(null)
    }

    request.value = ImageRequest.Builder(LocalContext.current.applicationContext)
        .data(imageUrl)
        .crossfade(true)
        .diskCacheKey(imageUrl)
        .build()
    AsyncImage(
        model = request.value,
        placeholder = rememberResource,
        contentDescription = "",
        error = rememberResource,
        onSuccess = {
            if (scale.value != contentScale) {
                scale.value = contentScale
            }
        },
        modifier = modifier
            .clip(shape ?: RoundedCornerShape(cornerRadius)).size(size),
        onError = {
            if (scale.value != ContentScale.Crop) {
                scale.value = ContentScale.Crop
            }
        },
        contentScale = scale.value
    )
}