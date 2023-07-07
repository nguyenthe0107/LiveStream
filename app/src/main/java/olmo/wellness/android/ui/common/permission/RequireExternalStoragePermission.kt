package olmo.wellness.android.ui.common.permission

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import olmo.wellness.android.ui.theme.Neutral_Gray_9
import olmo.wellness.android.ui.theme.marginDouble

@ExperimentalPermissionsApi
@Composable
fun RequireExternalStoragePermission(
    navigateToSettingsScreen: () -> Unit,
    content: @Composable() () -> Unit
) {
    // Track if the user doesn't want to see the rationale any more.
    var doNotShowRationale by remember { mutableStateOf(false) }

    // Permission state
    val permissionState = rememberPermissionState(
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )
    val context = LocalContext.current
    when {
        // If the camera permission is granted, then show screen with the feature enabled
        permissionState.hasPermission -> {
        }
        // If the user denied the permission but a rationale should be shown, or the user sees
        // the permission for the first time, explain why the feature is needed by the app and allow
        // the user to be presented with the permission again or to not see the rationale any more.
        permissionState.shouldShowRationale ||
                !permissionState.permissionRequested -> {
            if (doNotShowRationale) {
                Text("Feature not available")
            } else {
                Column(Modifier.padding(marginDouble)) {
                    Text("Need to read external storage to import photos. Please grant the permission.",
                    style = MaterialTheme.typography.subtitle2.copy(
                        color = Neutral_Gray_9,
                    ))
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Button(onClick = { permissionState.launchPermissionRequest() }) {
                            Text("Request permission", style = MaterialTheme.typography.subtitle2.copy(
                                color = Color.White
                            ))
                        }
                        Spacer(Modifier.width(10.dp))
                        Button(onClick = { doNotShowRationale = true }) {
                            Text("Don't show rationale again", style = MaterialTheme.typography.subtitle2.copy(
                                color = Color.White
                            ))
                        }
                    }
                }
            }
        }
        // If the criteria above hasn't been met, the user denied the permission. Let's present
        // the user with a FAQ in case they want to know more and send them to the Settings screen
        // to enable it the future there if they want to.
        else -> {
            Column(modifier = Modifier.padding(marginDouble)) {
                Text(
                    "External storage permission denied. " +
                            "Need to read external storage to import photos. " +
                            "Please grant access on the Settings screen.",
                    style = MaterialTheme.typography.subtitle2.copy(
                        color = Neutral_Gray_9,
                        fontFamily = FontFamily.Monospace)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    context.startActivity(
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                    )
                }){
                    Text("Open Settings")
                }
            }
        }
    }
}