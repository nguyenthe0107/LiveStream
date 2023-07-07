package olmo.wellness.android.data.model.user_message_shortcut

import olmo.wellness.android.domain.model.user_message.UserMessageShortcut

data class UserMessageShortcutDTO(
    val id: Int? = null,
    val messageShortcut: String? = null,
    val userId: List<Int> ? = null,
)

fun UserMessageShortcutDTO.toUserMessageShortcutDomain(): UserMessageShortcut{
    return UserMessageShortcut(
        this.id,
        this.messageShortcut,
        this.userId
    )
}