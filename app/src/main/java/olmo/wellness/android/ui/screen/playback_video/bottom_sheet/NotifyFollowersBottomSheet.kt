package olmo.wellness.android.ui.screen.playback_video.bottom_sheet

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.common.empty.EmptyBottomSheet
import olmo.wellness.android.ui.theme.Black_037
import olmo.wellness.android.ui.theme.Neutral_Gray_3
import olmo.wellness.android.ui.theme.Transparent
import olmo.wellness.android.ui.theme.White

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NotifyFollowersBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    modifier: Modifier = Modifier,
    confirmCallback: (() -> Unit) ?= null,
    cancelCallback: (() -> Unit) ?= null,
) {
    val context = LocalContext.current

    ModalBottomSheetLayout(   sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContentColor = Transparent,sheetContent = {
            if (modalBottomSheetState.isVisible){
            Title()
            Body(context)
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
            )
            BottomBar(context, confirmCallback, cancelCallback)
            }else{
                EmptyBottomSheet()
            }
        }) {

    }
}

@Composable
private fun BottomBar(
    context: Context,
    confirmCallback: (() -> Unit) ?= null,
    cancelCallback: (() -> Unit) ?= null,
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(White)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    bottom = 32.dp,
                    top = 12.dp,
                    end = 16.dp,
                    start = 16.dp
                )
        ) {
            SecondLiveButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp)
                    .noRippleClickable {
                        cancelCallback?.invoke()
                    }
                ,
                stringResource(id = R.string.cancel),
                onClickFunc = {
                    cancelCallback?.invoke()
                }
            )
            PrimaryLiveButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp),
                stringResource(id = R.string.ok),
                onClickFunc = {
                    confirmCallback?.invoke()
                }
            )
        }
    }
}

@Composable
private fun Body(context: Context) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(Color.White)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "When you send notifications for your follower, they'll receive a notification about your livestream right away.",
                style = MaterialTheme.typography.subtitle1.copy(
                    color = Black_037, fontSize = 14.sp
                )
            )
        }
    }
}

@Composable
private fun Title() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .height(60.dp)
        ) {
            Text(
                text = stringResource(R.string.lb_notify_followers),
                style = MaterialTheme.typography.subtitle2.copy(
                    fontSize = 18.sp
                ),
                modifier = Modifier
                    .align(Alignment.Center)
            )
            Spacer (
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(color = Neutral_Gray_3)
                    .align(Alignment.BottomStart)
            )
    }
}