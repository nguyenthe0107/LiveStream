package olmo.wellness.android.core

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.*
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import olmo.wellness.android.ui.common.bottom_sheet.showAsBottomSheet


inline fun <reified T> Any.cast(): T {
    return this as T
}

fun Any.toJson(): String = Gson().toJson(this)

inline fun <reified T> fromJson(json: String): T{
    return Gson().fromJson(json, T::class.java)
}

fun fieldsOf(vararg fields: String): String{
    return listOf(*fields).map {
        "\"$it\""
    }.toString()
}

@ExperimentalMaterialApi
suspend fun ModalBottomSheetState.expand(){
    this.animateTo(ModalBottomSheetValue.Expanded)
}

fun LazyGridState.isScrolledToTheEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

fun Context.maybeShowPipActivity(intent: Intent){
    if (this.packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)) {
        startActivity(
            intent.apply {
                addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            }
        )
    }
    else {
        Log.e("Failed Pip", "could not be showed")
    }
}

fun Activity.hideSystemUI() {
    //Hides the ugly action bar at the top
    //actionBar?.hide()
    //Hide the status bars
    //WindowCompat.setDecorFitsSystemWindows(window, false)
    /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    } else {
        window.insetsController?.apply {
            hide(WindowInsets.Type.statusBars())
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }*/
}

fun Activity.hideSystemUIUpdate() {
    //Hides the ugly action bar at the top
    actionBar?.hide()
    //Hide the status bars
    WindowCompat.setDecorFitsSystemWindows(window, false)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    } else {
        window.insetsController?.apply {
            hide(WindowInsets.Type.statusBars())
            hide(WindowInsets.Type.navigationBars())
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}

@SuppressLint("InlinedApi")
fun Activity.showSystemUIUpdate() {
    actionBar?.show()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(false)
        window.insetsController?.apply {
            show(WindowInsets.Type.statusBars())
            show(WindowInsets.Type.navigationBars())
        }
        try {
            val viewGroup = this.findViewById(android.R.id.content) as ViewGroup
            ViewCompat.setOnApplyWindowInsetsListener(viewGroup) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
                viewGroup.apply {
                    setPaddingRelative(paddingStart, paddingTop, paddingEnd, systemBars.bottom)
                }
                return@setOnApplyWindowInsetsListener insets
            }
        }catch (ex: Exception){
        }
    } else {
        // Show status bar
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_STABLE
        // Show Status Bar.
        window.decorView.systemUiVisibility = (SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
}

fun Int.formatToK(): String {
    return  if (this < 1000) "$this" else "${this/1000}K"
}

fun dpToPxUtils(context: Context, dpValue: Float): Float{
    return dpValue * context.resources.displayMetrics.density
}

fun hideForceKeyboard(activity: Activity){
    val inputMethodManager = activity.getSystemService(
        Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    if (inputMethodManager.isAcceptingText) {
        inputMethodManager.hideSoftInputFromWindow(
            activity.currentFocus?.windowToken,
            0
        )
    }
}

fun formatToVND(money: Float): String {
    return money.toString().plus("đ")
}

