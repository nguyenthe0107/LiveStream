package olmo.wellness.android.ui.screen.profile_dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.defination.ProfileSuccessType
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceHorizontalCompose
import olmo.wellness.android.ui.theme.*

@Composable
fun NotiUpdateSuccessCompose(type: ProfileSuccessType){
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .padding(start = marginDouble, end = marginDouble)
        .background(color = Green_FED)
        .border(
            width = 1.dp, color = Color(0xFFDDDFE3),
            shape = RoundedCornerShape(12.dp)
        )
        .padding(
            start = marginDouble,
            top = marginStandard,
            bottom = marginStandard,
            end = marginDouble
        )
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start){
        Image(painter = painterResource(id = R.drawable.ic_check_green), contentDescription = "")
        SpaceHorizontalCompose(width = 5.dp)
        var context by remember {
            mutableStateOf("Your info has been updated")
        }
        when(type){
            ProfileSuccessType.BIO -> {
                context = "Your bio has been updated"
            }
            ProfileSuccessType.EMAIL -> {
                context = "Your email has been updated"
            }
            ProfileSuccessType.SERVICE_NAME -> {
                context = "Your username has been updated"
            }
            ProfileSuccessType.NAME -> {
                context = "Your name has been updated"
            }
            ProfileSuccessType.PHONE -> {
                context = "Your phone has been updated"
            }
            ProfileSuccessType.BIRTHDAY -> {
                context = "Your birthday has been updated"
            }
            ProfileSuccessType.AVATAR -> {
                context = "Your avatar has been updated"
            }
            ProfileSuccessType.GENDER -> {
                context = "Your gender has been updated"
            }
            ProfileSuccessType.SOCIAL_MEDIA -> {
                context = "Your social media has been updated"
            }
            else -> {
                context = "Your info has been updated"
            }
        }
        Text(text = context, style = MaterialTheme.typography.subtitle2.copy(
            color = Neutral_Gray_9,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Normal
        ))
    }
}