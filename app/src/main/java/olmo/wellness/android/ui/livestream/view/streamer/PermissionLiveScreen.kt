package olmo.wellness.android.ui.livestream.view.streamer

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import olmo.wellness.android.core.hideSystemUI
import olmo.wellness.android.ui.livestream.utils.Effects
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.theme.*

val requiredPermissions = listOf(
    Manifest.permission.CAMERA,
    Manifest.permission.RECORD_AUDIO
)

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalFoundationApi::class)
@Composable
fun PermissionLiveScreen(navController: NavController){
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    var isLoading by remember{
        mutableStateOf(true)
    }
    (context as MainActivity).hideSystemUI()
    //-- check permissions
    val permissionStates = rememberMultiplePermissionsState(
        permissions = requiredPermissions
    )
    val missingPermissions =
        requiredPermissions.filter { requiredPermission ->
            permissionStates.permissions.none { requestedPermission ->
                requestedPermission.hasPermission && requestedPermission.permission == requiredPermission
            }
        }
    Effects.Disposable(
        lifeCycleOwner = lifecycleOwner,
        onStart = {
            permissionStates.launchMultiplePermissionRequest()
        },
        onStop = {
        }
    )
    var hasPermissionCamera by remember {
        mutableStateOf(false)
    }
    var hasPermissionAudio by remember {
        mutableStateOf(false)
    }
    permissionStates.permissions.forEach {
        when(it.permission) {
            Manifest.permission.CAMERA -> {
                if (it.hasPermission) {
                    hasPermissionCamera = true
                }
            }
            Manifest.permission.RECORD_AUDIO -> {
                if (it.hasPermission) {
                    hasPermissionAudio = true
                }
            }
        }
    }
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
            .background(Color.Black.copy(alpha = 0.6f))){
        if (missingPermissions.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    color = Color.Green,
                    text = "App needs following permissions",
                )
                missingPermissions.map { p -> Text(p) }
                Button(
                    onClick = {
                        context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        })
                    }) {
                    Text("Goto Settings")
                }
            }
        }else{
            if(hasPermissionCamera && hasPermissionAudio){
                LaunchedEffect(true){
                    navController.popBackStack()
                    navController.navigate(ScreenName.LivestreamStreamerScreen.route)
                    isLoading = false
                }
            }else{
                LaunchedEffect(true){
                    navController.popBackStack()
                    isLoading = false
                }
            }
        }
    }
    LoaderWithAnimation(isPlaying = isLoading)
}
