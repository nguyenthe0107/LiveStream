package olmo.wellness.android.ui.screen.verify_code_screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceHorizontalCompose
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UserLimitedOTP(
    onCallBackFunc: ((Boolean) -> Unit)? = null,
    onCallHotline: ((Boolean) -> Unit)? = null,
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .fillMaxHeight(0.6f)
            .verticalScroll(rememberScrollState())
            .padding(start = 33.dp, end = 33.dp),
        verticalArrangement = Arrangement.SpaceBetween) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ){
            SpaceCompose(26.dp)
            AsyncImage(
                model = R.drawable.olmo_ic_group_default_welcome_live_stream,
                contentDescription = "image",
                placeholder = painterResource(R.drawable.olmo_img_user_registed),
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(155.dp, 140.dp)
            )
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .border(
                        width = 1.dp,
                        color = Color_PURPLE_7F4,
                        shape = RoundedCornerShape(8.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(
                            paddingValues = PaddingValues(
                                vertical = 8.dp,
                                horizontal = 10.dp
                            )
                        ).noRippleClickable {
                            onCallHotline?.invoke(true)
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AsyncImage(
                        model = R.drawable.olmo_ic_support_customer,
                        contentDescription = "icon_support",
                        modifier = Modifier.size(32.dp)
                    )
                    SpaceHorizontalCompose(width = 18.dp)
                    Column() {
                        Text(
                            text = "Kepler Hotline",
                            style = MaterialTheme.typography.subtitle2.copy(
                                color = Color_PURPLE_7F4,
                                fontSize = 16.sp,
                                lineHeight = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "028 7777 76 78",
                            style = MaterialTheme.typography.subtitle2.copy(
                                color = Color_PURPLE_7F4,
                                fontSize = 16.sp,
                                lineHeight = 20.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                }
            }
            SpaceCompose(height = 32.dp)
            var contentHeader by remember {
                mutableStateOf("")
            }
            var contentDescription by remember {
                mutableStateOf("")
            }
            contentHeader = stringResource(id = R.string.text_limit_otp_header)
            contentDescription = stringResource(id = R.string.text_limit_otp_description)
            Text(
                text = contentHeader, style =
                MaterialTheme.typography.subtitle2.copy(
                    lineHeight = 22.sp,
                    fontSize = 14.sp
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
            verticalArrangement = Arrangement.Bottom
        ){
            PrimaryLiveButton(
                Modifier.fillMaxWidth(),
                stringResource(id = R.string.action_confirm_dialog_close),
                onClickFunc = {
                    onCallBackFunc?.invoke(true)
                }, enable = true
            )
        }
    }
}