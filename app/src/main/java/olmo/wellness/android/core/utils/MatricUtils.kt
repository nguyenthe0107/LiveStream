package olmo.wellness.android.core.utils

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics

fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

fun getScreenHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}

fun Float.dpToPx(context: Context): Float {
    return this * (context.resources
        .displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}

fun Int.pxToDp(context: Context): Float {
    return this.toFloat() / (context.resources
        .displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}
