package olmo.wellness.android.domain.repository

import olmo.wellness.android.domain.model.UserInfo
import olmo.wellness.android.domain.model.livestream.LiveCategory

interface LocalRepository {
    // category
    suspend fun getSubCategories(): List<Int>
    suspend fun saveSubCategories(subcategories: List<LiveCategory>)
    // user info
    suspend fun getUserInfo(userId: Int?): UserInfo?
    suspend fun saveUserInfo(userInfo: UserInfo)
    suspend fun updateUserInfo(userInfo: UserInfo)
    suspend fun deleteUserInfo()
}