package olmo.wellness.android.domain.use_case.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import olmo.wellness.android.domain.model.notification.NotificationInfo
import olmo.wellness.android.domain.repository.ApiChatRepository
import olmo.wellness.android.sharedPrefs
import kotlin.math.abs

class NotificationSource(
    private val movieRepository: ApiChatRepository
): PagingSource<Int, NotificationInfo>() {

    override fun getRefreshKey(state: PagingState<Int, NotificationInfo>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotificationInfo> {
        return try {
            val userId = sharedPrefs.getUserInfoLocal().userId ?: 0
            val nextPage = params.key ?: 1
            val movieListResponse = movieRepository.getNotification(page = nextPage, limit = 20, userId = userId)
            if(movieListResponse.isNotEmpty()){
                movieListResponse.map { notification ->
                    val startTime = notification.dataNotification?.payload?.livestream?.startTime
                    if(startTime != null){
                        val currentTime = System.currentTimeMillis()/1000
                        notification.isRecently = abs(currentTime - startTime) / (1000*60) <= 15
                    }
                }
            }
            LoadResult.Page(
                data = movieListResponse,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if(movieListResponse.isNotEmpty()) nextPage.plus(1) else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}