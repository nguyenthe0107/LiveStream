package olmo.wellness.android.ui.screen.profile_dashboard.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.defination.ProfileSuccessType
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.theme.*

@Composable
fun ItemInfoProfile(titleView: String ="",
                    hiltContent: String? = null,
                    content: String? ="",
                    callbackFun: (() -> Unit)?=null,
                    userFiledType: ProfileSuccessType,
                    typeSuccess: MutableState<MutableList<ProfileSuccessType>>? = null){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = marginDouble,
                end = marginDouble,
                top = marginStandard,
                bottom = marginMinimum
            )
            .height(defaultHeightButton)
            .noRippleClickable {
                callbackFun?.invoke()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = titleView,
            style = MaterialTheme.typography.body2,
            overflow = TextOverflow.Ellipsis,
            color = Color_Black_019,
            maxLines = 1
        )
        Row(modifier = Modifier.padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically){
            val color = if(content?.isEmpty() == true){
                Color_Purple_FBC.copy(alpha = 0.5F)
            }else{
                Color_Purple_FBC
            }
            if(content?.isNotEmpty() == true){
                Text(
                    text = content,
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    color = color,
                    maxLines = 1,
                )
            }else{
                Text(
                    text = hiltContent.orEmpty(),
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    color = color,
                    maxLines = 1,
                )
            }
            if(typeSuccess?.value?.isNotEmpty() == true){
                typeSuccess.value.distinct().map {
                    when(it){
                        ProfileSuccessType.ADDRESS -> {
                            if(userFiledType == it){
                                Image(painter = painterResource(id = R.drawable.olmo_ic_check_circle_blue_filled),
                                    contentDescription = "olmo_ic_check_circle_blue_filled",
                                    modifier = Modifier.padding(start = marginStandard))
                            }
                        }

                        ProfileSuccessType.NAME -> {
                            if(userFiledType == it){
                                Image(painter = painterResource(id = R.drawable.olmo_ic_check_circle_blue_filled),
                                    contentDescription = "olmo_ic_check_circle_blue_filled",
                                    modifier = Modifier.padding(start = marginStandard))
                            }
                        }

                        ProfileSuccessType.BIO -> {
                            if(userFiledType == it){
                                Image(painter = painterResource(id = R.drawable.olmo_ic_check_circle_blue_filled),
                                    contentDescription = "olmo_ic_check_circle_blue_filled",
                                    modifier = Modifier.padding(start = marginStandard))
                            }
                        }

                        ProfileSuccessType.BIRTHDAY -> {
                            if(userFiledType == it){
                                Image(painter = painterResource(id = R.drawable.olmo_ic_check_circle_blue_filled),
                                    contentDescription = "olmo_ic_check_circle_blue_filled",
                                    modifier = Modifier.padding(start = marginStandard))
                            }
                        }

                        ProfileSuccessType.PHONE -> {
                            if(userFiledType == it){
                                Image(painter = painterResource(id = R.drawable.olmo_ic_check_circle_blue_filled),
                                    contentDescription = "olmo_ic_check_circle_blue_filled",
                                    modifier = Modifier.padding(start = marginStandard))
                            }
                        }

                        ProfileSuccessType.EMAIL -> {
                            if(userFiledType == it){
                                Image(painter = painterResource(id = R.drawable.olmo_ic_check_circle_blue_filled),
                                    contentDescription = "olmo_ic_check_circle_blue_filled",
                                    modifier = Modifier.padding(start = marginStandard))
                            }
                        }

                        ProfileSuccessType.STORE_NAME -> {
                            if(userFiledType == it){
                                Image(painter = painterResource(id = R.drawable.olmo_ic_check_circle_blue_filled),
                                    contentDescription = "olmo_ic_check_circle_blue_filled",
                                    modifier = Modifier.padding(start = marginStandard))
                            }
                        }

                        ProfileSuccessType.YOUR_NAME -> {
                            if(userFiledType == it){
                                Image(painter = painterResource(id = R.drawable.olmo_ic_check_circle_blue_filled),
                                    contentDescription = "olmo_ic_check_circle_blue_filled",
                                    modifier = Modifier.padding(start = marginStandard))
                            }
                        }
                    }
                }
            }
        }
    }
}