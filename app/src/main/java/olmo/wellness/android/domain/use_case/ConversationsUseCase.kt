package olmo.wellness.android.domain.use_case

import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject

class ConversationsUseCase @Inject constructor(
    private val apiRepository: ApiRepository
) {
    operator fun invoke(query: String) = apiRepository.getConversations(query)

}