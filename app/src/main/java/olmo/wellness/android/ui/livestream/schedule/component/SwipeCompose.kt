package olmo.wellness.android.ui.livestream.schedule.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import olmo.wellness.android.core.dpToPxUtils
import olmo.wellness.android.domain.model.defination.SwipeDirection
import olmo.wellness.android.ui.theme.Color_Green_Main
import olmo.wellness.android.ui.theme.Color_RED_65B2
import kotlin.math.roundToInt

@ExperimentalMaterialApi
@Composable
fun SwipeCompose(color: Color=Color.White,
                 isShowOptionDefault: Boolean ?= null,
                 isShowDeleteItemDefault: Boolean ?= null,
                 content: @Composable() () -> Unit,
                 callbackDeleteItemUpcoming: (() -> Unit) ?= null,
                 callbackEditItemUpcoming: (() -> Unit) ?= null,
                 callbackSharingItemUpcoming: (() -> Unit) ?= null,
                 disableSwipeLeftToRight: Boolean ?= false
){
    val widthDefaultSize = 100.dp
    val swipeAbleState = rememberSwipeableState(initialValue = 0,
        confirmStateChange = {
            true
        })
    val isShowOption by remember {
        mutableStateOf(isShowOptionDefault ?: false)
    }
    val isShowDeleteItem by remember {
        mutableStateOf(isShowDeleteItemDefault ?: false)
    }
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val widthDefault = (screenWidth / 2) ?: widthDefaultSize
    val anchors = mapOf(
        0f to 0,
        -dpToPxUtils(
            context = context,
            dpValue = 110F
        ) to 1, // Swipe to left
        dpToPxUtils(
            context = context,
            dpValue = if(disableSwipeLeftToRight == true) 10F else 200F
        ) to 2 // Swipe to right
    )
    if (swipeAbleState.isAnimationRunning) {
        DisposableEffect(Unit) {
            onDispose {
                when (swipeAbleState.currentValue) {
                    SwipeDirection.Right.raw -> {
                    }
                    SwipeDirection.Left.raw -> {
                    }
                    else -> {
                        return@onDispose
                    }
                }
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().background(color= color)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color)
                    .swipeable(
                        state = swipeAbleState,
                        anchors = anchors,
                        thresholds = { _, _ ->
                            FractionalThreshold(0.3f)
                        },
                        orientation = Orientation.Horizontal
                    )
            ){
                /*  Delete Part when Swipe*/
                if(isShowDeleteItem){
                    DeleteItemSwipeUpcoming(modifier = Modifier
                        .padding(16.dp)
                        .width(100.dp)
                        .height(88.dp)
                        .align(Alignment.CenterEnd)
                        .background(color = Color_RED_65B2), callbackDeleteItemUpcoming = {
                        callbackDeleteItemUpcoming?.invoke()
                    })
                }
                /* Option Part when Swipe*/
                if(isShowOption){
                    OptionItemSwipeUpcoming(modifier = Modifier
                        .padding(16.dp)
                        .height(88.dp)
                        .align(Alignment.CenterStart)
                        .width(widthDefault)
                        .background(color = Color_Green_Main),
                    callbackEditItemUpcoming = {
                        callbackEditItemUpcoming?.invoke()
                    }, callbackSharingItemUpcoming = {
                        callbackSharingItemUpcoming?.invoke()
                    })
                }
                /*Main Component */
                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                swipeAbleState.offset.value.roundToInt(), 0
                            )
                        }
                        .fillMaxWidth()
                        .background(color)
                        .wrapContentHeight()
                        .align(Alignment.CenterStart)
                ) {
                    content()
                }
            }
        }
    }
}