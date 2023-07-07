package olmo.wellness.android.ui.screen.account_setting.chat_setting.message_shortcut.create

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Result
import olmo.wellness.android.domain.model.user_message.UserMessageShortcut
import olmo.wellness.android.domain.use_case.UserMessageShortcutUseCase
import olmo.wellness.android.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class CreateMessageShortcutViewModel @Inject constructor(
    private val userMessageShortcut: UserMessageShortcutUseCase,
) : BaseViewModel<CreateMessageShortcutState, CreateMessageShortcutEvent>() {

    private var _createType: CreateType = CreateType.CREATE
    val createType get() = _createType

    private var _messageShortcutId: Int? = null

    private val _createMessageShortcut: MutableSharedFlow<Result<List<UserMessageShortcut>>> = MutableSharedFlow()
    private val _updateMessageShortcut: MutableSharedFlow<Result<List<UserMessageShortcut>>> = MutableSharedFlow()

    init {
        initObserver()
    }

    override fun onTriggeredEvent(event: CreateMessageShortcutEvent) {
        when(event){
            is CreateMessageShortcutEvent.Validate -> {
                setState(
                    uiState.value.copy(
                        errMessage = event.errMessage,
                        isValid = event.isValid,
                        message = event.message,
                        showLoading = false
                    )
                )
            }
            is CreateMessageShortcutEvent.SaveMessageSuccess -> {
                setState(
                    uiState.value.copy(
                        savedSuccess = event.savedSuccess,
                        showLoading = false
                    )
                )
            }
            is CreateMessageShortcutEvent.ShowLoading -> {
                setState(uiState.value.copy(
                    showLoading = event.showLoading
                ))
            }
        }
    }

    fun validateMessage(msg: String){
        when {
            msg.isEmpty() -> {
                triggerStateEvent(
                    CreateMessageShortcutEvent.Validate(
                        isValid = false,
                        errMessage = "Message should not be empty",
                        message = msg
                    )
                )
            }
            msg.length > 500 -> {
                triggerStateEvent(
                    CreateMessageShortcutEvent.Validate(
                        isValid = false,
                        errMessage = "Message length should not be greater than 500",
                        message = msg
                    )
                )
            }
            else ->  triggerStateEvent(
                CreateMessageShortcutEvent.Validate(
                    isValid = true,
                    message = msg
                )
            )
        }
    }


    fun setDefaultMessage(msg: UserMessageShortcut?){
      when {
            msg != null && !msg.messageShortcut.isNullOrEmpty() -> {
                validateMessage(msg = msg.messageShortcut)
                _messageShortcutId = msg.id
                _createType = CreateType.UPDATE
            }
            else -> {
                _createType = CreateType.CREATE
            }
        }
    }

    override fun initState(): CreateMessageShortcutState {
        return CreateMessageShortcutState()
    }

    fun saveMessageShortcut(msgString: String){
        if (msgString.isEmpty())
            return

        if (this._createType == CreateType.UPDATE)
            updateMessageShortcut(msgString)
        else
            createMessageShortcut(msgString)
    }

    private fun createMessageShortcut(msg: String){
        viewModelScope.launch {
            _createMessageShortcut.emit(Result.Loading())
            userMessageShortcut.createMessageShortcuts(msg).collectLatest {
                _createMessageShortcut.emit(it)
            }
        }
    }

    private fun updateMessageShortcut(msg: String){
        if (msg.isEmpty() || _messageShortcutId == null)
            return

        viewModelScope.launch {
            _updateMessageShortcut.emit(Result.Loading())
            val fields = "[\"id\",\"messageShortcut\"]"
            userMessageShortcut.updateMessageShortcut(
                messageShortcut = UserMessageShortcut(
                    messageShortcut = msg,
                    id = _messageShortcutId
                ),
                fields = fields
            ).collectLatest {
                _updateMessageShortcut.emit(it)
            }
        }
    }

    private fun initObserver() {
        viewModelScope.launch {
            _createMessageShortcut.collectLatest {
                it.onResultReceived(
                    onSuccess = {
                        triggerStateEvent(CreateMessageShortcutEvent.ShowLoading(false))
                    },
                    onLoading = {
                        triggerStateEvent(CreateMessageShortcutEvent.ShowLoading(true))
                    }
                ) {
                    triggerStateEvent(CreateMessageShortcutEvent.ShowLoading(false))
                }
            }
        }

        viewModelScope.launch {
            _updateMessageShortcut.collectLatest {
                it.onResultReceived(
                    onSuccess = {
                        triggerStateEvent(CreateMessageShortcutEvent.ShowLoading(false))
                    },
                    onLoading = {
                        triggerStateEvent(CreateMessageShortcutEvent.ShowLoading(true))
                    }
                ) {
                    triggerStateEvent(CreateMessageShortcutEvent.ShowLoading(false))
                }
            }
        }
    }
}