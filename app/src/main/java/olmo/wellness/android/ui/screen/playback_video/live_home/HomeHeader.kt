package olmo.wellness.android.ui.screen.playback_video.live_home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import olmo.wellness.android.R
import olmo.wellness.android.core.utils.getScreenHeight
import olmo.wellness.android.core.utils.pxToDp
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color
import olmo.wellness.android.ui.theme.White

@Composable
fun HomeHeader() {
    ConstraintLayout {
        val (bottomZone, topZone, imgGhosts) = createRefs()
        val topZoneHeight = (getScreenHeight() /5).pxToDp(LocalContext.current).dp
        val imageHeight = topZoneHeight/1.5f
        val imageWidth = imageHeight * 1.779f // 1.779 is image ratio (w/h, 210/118) follow by design

        Box(modifier = Modifier
            .fillMaxWidth()
            .height(topZoneHeight)
            .background(Color_LiveStream_Main_Color)
            .constrainAs(topZone) {
                top.linkTo(parent.top)
            }
        )

        Box(modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .background(White)
            .constrainAs(bottomZone) {
                top.linkTo(topZone.bottom)
            }
        )

        Image(
            painter = painterResource(id = R.drawable.img_ghosts),
            contentDescription = "Image ghosts",
            Modifier
                .size(imageWidth, imageHeight)
                .padding(
                    end = 16.dp,
                )
                .constrainAs(imgGhosts) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}

fun buildListActions(): @Composable() (RowScope.() -> Unit) {
    return {
        Icon(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = "",
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(16.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_notification),
            contentDescription = "",
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(16.dp))
    }
}
