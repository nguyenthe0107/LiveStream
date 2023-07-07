package olmo.wellness.android.data.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class LanguageModel(
    val id: Int,
    val languageName: String,
    val code: String,
    val select : MutableState<Boolean> = mutableStateOf(false)
){
    companion object {
        val ENGLISH = LanguageModel(1, "English", "english")
        val VIETNAM= LanguageModel(2,"Vietnamese","vietnamese")
    }
}