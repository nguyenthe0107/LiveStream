package olmo.wellness.android.ui.livestream.schedule.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.theme.Black_037
import olmo.wellness.android.ui.theme.Color_LiveStream_Accent_Color
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color
import olmo.wellness.android.ui.theme.White

@Composable
fun LiveAlertDialog(
    show: Boolean,
    title: String,
    message: String,
    negativeText: String,
    positiveText: String,
    onNegativeClick: () -> Unit,
    onPositiveClick: () -> Unit,
    onClickOutSide: (() -> Unit)? = null
    ) {
    AnimatedVisibility(
        visible = show,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Black_037.copy(
                        alpha = 0.7f
                    )
                )
                .noRippleClickable {
                    onClickOutSide?.invoke()
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color_LiveStream_Main_Color,
                                Color_LiveStream_Accent_Color,
                            )
                        ),
                        shape = RoundedCornerShape(25.dp)
                    )
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.body1.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = message,
                        style = MaterialTheme.typography.body2.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Normal
                        )
                    )
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
                            ,
                            negativeText,
                            borderColor = White,
                            textColor = Color.White,
                            paddingVertical = 10.dp,
                            paddingHorizontal = 20.dp,
                            onClickFunc = {
                                onNegativeClick.invoke()
                            }
                        )
                        PrimaryLiveButton(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 6.dp),
                            positiveText,
                            backgroundColor = Color.White,
                            textColor = Color_LiveStream_Main_Color,
                            paddingVertical = 10.dp,
                            paddingHorizontal = 20.dp,
                            onClickFunc = {
                                onPositiveClick.invoke()
                            }
                        )
                    }
                }
            }
        }
    }
}