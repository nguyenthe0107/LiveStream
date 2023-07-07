package olmo.wellness.android.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest


@Composable
public fun rememberStreamImagePainter(
    data: Any?,
    placeholderPainter: Painter? = null,
    errorPainter: Painter? = null,
    fallbackPainter: Painter? = errorPainter,
    onLoading: ((AsyncImagePainter.State.Loading) -> Unit)? = null,
    onSuccess: ((AsyncImagePainter.State.Success) -> Unit)? = null,
    onError: ((AsyncImagePainter.State.Error) -> Unit)? = null,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
): AsyncImagePainter {
    return rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(data)
            .build(),
        imageLoader = LocalStreamImageLoader.current,
        placeholder = placeholderPainter,
        error = errorPainter,
        fallback = fallbackPainter,
        contentScale = contentScale,
        onSuccess = onSuccess,
        onError = onError,
        onLoading = onLoading,
        filterQuality = filterQuality
    )
}
