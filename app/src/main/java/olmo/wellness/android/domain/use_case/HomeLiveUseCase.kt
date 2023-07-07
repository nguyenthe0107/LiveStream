package olmo.wellness.android.domain.use_case

import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject

class HomeLiveUseCase @Inject constructor(
    val repository: ApiRepository
) {
    fun getListCategories() = repository.getCategories()
    fun getSections(isMySelf: Int?, userId: Int?) = repository.getSections(isMySelf, userId)
    fun getLivestreamFilter(
        typeTitle: String?, startTime: Long?, endTime: Long?,categoryId : Int? ,title: String? = null,
        limit: Int? = null, page: Int? = null, isMySelf: Boolean?=null,
    ) = repository.getLivestreamFilter(
        typeTitle = typeTitle, startTime = startTime,
        endTime = endTime,categoryId= categoryId, title = title, page = page, limit = limit, userId = null, isMySelf = isMySelf
    )

    suspend fun getLivestreamByType(
        typeTitle: String, userId: Int,
        limit: Int? = null, page: Int? = null,
    ) = repository.getAllLiveStreams(
        typeTitle = typeTitle,
        userId = userId,
        limit = limit,
        page = page
    )

    fun getTrendingHashtags() = repository.getTrendingHashtags()
}