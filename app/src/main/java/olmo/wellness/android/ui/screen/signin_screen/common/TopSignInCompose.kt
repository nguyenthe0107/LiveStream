package olmo.wellness.android.ui.screen.signin_screen.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color
import olmo.wellness.android.ui.theme.MontserratFont
import olmo.wellness.android.ui.theme.Neutral_Gray_9

@Composable
fun TopSignInCompose(onNavigationSignUp: (() -> Unit)?=null){
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "New to Kepler?", style = MaterialTheme.typography.subtitle2.copy(
                fontFamily = MontserratFont,
                fontWeight = FontWeight.Normal,
                lineHeight = 22.sp,
                color = Neutral_Gray_9
            ),
        )
        Text(
            text = "Sign up for free", style = MaterialTheme.typography.subtitle2.copy(
                fontFamily = MontserratFont,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 22.sp,
                color = Color_LiveStream_Main_Color
            ),
            modifier = Modifier.noRippleClickable {
                onNavigationSignUp?.invoke()
            }
        )
    }
}