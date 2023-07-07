package olmo.wellness.android.ui.common.permission

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Permission(permission: String = android.Manifest.permission.CAMERA,
               rationale: String = "This permission is important for this app. Please grant the permission.",
               permissionNotAvailableContent: @Composable () -> Unit = { },
               content: @Composable () -> Unit = { }){

    val permissionState = rememberPermissionState(permission)
    PermissionRequired(
        permissionState = permissionState,
        permissionNotGrantedContent = {
            Rationale(
                text = rationale,
                onRequestPermission = { permissionState.launchPermissionRequest() }
            )
        },
        permissionNotAvailableContent = permissionNotAvailableContent,
        content = content
    )
}

@Composable
private fun Rationale(
    text: String,
    onRequestPermission: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* Don't */ },
        title = {
            Text(text = "Permission request", color = Color.Black)
        },
        text = {
            Text(text)
        },
        confirmButton = {
            Button(onClick = onRequestPermission) {
                Text("Ok", style = MaterialTheme.typography.subtitle2.copy(
                    color = Color.White
                ))
            }
        }
    )
}