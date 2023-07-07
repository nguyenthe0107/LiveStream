package olmo.wellness.android.ui.screen.account_setting.chat_setting.message_shortcut.create

data class CreateMessageShortcutState(
    val isValid: Boolean? = null,
    val errMessage: String = "",
    val message: String? = null,
    val showLoading: Boolean = false,
    val savedSuccess: Boolean = false
)

sealed class CreateMessageShortcutEvent{
    data class Validate(
        val isValid: Boolean? = null,
        val errMessage: String = "",
        val message: String? = null
    ): CreateMessageShortcutEvent()

    data class ShowLoading(
        val showLoading: Boolean
    ): CreateMessageShortcutEvent()

    data class SaveMessageSuccess(
        val savedSuccess: Boolean
    ): CreateMessageShortcutEvent()
}
