package olmo.wellness.android.ui.common

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.PrimaryButton
import olmo.wellness.android.ui.theme.Black_037
import olmo.wellness.android.ui.theme.Neutral_Gray_7
import olmo.wellness.android.ui.theme.Neutral_Gray_8
import olmo.wellness.android.ui.theme.White

@Composable
fun AlertDialog(
    title: String,
    text: String,
    onNegativePressed: () -> Unit,
    onPositivePressed: () -> Unit,
    show: Boolean,
    cancelable: Boolean = true,
    onDismissRequest: () -> Unit,
) {
    BackHandler {
        if (cancelable)
            onDismissRequest.invoke()
    }

    AnimatedVisibility(
        visible = show,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .noRippleClickable {
                    if (cancelable)
                        onDismissRequest.invoke()
                }
                .background(
                    Neutral_Gray_8.copy(
                        alpha = 0.8f
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(
                            White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(
                            16.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.body1.copy(
                            color = Black_037
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = text,
                        style = MaterialTheme.typography.body2.copy(
                            color = Neutral_Gray_7
                        ),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SecondaryButton(
                            text = "Canncel",
                            modifier = Modifier
                                .weight(1f)
                                .padding(
                                    end = 4.dp
                                )
                                .noRippleClickable {
                                    onNegativePressed.invoke()
                                    onDismissRequest.invoke()
                                }
                        )
                        PrimaryButton(
                            text = "OK",
                            modifier = Modifier
                                .weight(1f)
                                .padding(
                                    start = 4.dp
                                )
                                .noRippleClickable {
                                    onPositivePressed.invoke()
                                    onDismissRequest.invoke()
                                }
                        )
                    }
                }
            }
        }
    }
}

data class AlertData(
    val isShow: Boolean,
    val title: String = "",
    val text: String = "",
    val user: User?= null
)