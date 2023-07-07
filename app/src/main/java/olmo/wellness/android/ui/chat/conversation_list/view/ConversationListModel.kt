package olmo.wellness.android.ui.chat.conversation_list.view

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Result
import olmo.wellness.android.domain.use_case.RoomChatUseCase
import olmo.wellness.android.ui.base.BaseViewModel
import olmo.wellness.android.ui.chat.conversation_list.event.ConversationListEvent
import olmo.wellness.android.ui.chat.conversation_list.event.ConversationListState
import javax.inject.Inject

@HiltViewModel
class ConversationListModel @Inject constructor(
    private val getRoomChatUseCase: RoomChatUseCase,
) : BaseViewModel<ConversationListState, ConversationListEvent>() {

    fun getListRoom(page: Int, search: String?) {
        viewModelScope.launch {
            val keySearch = ((if (search==null) {
                null
            } else {
                "\"${search}\""
            }))
            getRoomChatUseCase.getRoomListChatSingle(page = page, search = keySearch)
                .collectLatest { response ->
                    when (response) {
                        is Result.Loading -> {
                            if (page==1) {
                                triggerStateEvent(ConversationListEvent.ShowLoading(true))
                            }
                        }
                        is Result.Success -> {
                            if (response.data != null) {
                                if (page == 1) {
                                    triggerStateEvent(
                                        ConversationListEvent.UpdateListRoomConversation(
                                            response.data.records
                                        )
                                    )
                                } else {
                                    val temp = uiState.value.listRoomConversation as? MutableList
                                    temp.let {
                                        it?.addAll(response.data.records)
                                        triggerStateEvent(
                                            ConversationListEvent.UpdateListRoomConversation(
                                                it
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        is Result.Error -> {
                            triggerStateEvent(ConversationListEvent.ShowLoading(false))

                        }
                    }
                }
        }
    }

    override fun initState(): ConversationListState {
        return ConversationListState()
    }

    override fun onTriggeredEvent(event: ConversationListEvent) {
        when (event) {
            is ConversationListEvent.ShowLoading -> {
                setState(
                    uiState.value.copy(
                        showLoading = event.isLoading
                    )
                )
            }
            is ConversationListEvent.UpdateListRoomConversation -> {
                setState(
                    uiState.value.copy(
                        showLoading = false,
                        listRoomConversation = event.listRoom
                    )
                )
            }
        }
    }

}