package olmo.wellness.android.ui.common.components.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.net.toUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import olmo.wellness.android.ui.common.permission.Permission
import olmo.wellness.android.ui.theme.marginDouble
import olmo.wellness.android.ui.theme.marginStandard
import olmo.wellness.android.ui.theme.sizeIcon_100
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val MAX_IMAGE_SIZE = 5 * 1024
private const val IMAGE_FILE_NAME = "yyyy-MM-dd-HH-mm-ss-SSS"
private const val PHOTO_EXTENSION = ".jpg"
@ExperimentalPermissionsApi
@ExperimentalCoroutinesApi
@Composable
fun CameraCapture(
    modifier: Modifier = Modifier,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    onImageFile: (File) -> Unit = { },
    onClick: ((status: Boolean) -> Unit)? = null
) {
    val context = LocalContext.current
    Permission(
        permission = Manifest.permission.CAMERA,
        rationale = "You said you wanted a picture, so I'm going to have to ask for permission.",
        permissionNotAvailableContent = {
            Column(modifier) {
                Text("O noes! No Camera!")
                Spacer(modifier = Modifier.height(marginStandard))
                Button(
                    onClick = {
                        context.startActivity(
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                            }
                        )
                    }
                ) {
                    Text("Open Settings")
                }
            }
        }
    ) {
        Box(modifier = modifier) {
            val lifecycleOwner = LocalLifecycleOwner.current
            val coroutineScope = rememberCoroutineScope()
            var previewUseCase by remember { mutableStateOf<UseCase>(Preview.Builder().build()) }
            val imageCaptureUseCase by remember {
                mutableStateOf(
                    ImageCapture.Builder()
                        .setCaptureMode(CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build()
                )
            }
            Box {
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    onUseCase = {
                        previewUseCase = it
                    }
                )
                CapturePictureButton(
                    modifier = Modifier
                        .size(sizeIcon_100)
                        .padding(marginDouble)
                        .align(Alignment.BottomCenter),
                    onClick = {
                        onClick?.invoke(true)
                        coroutineScope.launch {
                            val file = imageCaptureUseCase.takePicture(context)
                            onImageFile(file)
                        }
                    }
                )
            }
            LaunchedEffect(previewUseCase) {
                val cameraProvider = context.getCameraProvider()
                try {
                    // Must unbind the use-cases before rebinding them.
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, previewUseCase, imageCaptureUseCase
                    )
                } catch (ex: Exception) {
                    Log.e("CameraCapture", "Failed to bind camera use cases", ex)
                }
            }
        }
    }
}

private fun getExternalDirectory(context: Context): File? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    } else {
        context.applicationContext.filesDir
    }
}

@SuppressLint("SimpleDateFormat")
suspend fun ImageCapture.takePicture(context: Context): File {
    val photoFile = withContext(Dispatchers.IO) {
        kotlin.runCatching {
            // Create an image file name
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir = getExternalDirectory(context)
            val tempFile = File.createTempFile(
                "JPEG_${timeStamp}_", //prefix
                ".jpg", //suffix
                storageDir //directory
            )
            tempFile
        }.getOrElse {
            File("/dev/null")
        }
    }

    return suspendCoroutine { continuation ->
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        takePicture(
            outputOptions, context.executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    continuation.resume(photoFile)
                }

                override fun onError(ex: ImageCaptureException) {
                    continuation.resumeWithException(ex)
                }
            }
        )
    }
}

fun resizeAndCompress(context: Context, contentResolver: ContentResolver, uri: Uri?): File {
    // First decode with inJustDecodeBounds=true to check dimensions of image
    val cacheDir by lazy { context.cacheDir }
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(uri?.path, options)

    // Calculate inSampleSize(First we are going to resize the image to 800x800 image, in order to not have a big but very low quality image.
    //resizing the image will already reduce the file size, but after resizing we will check the file size and start to compress image
    options.inSampleSize = calculateInSampleSize(options, 800, 800)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    options.inPreferredConfig = Bitmap.Config.ARGB_8888
    val bmpPic: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
//        val bmpPic: Bitmap = BitmapFactory.decodeFile(uri?.path, options)
    var compressQuality = 100 // quality decreasing by 5 every loop.
    var streamLength: Int

    do {
        val bmpStream = ByteArrayOutputStream()
        bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray: ByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength >= MAX_IMAGE_SIZE)

    try {
        //save the resized and compressed file to disk cache
        val path = cacheDir.toString() + SimpleDateFormat(IMAGE_FILE_NAME, Locale.getDefault())
            .format(System.currentTimeMillis()) + PHOTO_EXTENSION
        val bmpFile = FileOutputStream(path)
        bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile)
        bmpFile.flush()
        bmpFile.close()

        return File(path)
    } catch (e: Exception) {
        Log.e("WTF", " compressBitmap $e")
    }

    return File(uri?.path.orEmpty())
}

private fun calculateInSampleSize(
    options: BitmapFactory.Options,
    reqWidth: Int,
    reqHeight: Int
): Int {
    val height = options.outHeight
    val width = options.outWidth

    var inSampleSize = 1
    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2

        while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}


