package olmo.wellness.android.data.repository

import android.content.Context
import olmo.wellness.android.data.model.category.dao.SubCategoryDAO
import olmo.wellness.android.data.model.user_info.UserInfoDAO
import olmo.wellness.android.data.model.user_info.toUserInfoDomain
import olmo.wellness.android.domain.model.UserInfo
import olmo.wellness.android.domain.model.livestream.LiveCategory
import olmo.wellness.android.domain.model.livestream.toSubCategoryEntity
import olmo.wellness.android.domain.model.toUserInfoEntity
import olmo.wellness.android.domain.repository.LocalRepository
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val context: Context,
    private val subCategoryDAO: SubCategoryDAO,
    private val userInfoDAO: UserInfoDAO
) : LocalRepository {

    override suspend fun getSubCategories(): List<Int> {
        return subCategoryDAO.getAllSubCategories().map { it.id }
    }

    override suspend fun saveSubCategories(subcategories: List<LiveCategory>) {
        subcategories.map { it.toSubCategoryEntity() }
            .let { subCategoryDAO.insertSubCategories(it) }
    }

    override suspend fun getUserInfo(userId: Int?): UserInfo? {
        userId?.let {
            return userInfoDAO.getUserInfo(it)?.toUserInfoDomain()
        }
        return userInfoDAO.getAllUserInfo().lastOrNull()?.toUserInfoDomain()
    }

    override suspend fun saveUserInfo(userInfo: UserInfo) {
        userInfoDAO.deleteAllUserInfo()
        userInfoDAO.insertUserInfo(userInfo.toUserInfoEntity())
    }

    override suspend fun updateUserInfo(userInfo: UserInfo) {
        userInfoDAO.updateUserInfo(userInfo.toUserInfoEntity())
    }

    override suspend fun deleteUserInfo() {
        userInfoDAO.deleteAllUserInfo()
    }

}