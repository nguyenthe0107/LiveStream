package olmo.wellness.android.ui.common.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

@SuppressLint("Range")
fun getFileName(context: Context, uri: Uri): String? {
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor.use {
            if(cursor?.moveToFirst() == true) {
                return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
    }
    return if(uri.path != null && uri.path?.isNotEmpty() == true){
        uri.path?.lastIndexOf('/')?.plus(1)?.let { uri.path?.substring(it) }
    }else{
        ""
    }
}