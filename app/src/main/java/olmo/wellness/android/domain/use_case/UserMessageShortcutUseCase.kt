package olmo.wellness.android.domain.use_case

import kotlinx.coroutines.flow.Flow
import olmo.wellness.android.core.Result
import olmo.wellness.android.domain.model.user_message.UserMessageShortcut
import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject
import javax.inject.Provider

class UserMessageShortcutUseCase @Inject constructor(
    private val repository: Provider<ApiRepository>
) {
    fun getMessageShortcuts(userId: Int, queryString: String) : Flow<Result<List<UserMessageShortcut>>>
        = repository.get().getUserMessageShortcut(userId, queryString)

    fun createMessageShortcuts(msgShortcut: String)
        = repository.get().createUserMessageShortcut(msgShortcut)

    fun updateMessageShortcut(
        messageShortcut: UserMessageShortcut,
        fields: String,
        returning: Boolean = true
    ) = repository.get().updateUserMessageShortcut(
        messageShortcut = messageShortcut,
        fields = fields,
        returning = returning
    )
}