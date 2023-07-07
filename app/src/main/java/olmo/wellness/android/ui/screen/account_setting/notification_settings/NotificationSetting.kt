package olmo.wellness.android.ui.screen.account_setting.notification_settings

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.ItemSwitch
import olmo.wellness.android.ui.common.ToolbarSchedule
import olmo.wellness.android.ui.screen.business_hours.AvatarMascot
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotificationSetting(navController: NavController,
    viewModel: NotificationSettingViewModel = hiltViewModel()) {

    val defaultValue = viewModel.defaultContactSyncMode.collectAsState()
    var isTurnOnAllNotification by remember {
        mutableStateOf(false)
    }
    Scaffold(topBar = {
        ToolbarSchedule(
            title = stringResource(id = R.string.title_notification_setting),
            backIconDrawable = R.drawable.ic_back_calendar,
            navController = navController,
            backgroundColor = Color_LiveStream_Main_Color
        )
    }){
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(Color_LiveStream_Main_Color)){
            val (options, imageCompose) = createRefs()
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 32.dp,
                            topEnd = 32.dp
                        )
                    )
                    .background(color = Color_gray_FF7)
                    .fillMaxSize()
                    .fillMaxHeight()
                    .constrainAs(options) {
                        start.linkTo(parent.start)
                        top.linkTo(imageCompose.top, 36.dp)
                    }
                    .padding(top = 50.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color_gray_FF7)
                        .padding(
                            vertical = marginStandard
                        )
                ){
                    Spacer(modifier = Modifier.padding(vertical = 20.dp))
                    PushNotifications(modifier = Modifier.padding(),
                        defaultValue.value, enablePushNotification = {
                            isTurnOnAllNotification = it
                    })
                    AnimatedVisibility(visible = isTurnOnAllNotification) {
                        MyNotifications(
                            modifier = Modifier.padding(
                                top = 8.dp
                            )
                        )
                    }
                }
            }

            AvatarMascot(modifier = Modifier.constrainAs(imageCompose) {
                top.linkTo(parent.top, 15.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, uri = null, callbackFun = {
            }, src = R.drawable.olmo_ic_notification_setting)

        }
    }
}

@Composable
fun PushNotifications(
    modifier: Modifier = Modifier,
    defaultValueInput: Boolean?,
    enablePushNotification: (Boolean) -> Unit
){
    Column {
        Text(
            stringResource(id = R.string.title_notification_setting_setion_push_noti),
            style = MaterialTheme.typography.subtitle1.copy(
                color = Color_Purple_FBC,
                fontWeight = FontWeight.Bold
            ),
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                )
        )
        var defaultValue by remember {
           mutableStateOf(defaultValueInput ?: false)
        }
        ItemSwitch(title = stringResource(R.string.title_notification_setting_turn_on_all), modifier = Modifier,
            switchDefault = defaultValue ,onSwitch = {
                defaultValue = it
                enablePushNotification.invoke(it)
            })
    }
}

@Composable
fun MyNotifications(
    modifier: Modifier = Modifier)
{
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            stringResource(R.string.title_notification_setting_section_my_noti),
            style = MaterialTheme.typography.subtitle1.copy(
                color = Color_Purple_FBC,
                fontWeight = FontWeight.Bold
            ),
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    top = 8.dp
                )
                .padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                )
        )

        NotificationSettingsList(
            listOf(
                NotificationSettingsModel(
                    1,
                    false,
                    stringResource(id = R.string.title_notification_setting_kepler_update)
                ),
                NotificationSettingsModel(
                    2,
                    false,
                    stringResource(id = R.string.title_notification_setting_live_stream)
                ),
                NotificationSettingsModel(
                    3,
                    false,
                    stringResource(id = R.string.title_notification_setting_your_circle)
                ),
                NotificationSettingsModel(
                    4,
                    false,
                    stringResource(id = R.string.title_notification_setting_messages)
                ),
                NotificationSettingsModel(
                    5,
                    false,
                    stringResource(id = R.string.title_notification_setting_wallet_updates)
                ),
            )
        ){ newData, itemChanged ->
        }
    }
}

