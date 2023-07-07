package olmo.wellness.android.ui.screen.business_hours

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import olmo.wellness.android.R
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color

@Composable
fun AvatarMascot(
    modifier: Modifier,
    url: String? = null,
    uri: Uri?,
    src : Int?=null,
    callbackFun: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .size(114.dp)
            .background(
                Color_LiveStream_Main_Color,
                shape = CircleShape
            )
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center),
            shape = CircleShape,
            elevation = 2.dp,
            backgroundColor = Color_LiveStream_Main_Color
        ) {
            var painter = painterResource(R.drawable.ic_launcher_foreground)
            if (url != null) {
                painter =
                    rememberImagePainter(data = url,
                        builder = {
                            placeholder(R.drawable.ic_baseline_image_24)
                        })
            } else if (uri != null) {
                painter = rememberImagePainter(
                    data = uri
                )
            }
            painter = if (url != null && uri != null) {
                rememberImagePainter(data = url,
                    builder = {
                        placeholder(R.drawable.ic_baseline_image_24)
                    })
            } else {
                if (src!=null){
                    rememberAsyncImagePainter(model = src)
                }else{
                    rememberAsyncImagePainter(model = R.drawable.ic_launcher_foreground)
                }
            }
            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center),
                shape = CircleShape,
                elevation = 2.dp,
                backgroundColor = Color_LiveStream_Main_Color
            ) {
                AsyncImage(
                    model = R.drawable.ic_launcher_foreground,
                    error = painter,
                    contentDescription = "avatar",
                    modifier = modifier
                        .size(90.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }

    }
}