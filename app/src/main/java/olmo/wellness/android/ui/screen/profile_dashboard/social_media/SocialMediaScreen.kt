package olmo.wellness.android.ui.screen.profile_dashboard.social_media

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.bottom_sheet.showAsBottomSheet
import olmo.wellness.android.ui.common.components.dialog_confirm.DialogAction
import olmo.wellness.android.ui.common.extensions.showMessageResource
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.account_setting.notification_settings.NotificationSettingsList
import olmo.wellness.android.ui.screen.account_setting.notification_settings.NotificationSettingsModel
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SocialMediaScreen(
    navController: NavController,
    viewModel: SocialMediaViewModel = hiltViewModel()
) {
    val maxHeight = 500.dp
    val showDialogConfirm = remember {
        mutableStateOf(false)
    }
    val showDialogConfirmDisconnect = remember {
        mutableStateOf(false)
    }
    var socialModelSelected by remember {
        mutableStateOf<NotificationSettingsModel?>(null)
    }
    val context = LocalContext.current

    val mFirebaseAuth = FirebaseAuth.getInstance()
    if (mFirebaseAuth.currentUser != null) {
        mFirebaseAuth.signOut()
    }
    val signInClient = GoogleSignIn.getClient(
        context,
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(stringResource(id = R.string.gcp_id))
            .requestId()
            .build()
    )
    signInClient.signOut()
    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (result.data != null) {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(intent)
                }
            } else {
                // handle failed
                showMessageResource(
                    context,
                    R.string.tv_login_failed
                )
            }
        }
    Column(
        modifier = Modifier
            .background(Color_gray_FF7)
            .padding(marginStandard)
            .requiredHeight(maxHeight)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        SpaceCompose(height = 8.dp)
        Text(
            text = stringResource(id = R.string.title_social_setting),
            style = MaterialTheme.typography.h6.copy(
                color = Color_Purple_FBC,
                fontWeight = FontWeight.Bold, lineHeight = 26.sp, fontSize = 18.sp
            )
        )
        SpaceCompose(height = 30.dp)
        Column(modifier = Modifier.padding(marginDouble)) {
            NotificationSettingsList(
                listOf(
                    NotificationSettingsModel(
                        1,
                        true,
                        stringResource(id = R.string.title_google_social),
                        R.drawable.olmo_ic_google_purple,
                        SocialType.GOOGLE_SOCIAL
                    ),
                    NotificationSettingsModel(
                        2,
                        false,
                        stringResource(id = R.string.title_tiktok_social),
                        R.drawable.olmo_ic_tiktok_purple,
                        SocialType.TIKTOK_SOCIAL
                    ),
                    NotificationSettingsModel(
                        3,
                        false,
                        stringResource(id = R.string.title_instagram_social),
                        R.drawable.olmo_ic_instagram_purple,
                        SocialType.INSTAGRAM_SOCIAL
                    )
                )
            ) { newData, itemChanged ->
                socialModelSelected = itemChanged
                if(itemChanged.isSelected){
                    showDialogConfirm.value = true
                }else{
                    showDialogConfirmDisconnect.value = true
                }
            }
        }
    }

    val titleDialog = "\"Kepler\" wants to use\n \"${socialModelSelected?.optionName}\" to Sign In"
    val des = "This allows the app and website to share information about you"
    DialogAction(openDialogCustom = showDialogConfirm, title = titleDialog,
        description = des,
        titleBtnCancel = stringResource(id = R.string.cancel),
        titleBtnConfirm = stringResource(id = R.string.continue_value),
        btnCancelCallback = {
            showDialogConfirm.value = false
        }, btnConfirmCallback = {
            showDialogConfirm.value = false
            if(socialModelSelected?.optionName == context.getString(R.string.title_google_social)){
                startForResult.launch(signInClient.signInIntent)
            }else{
                (context as MainActivity).showAsBottomSheet { 
                    WebViewSocialMediaScreen(navController = navController, socialModelSelected?.socialType)
                }
            }
        })

    val titleDialogDisconnect = "Are you sure you want to\n disconnect your account?"
    DialogAction(openDialogCustom = showDialogConfirmDisconnect, title = titleDialogDisconnect,
        description = des,
        titleBtnCancel = stringResource(id = R.string.cancel),
        titleBtnConfirm = stringResource(id = R.string.continue_value),
        btnCancelCallback = {
            showDialogConfirmDisconnect.value = false
        }, btnConfirmCallback = {
            showDialogConfirmDisconnect.value = false
        })
}

