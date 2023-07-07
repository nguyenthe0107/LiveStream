package olmo.wellness.android.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.theme.Transparent
import olmo.wellness.android.ui.theme.White


@Composable
fun ToolbarSchedule(
    title: String, navController: NavController,
    backgroundColor: Color = Transparent,
    backIconDrawable: Int = R.drawable.ic_back_calendar,
    optionIconRightDrawable: Int? = null,
    titleColor: Color = White,
    onOpenDrawer: (() -> Unit)? = null,
    onBackStackFunc: (() -> Unit)? = null,
    onOpenOptionSettingFunc: (() -> Unit)? = null,
) {
    val focusManager = LocalFocusManager.current
    val height = 56.dp
    val horizontalPadding = 15.dp

    Box(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .height(height)
            .padding(horizontal = horizontalPadding)
    ) {

        Image(
            painter = painterResource(id = backIconDrawable),
            contentDescription = "Back Navigation",
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .align(Alignment.CenterStart)
                .clickable {
                    if (onBackStackFunc != null) {
                        onBackStackFunc.invoke()
                    } else if (onOpenDrawer != null) {
                        onOpenDrawer.invoke()
                    } else {
                        navController.popBackStack()
                    }
                    focusManager.clearFocus()
                }
        )

        Text(text = title, style = MaterialTheme.typography.subtitle2.copy(
            color = titleColor,
            fontSize = 20.sp
        ), modifier = Modifier.align(Alignment.Center))

        if(optionIconRightDrawable != null){
            Image(painter = painterResource(id = optionIconRightDrawable), contentDescription = "ic_right",
            modifier = Modifier.noRippleClickable {
                onOpenOptionSettingFunc?.invoke()
            }.align(Alignment.CenterEnd))
        }
    }
}