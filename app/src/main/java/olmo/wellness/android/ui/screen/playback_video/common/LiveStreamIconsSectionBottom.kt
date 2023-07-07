package olmo.wellness.android.ui.screen.playback_video.common

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.screen.playback_video.explore.OlboardHomeInteractionEvents
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color

@Composable
fun LiveStreamIconsSectionBottom(
    avatar : String ?= null,
    livestreamInfo: LiveSteamShortInfo?=null,
    countHeart : Int?,
    countServiceBooking :Int?,
    interactionEvents: ((OlboardHomeInteractionEvents) -> Unit)?=null,
    isLiveStream: Boolean?=null,
    isComment : Boolean ?= null){

    var isButtonVisible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
//        delay(500)
        isButtonVisible = false
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(20.dp))
//        ProfileImageWithFollow(
//            modifier = Modifier
//                .size(40.dp),
//            showFollow = false,
//            imageId = R.drawable.olmo_img_default_avatar,
//            avatar = avatar,
//            disableFollow = true
//        )
        Spacer(modifier = Modifier.height(20.dp))
        if(isComment == null || isComment == false){
            Icon(
                painter = painterResource(id = R.drawable.olmo_ic_seller_rep_mes),
                contentDescription = "olmo_ic_seller_rep_mes",
                tint= Color.Unspecified,
                modifier = Modifier
                    .size(40.dp)
                    .noRippleClickable {
                        interactionEvents?.invoke(OlboardHomeInteractionEvents.SetInformationLiveStream)
                    }
            )
        }else{
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                var fav by remember { mutableStateOf(false) }
                val animatedProgress = remember { Animatable(0f) }
                if (!fav) {
                    LaunchedEffect(fav) {
                        animatedProgress.animateTo(
                            targetValue = 1.3f,
                            animationSpec = tween(600),
                        )
                    }
                }
                Icon(
                    painter = painterResource(id = R.drawable.olmo_ic_heart_white),
                    contentDescription = "olmo_ic_heart_white",
                    tint= (if (fav) Color_LiveStream_Main_Color else Color.Unspecified),
                    modifier = Modifier
                        .size(40.dp)
                        .noRippleClickable {
                            if (!isButtonVisible) {
                                fav = true
                                interactionEvents?.invoke(OlboardHomeInteractionEvents.ReactionHeart)
                            }
                        }
                )
                Text(text =(countHeart?.toString() ?: "0"), style = MaterialTheme.typography.caption.copy(
                    color = Color.White,
                    lineHeight = 18.sp
                ))
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        if(isLiveStream == null || isLiveStream == false){
            Icon(
                painter = painterResource(id = R.drawable.olmo_ic_broadcast_livestream),
                contentDescription = "olmo_ic_broadcast_livestream",
                tint= Color.Unspecified,
                modifier = Modifier
                    .size(40.dp)
                    .noRippleClickable {
                        interactionEvents?.invoke(OlboardHomeInteractionEvents.OpenModeBroadcast)
                    }
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
        Icon(painter = painterResource(id = R.drawable.olmo_ic_switch_camera),
            contentDescription = "olmo_ic_switch_camera",
            tint= Color.Unspecified,
            modifier = Modifier
                .size(40.dp)
                .noRippleClickable {
                    interactionEvents?.invoke(OlboardHomeInteractionEvents.SwitchCamera)
                }
        )
//        Spacer(modifier = Modifier.height(20.dp))
//
//        Icon(painter = painterResource(id = R.drawable.olmo_ic_livestream_filter),
//            contentDescription = "ic_filter",
//            tint= Color.Unspecified,
//            modifier = Modifier
//                .size(40.dp)
//                .noRippleClickable {
//                    interactionEvents?.invoke(OlboardHomeInteractionEvents.FilterLiveStream)
//                }
//        )

        Spacer(modifier = Modifier.height(20.dp))

        /*if(isLiveStream != null && isLiveStream == true){
            Icon(painter = painterResource(id = R.drawable.olmo_ic_sharing),
                contentDescription = "ic_share",
                tint= Color.Unspecified,
                modifier = Modifier
                    .size(40.dp)
                    .noRippleClickable {
                        interactionEvents?.invoke(OlboardHomeInteractionEvents.ShareVideo(livestreamInfo))
                    }
            )
            Spacer(modifier = Modifier.height(20.dp))
        }*/

        Box(modifier = Modifier){
            Icon(painter = painterResource(id = R.drawable.olmo_ic_create_booking),
                contentDescription = "ic_share",
                tint= Color.Unspecified,
                modifier = Modifier
                    .size(40.dp)
                    .noRippleClickable {
                        interactionEvents?.invoke(OlboardHomeInteractionEvents.AddBookService)
                    }
            )
            if(countServiceBooking != null && countServiceBooking > 0){
                Box(modifier = Modifier
                    .size(20.dp)
                    .clip(shape = CircleShape)
                    .background(color = Color.Red)
                    .align(Alignment.TopEnd)){
                    Text(text = countServiceBooking.toString(), style = MaterialTheme.typography.subtitle2.copy(
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 8.sp
                    ), modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}
