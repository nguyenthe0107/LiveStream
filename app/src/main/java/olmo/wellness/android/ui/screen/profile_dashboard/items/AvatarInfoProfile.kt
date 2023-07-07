package olmo.wellness.android.ui.screen.profile_dashboard.items

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import coil.compose.rememberImagePainter
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color

@Composable
fun AvatarInfoProfile(
    modifier: Modifier,
    url: String? = null,
    uriTakePicture : Uri?= null,
    callbackFun: (() -> Unit)? = null){
    Box(
        modifier = modifier
            .size(120.dp)
            .background(
                Color_LiveStream_Main_Color,
                shape = CircleShape
            ).noRippleClickable {
                callbackFun?.invoke()
            }
    ){
        Card(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center),
            shape = CircleShape,
            elevation = 2.dp,
            backgroundColor = Color_LiveStream_Main_Color){
            val painter = painterResource(R.drawable.ic_launcher_foreground)
            AsyncImage(
                model = url ?: painter,
                error = painter,
                contentDescription = "",
                modifier = modifier
                    .size(100.dp)
                    .align(Alignment.Center)
                    .clip(CircleShape)
                    .border(6.dp, Color.White, CircleShape),
                contentScale = ContentScale.Crop
            )
            if(uriTakePicture != null){
                Image(
                    painter = rememberImagePainter(
                        data = uriTakePicture
                    ),
                    contentDescription = "",
                    modifier = modifier
                        .size(100.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .border(5.dp, Color.White, CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Image(
            painter = painterResource(R.drawable.olmo_ic_take_picture_black_filled),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset((-12).dp, 0.dp)
                .size(36.dp)
        )
    }
}