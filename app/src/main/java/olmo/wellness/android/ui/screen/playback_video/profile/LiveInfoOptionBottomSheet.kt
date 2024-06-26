package olmo.wellness.android.ui.screen.playback_video.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.ui.screen.profile_dashboard.component_common.GroupButtonBottomCompose
import olmo.wellness.android.ui.screen.profile_setting_screen.cell.GroupRadioButtonSelected
import olmo.wellness.android.ui.theme.White

@Composable
fun LiveInfoOptionBottomSheet(onCancelCallBack : (() -> Unit) ?= null,
                              onConfirmCallBack : ((typeOption: OptionLiveInfoType) -> Unit) ?= null){
    var typeOption by remember {
        mutableStateOf(OptionLiveInfoType.PIN_OPTION)
    }
    Column(
        modifier = Modifier.background(color = White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 48.dp, end = 48.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val reasons = listOf(
                    stringResource(id = R.string.title_pin_to_spot),
                    stringResource(id = R.string.title_delete),
                    stringResource(id = R.string.title_share_on_social_media),
                )
                GroupRadioButtonSelected(
                    modifier = Modifier,
                    titleText = "",
                    selectionList = reasons,
                    swapPosition = false,
                    hideTitle = false,
                    colorBackground = Color.White,
                    showDivider = false
                ) {
                    when(it){
                        reasons[0] -> {
                            typeOption = OptionLiveInfoType.PIN_OPTION
                        }
                        reasons[1] -> {
                            typeOption = OptionLiveInfoType.DELETE_OPTION
                        }
                        reasons[2] -> {
                            typeOption = OptionLiveInfoType.SHARE_SOCIAL_OPTION
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom
        ){
            GroupButtonBottomCompose(cancelCallback = {
                onCancelCallBack?.invoke()
            }, confirmCallback = {
                onConfirmCallBack?.invoke(typeOption)
            })
        }
    }
}