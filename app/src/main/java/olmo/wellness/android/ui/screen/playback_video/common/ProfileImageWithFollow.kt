package olmo.wellness.android.ui.screen.playback_video.common

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.theme.color_Red_1D52

@Composable
fun ProfileImageWithFollow(
    idVideo : Int ?= 0,
    modifier: Modifier,
    showFollow: Boolean,
    imageId: Int,
    avatar : String ?= null,
    followActionCallBack : (() -> Unit) ?= null,
    unFollowActionCallBack : (() -> Unit) ?= null,
    transformFollowAction : Boolean ?= null,
    actionFollow : MutableState<Pair<Int, Boolean>>?= mutableStateOf(Pair(0, false)),
    disableFollow: Boolean ?= null,
    navigationToProfile : (() -> Unit) ?= null,
){
    var transformPin = false
    if(actionFollow?.value?.first == idVideo){
        transformPin = actionFollow?.value?.second ?: false
    }
    if (showFollow || transformFollowAction == true || transformPin) {
        Box(modifier = modifier){
            ImageWithBorder(
                avatar = avatar,
                imageId = imageId,
                modifier = modifier,
                onClickAction = {
                    navigationToProfile?.invoke()
                }
            )
            val imageChecked = painterResource(id = R.drawable.olmo_ic_checked_purple)
            Icon(
                painter = imageChecked,
                contentDescription = null,
                modifier = Modifier
                    .size(18.dp)
                    .offset(y = (5).dp)
                    .clip(CircleShape)
                    .background(color_Red_1D52)
                    .align(Alignment.BottomCenter).noRippleClickable {
                        unFollowActionCallBack?.invoke()
                    },
                tint = Color.Unspecified
            )
        }
    } else {
        Box(modifier = modifier){
            ImageWithBorder(
                avatar = avatar,
                imageId = imageId,
                modifier = modifier,
                onClickAction = {
                    navigationToProfile?.invoke()
                }
            )
            if(disableFollow == null || disableFollow == false){
                val imageVectorOpen = painterResource(id = R.drawable.olmo_ic_group_follow)
                Icon(
                    painter = imageVectorOpen,
                    contentDescription = "Icon_bottom",
                    modifier = Modifier
                        .size(18.dp)
                        .offset(y = (5).dp)
                        .clip(CircleShape)
                        .background(color_Red_1D52)
                        .align(Alignment.BottomCenter).noRippleClickable {
                            followActionCallBack?.invoke()
                    },
                    tint = Color.Unspecified
                )
            }
        }
    }
}
