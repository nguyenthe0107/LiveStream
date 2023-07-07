package olmo.wellness.android.extension

import android.app.Activity
import android.app.Application
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import com.google.accompanist.insets.LocalWindowInsets
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.domain.model.user_follow.UserFollowInfo
import olmo.wellness.android.sharedPrefs


fun hideKeyboard(activity: Activity) {
    val imm: InputMethodManager =
        activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = activity.currentFocus
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}


fun String?.subName(): String {
    if (this?.isBlank() == false) return if (this.length <= 10) {
        this
    } else {
        substring(0, 9) + "..."
    }
    return ""
}


fun getNameUserChat(user: User?): String {
    return  if (user?.store?.name?.isBlank()==false){
        user.store.name
    }else
    if (!user?.name.isNullOrBlank()) {
        user?.name.orEmpty()
    } else {
        if (!user?.email.isNullOrBlank()) {
            user?.email.orEmpty()
        } else "Kepler"
    }
}

fun getNameUserFollow(user: UserFollowInfo?): String {
    return  if (user?.storeModel?.name?.isEmpty()==false){
        user.storeModel.name
    }else
        if (user?.firstName?.isNotEmpty() == true || user?.lastName?.isNotEmpty() == true) {
            user.firstName.plus(" ").plus(user.lastName)
        } else {
            "Kepler"
        }
}


fun getUserChat(listUsers: List<User>?): User? {
    val index = listUsers?.indexOfFirst { it.id != sharedPrefs.getUserInfoLocal().userId }
    return if (index != null && index >= 0) {
        listUsers[index]
    } else {
        null
    }
}


//fun getNameChatMessage(message: ChatMessage?): String {
//    return if (!message?.userName.isNullOrBlank()) message?.userName!! else {
//        if (!message?.userEmail.isNullOrBlank()) message?.userEmail!! else "Kepler"
//    }
//}

fun CoroutineScope.showToast(application : Application, text : String?){
    launch(Dispatchers.Main){
        Toast.makeText(application, text, Toast.LENGTH_SHORT).show()
    }
}

