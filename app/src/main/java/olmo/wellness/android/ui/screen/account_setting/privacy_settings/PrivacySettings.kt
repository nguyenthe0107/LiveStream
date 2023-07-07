package olmo.wellness.android.ui.screen.account_setting.privacy_settings

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.user_info.UserInfoLocal
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.*
import olmo.wellness.android.ui.common.bottom_sheet.showAsBottomSheet
import olmo.wellness.android.ui.common.components.dialog_confirm.DialogAction
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.business_hours.AvatarMascot
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.profile_dashboard.social_media.SocialMediaScreen
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PrivacySettings(
    navController: NavController,
    viewModel: PrivacySettingViewModel = hiltViewModel())
{
    val privacyController: MutableState<Boolean>? = null

    val vacationModeController: MutableState<Boolean>? = null

    val userInfoLocal = viewModel.userLocal.collectAsState()
    val userInfoLocalFinal by remember {
        mutableStateOf(userInfoLocal.value)
    }
    val showDialogPrivacyPositive = remember {
        mutableStateOf(false)
    }

    val showDialogVacationPositive = remember {
        mutableStateOf(false)
    }

    val defaultPrivateMode = viewModel.defaultPrivateMode.collectAsState()
    val context = LocalContext.current

    Scaffold(topBar = {
        ToolbarSchedule(
            title = stringResource(id = R.string.title_privacy_setting),
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
                    PrivateActivity(defaultPrivateMode.value?:false){ controller ->
                        privacyController?.value = controller
                        showDialogPrivacyPositive.value = controller
                    }

                    VacationMode(defaultPrivateMode.value?:false){
                        vacationModeController?.value = it
                        showDialogVacationPositive.value = it
                    }
                    
                    BlockContact(navController)
                    SocialMediaAccount(navController, context)
                    PasswordItem(navController, userInfoLocalFinal)
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

    DialogAction(openDialogCustom = showDialogPrivacyPositive,title = stringResource(id = R.string.title_dialog_change_privacy),
        description = stringResource(id = R.string.des_dialog_change_privacy),
        titleBtnCancel = stringResource(id = R.string.no),
        titleBtnConfirm = stringResource(id = R.string.yes),
        btnCancelCallback= {
            showDialogPrivacyPositive.value = false
        }, btnConfirmCallback = {
            showDialogPrivacyPositive.value = false
        })

    DialogAction(openDialogCustom = showDialogVacationPositive,title = stringResource(id = R.string.title_dialog_change_vocation),
        description = stringResource(id = R.string.des_dialog_change_vocation),
        btnCancelCallback= {
            showDialogVacationPositive.value = false
        }, btnConfirmCallback = {
            showDialogVacationPositive.value = false
        },
        titleBtnCancel = stringResource(id = R.string.no),
        titleBtnConfirm = stringResource(id = R.string.yes),)
}

@Composable
private fun BlockContact(navController:  NavController) {
    TextViewDrawable(
        text = stringResource(id = R.string.title_block_contacts),
        drawableRes = R.drawable.olmo_ic_arrow_right_bold,
        modifier = Modifier
            .padding(
                top = 16.dp
            )
            .noRippleClickable {
                navController.navigate(ScreenName.BlockContacts.route)
            }
    )
}

@Composable
private fun SocialMediaAccount(navController:  NavController, context: Context?) {
    TextViewDrawable(
        text = stringResource(id = R.string.title_privacy_social_media_accounts),
        drawableRes = R.drawable.olmo_ic_arrow_right_bold,
        modifier = Modifier
            .padding(
                top = 16.dp
            )
            .noRippleClickable {
                context?.let {
                    (context as MainActivity).showAsBottomSheet {
                        SocialMediaScreen(navController = navController)
                    }
                }
            }
    )
}

@Composable
private fun PasswordItem(navController:  NavController, userInfoLocal: UserInfoLocal?) {
    TextViewDrawable(
        text = stringResource(id = R.string.title_privacy_password),
        drawableRes = R.drawable.olmo_ic_arrow_right_bold,
        modifier = Modifier
            .padding(
                top = 16.dp
            )
            .noRippleClickable {
                val identity = userInfoLocal?.identity ?: ""
                navController.navigate(
                    ScreenName.CreatePasswordScreen.route
                        .plus("?identity=$identity")
                        .plus("&code=123456")
                )
            }
    )
}

@Composable
private fun PrivateActivity(
    defaultValue: Boolean,
    setupController: (Boolean) -> Unit
){
    var statusSwitch by remember {
        mutableStateOf(defaultValue)
    }
    ItemSwitch(
        title = stringResource(id = R.string.title_privacy_activity),
        onSwitch = {
            statusSwitch = it
            setupController.invoke(it)
        },
        switchDefault = statusSwitch
    )

    Text(
        text = stringResource(id = R.string.des_privacy_activity),
        style = MaterialTheme.typography.caption.copy(
            fontWeight = FontWeight.Medium,
            color = Neutral_Gray_6
        ),
        modifier = Modifier.padding(
            horizontal = 16.dp,
            vertical = 6.dp
        ),
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun VacationMode(
    defaultValue: Boolean,
    setupController: (Boolean) -> Unit
){
    var statusSwitch by remember {
        mutableStateOf(defaultValue)
    }
    ItemSwitch(
        title = stringResource(id = R.string.title_privacy_vacation_mode),
        onSwitch = {
            statusSwitch = it
            setupController.invoke(it)
        },
        switchDefault = statusSwitch
    )

    Text(
        text = stringResource(id = R.string.des_privacy_vacation_mode),
        style = MaterialTheme.typography.caption.copy(
            fontWeight = FontWeight.Medium,
            color = Neutral_Gray_6
        ),
        modifier = Modifier.padding(
            horizontal = 16.dp,
            vertical = 6.dp
        ),
        overflow = TextOverflow.Ellipsis
    )
}

