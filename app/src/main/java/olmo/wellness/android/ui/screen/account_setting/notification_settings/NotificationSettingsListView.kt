package olmo.wellness.android.ui.screen.account_setting.notification_settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.ItemSwitch
import olmo.wellness.android.ui.common.SwitchViewEx
import olmo.wellness.android.ui.screen.profile_dashboard.social_media.SocialType
import olmo.wellness.android.ui.theme.Color_gray_FF7

@Composable
fun NotificationSettingsList(
    data: List<NotificationSettingsModel>,
    onNotificationSettingsUpdate: (dataUpdated: List<NotificationSettingsModel>, itemChanged: NotificationSettingsModel) -> Unit
) {
    val listOptions = remember {
        mutableStateOf(data)
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color_gray_FF7)
    ){
        items(listOptions.value.size) { index ->
            ItemNotificationSetting(
                listOptions.value[index],
            ) { itemChanged ->
                listOptions.value.toMutableList().find {
                    it.id == itemChanged.id
                }.also {
                    it?.isSelected = itemChanged.isSelected
                }

                onNotificationSettingsUpdate.invoke(
                    listOptions.value, itemChanged
                )
            }
        }
    }
}

@Composable
private fun ItemNotificationSetting(
    item: NotificationSettingsModel,
    onSelected: (NotificationSettingsModel) -> Unit
){
    if(item.icon != null){
        Row(modifier = Modifier.fillMaxWidth().padding(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically){
            Image(modifier = Modifier.defaultMinSize(30.dp),
                painter = painterResource(id = item.icon), contentDescription = "icon")
            var statusSwitch by remember {
                mutableStateOf(item.isSelected)
            }
            ItemSwitch(
                title = item.optionName,
                switchDefault = statusSwitch,
                onSwitch = { isOn ->
                    statusSwitch = isOn
                    onSelected.invoke(item.copy(
                        isSelected = isOn
                    ))
                }
            )
        }
    }else{
        var statusSwitch by remember {
            mutableStateOf(item.isSelected)
        }
        ItemSwitch(
            title = item.optionName,
            switchDefault = statusSwitch,
            onSwitch = { isOn ->
                statusSwitch = isOn
                onSelected.invoke(item.copy(
                    isSelected = isOn
                ))
            }
        )
    }
}

data class NotificationSettingsModel(
    val id: Int,
    var isSelected: Boolean,
    val optionName: String,
    val icon: Int?=null,
    val socialType: SocialType?=null
)