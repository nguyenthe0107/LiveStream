package olmo.wellness.android.domain.use_case

import olmo.wellness.android.domain.model.UserInfo
import olmo.wellness.android.domain.repository.LocalRepository
import javax.inject.Inject

class UpdateUserInfoUseCase @Inject constructor(
    private val repository: LocalRepository
) {
    @JvmInline
    value class Params(val userInfo: UserInfo)

    suspend operator fun invoke(params: Params) = repository.updateUserInfo(params.userInfo)
}