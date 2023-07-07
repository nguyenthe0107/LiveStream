package olmo.wellness.android.extension

import android.content.Context
import android.net.Uri
import olmo.wellness.android.core.Constants
import java.io.File

val File.size get() = if (!exists()) 0.0 else length().toDouble()
val File.sizeInKb get() = size / 1024
val File.sizeInMb get() = sizeInKb / 1024
val File.sizeInGb get() = sizeInMb / 1024
val File.sizeInTb get() = sizeInGb / 1024

fun Uri.getImageMimeType(context: Context): String {
    val mimeType = context.contentResolver.getType(this) ?: "image/${Constants.MIME_IMAGE}"
    return mimeType.substring(mimeType.indexOf("/") + 1).ifEmpty { "" }
}
