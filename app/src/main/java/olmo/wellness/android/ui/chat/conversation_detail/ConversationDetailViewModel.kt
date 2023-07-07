package olmo.wellness.android.ui.chat.conversation_detail

import android.app.Application
import dagger.hilt.android.lifecycle.HiltViewModel
import olmo.wellness.android.core.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ConversationDetailViewModel @Inject constructor(application: Application) :
    BaseViewModel(application) {

//    private val _messageList = MutableStateFlow(newUiState.messages)
//    val messageList: StateFlow<List<Message>> = _messageList


//    fun addMessage(message: Message) {
//        (_messageList.value as MutableList).add(0,message)
//    }
}