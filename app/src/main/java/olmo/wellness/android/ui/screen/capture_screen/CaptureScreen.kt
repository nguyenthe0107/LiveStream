package olmo.wellness.android.ui.screen.capture_screen

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import id.zelory.compressor.Compressor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import olmo.wellness.android.ui.common.components.camera.CameraCapture
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@ExperimentalPermissionsApi
@Composable
fun CaptureScreen(
    isDisplay: Boolean,
    callbackUri: ((uriSelected: Uri?) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var uriSelected by remember {
        mutableStateOf<Uri?>(null)
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    if (isDisplay)
        Scaffold(
            topBar = {
                TopAppBar(
                    backgroundColor = Color.Black,
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = {
                            uriSelected = null
                            callbackUri?.invoke(uriSelected)
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                tint = Color.White,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        ) {
            Box {
                Surface(
                    Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    if (uriSelected != null) {
                        Box(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(vertical = marginDouble, horizontal = marginDouble)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Center),
                            ) {
                                Image(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = marginDouble, end = marginDouble),
                                    painter = rememberImagePainter(data = uriSelected),
                                    contentDescription = "Captured image",
                                    contentScale = ContentScale.Inside
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = padding_20),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = marginMinimum)
                                        .height(height_44),
                                    shape = RoundedCornerShape(cornerRadius),
                                    border = BorderStroke(defaultBorderWidth, Tiffany_Blue_500),
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = Tiffany_Blue_500,
                                        backgroundColor = Color.White
                                    ),
                                    onClick = {
                                        uriSelected = null
                                    }
                                ) {
                                    Text("Retake")
                                }

                                Button(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = marginMinimum)
                                        .height(height_44),
                                    shape = RoundedCornerShape(cornerRadius),
                                    border = BorderStroke(defaultBorderWidth, Color.Transparent),
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = Color.White,
                                        backgroundColor = Tiffany_Blue_500
                                    ),
                                    onClick = {
                                        callbackUri?.invoke(uriSelected)
                                        uriSelected = null
                                    }
                                ) {
                                    Text("Use Photo")
                                }
                            }
                        }
                    } else {
                        CameraCapture(
                            modifier = modifier,
                            onImageFile = { file ->
                                scope.launch{
                                    val compressedImageFile = Compressor.compress(context, file)
                                    val imageUri = FileProvider.getUriForFile(
                                        context,
                                        context.packageName + ".fileprovider",
                                        compressedImageFile
                                    )
                                    uriSelected = imageUri
                                }
                                isLoading = false
                            },
                            onClick = {
                                isLoading = true
                            }
                        )
                    }
                }
                LoadingScreen(isLoading = isLoading)
            }
        }
}
