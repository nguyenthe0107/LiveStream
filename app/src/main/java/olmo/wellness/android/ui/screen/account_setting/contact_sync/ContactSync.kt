package olmo.wellness.android.ui.screen.account_setting.contact_sync

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.ItemSwitch
import olmo.wellness.android.ui.common.ToolbarSchedule
import olmo.wellness.android.ui.screen.business_hours.AvatarMascot
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ContactSync(
    navController: NavController,
    viewModel: ContactSyncViewModel = hiltViewModel()
) {
    val isSuccess = viewModel.isSuccess.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val defaultValue = viewModel.defaultContactSyncMode.collectAsState()


    Scaffold(topBar = {
        ToolbarSchedule(
            title = stringResource(R.string.lb_contact_syncing),
            backIconDrawable = R.drawable.ic_back_calendar,
            navController = navController,
            backgroundColor = Color_LiveStream_Main_Color
        )
    }) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(Color_LiveStream_Main_Color)
        ) {
            val (options, imageCompose, endCompose) = createRefs()
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
                ) {

                    Spacer(modifier = Modifier.padding(vertical = 20.dp))

                    ItemSwitch(title = stringResource(R.string.lb_allow_kepler_to_access_contact_list), modifier = Modifier,switchDefault =defaultValue.value?:false,onSwitch ={
                        viewModel.updateUserSetting(it)

                    })
                }
            }

            AvatarMascot(modifier = Modifier.constrainAs(imageCompose) {
                top.linkTo(parent.top, 15.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, uri = null, callbackFun = {
            }, src = R.drawable.ic_business_hours)

        }
    }

    LoadingScreen(isLoading = isLoading.value)
}