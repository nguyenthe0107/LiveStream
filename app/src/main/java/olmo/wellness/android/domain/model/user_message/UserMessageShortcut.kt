package olmo.wellness.android.domain.model.user_message

import olmo.wellness.android.data.model.user_message_shortcut.UserMessageShortcutDTO

data class UserMessageShortcut(
    val id: Int? = null,
    val messageShortcut: String? = null,
    val userId: List<Int>? = null,
)

fun UserMessageShortcut.toDTO(): UserMessageShortcutDTO = UserMessageShortcutDTO(
    this.id,
    this.messageShortcut,
    this.userId
)