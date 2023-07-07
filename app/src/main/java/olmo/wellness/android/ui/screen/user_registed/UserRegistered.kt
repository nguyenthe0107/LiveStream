package olmo.wellness.android.ui.screen.user_registed

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.defination.UserExistsType
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.MontserratFont
import olmo.wellness.android.ui.theme.Neutral_Gray_9

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UserRegisteredScreen(
    onCallBackFunc: ((Boolean) -> Unit)? = null,
    userType : UserExistsType ?= UserExistsType.USER_REGISTERED
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
            .verticalScroll(rememberScrollState())
            .padding(start = 33.dp, end = 33.dp),
        verticalArrangement = Arrangement.SpaceBetween){

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ){
            SpaceCompose(30.dp)
            AsyncImage(
                model = R.drawable.olmo_img_user_registed,
                contentDescription = "image",
                placeholder = painterResource(R.drawable.olmo_img_user_registed),
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(155.dp, 140.dp)
            )
            SpaceCompose(height = 32.dp)
            var contentHeader by remember {
              mutableStateOf("")
            }
            var contentDescription by remember {
                mutableStateOf("")
            }

            when(userType){
                UserExistsType.USER_REGISTERED -> {
                    contentHeader = stringResource(id = R.string.title_user_registered)
                    contentDescription = stringResource(id = R.string.text_des_user_registered)
                }

                UserExistsType.USER_NOT_REGISTER -> {
                    contentHeader = stringResource(id = R.string.title_user_not_registered)
                    contentDescription = stringResource(id = R.string.description_not_user_registered)
                }
            }
            Text(
                text = contentHeader, style =
                MaterialTheme.typography.subtitle2.copy(
                    lineHeight = 22.sp
                ),
                fontFamily = MontserratFont,
                color = Neutral_Gray_9,
                fontWeight = FontWeight.SemiBold
            )
            SpaceCompose(12.dp)
            Text(
                text = contentDescription, style =
                MaterialTheme.typography.subtitle2.copy(
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                ),
                fontFamily = MontserratFont,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            SpaceCompose(16.dp)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom){
            when(userType){
                UserExistsType.USER_NOT_REGISTER -> {
                    PrimaryLiveButton(
                        Modifier.fillMaxWidth(),
                        stringResource(id = R.string.action_sign_up),
                        onClickFunc = {
                            onCallBackFunc?.invoke(true)
                        }, enable = true
                    )
                }
                UserExistsType.USER_REGISTERED -> {
                    PrimaryLiveButton(
                        Modifier.fillMaxWidth(),
                        stringResource(id = R.string.tv_login),
                        onClickFunc = {
                            onCallBackFunc?.invoke(true)
                        }, enable = true
                    )
                }
            }
        }
    }
}