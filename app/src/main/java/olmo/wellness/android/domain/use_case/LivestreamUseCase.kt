package olmo.wellness.android.domain.use_case

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.live_stream.LiveStreamRequest
import olmo.wellness.android.data.model.profile.BodyProfileRequest
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.ui.livestream.stream.data.LivestreamKeyResponse
import olmo.wellness.android.domain.model.livestream.LivestreamInfo
import olmo.wellness.android.domain.model.livestream.QueryLiveStreamModel
import olmo.wellness.android.domain.model.livestream.QueryLiveStreamWrap
import olmo.wellness.android.domain.model.user_follow.UserFollowInfo
import olmo.wellness.android.domain.model.user_follow.UserFollowRequest
import olmo.wellness.android.domain.repository.ApiRepository
import olmo.wellness.android.ui.livestream.stream.data.UpdateLivestreamWrapRequest
import javax.inject.Inject

class LivestreamUseCase @Inject constructor(
    private val repository: ApiRepository){

    suspend fun requestLivestream(request : LiveStreamRequest): Flow<Result<LivestreamKeyResponse>> {
        return repository.getLivestreamKey(request)
    }

    suspend fun getLivestreams(query: String, projection: String?): Flow<Result<List<LivestreamInfo>>> {
        return repository.listLivestream(query, projection)
    }
    suspend fun deleteLivestream(query: String,returning : Boolean?) : Flow<Result<Boolean>>{
        return repository.deleteLivestream(query,returning)
    }

    suspend fun updateStatusLivestream(query: String, updateLivestreamRequest: UpdateLivestreamWrapRequest): Flow<Result<Boolean>> {
        return repository.updateStatusLivestream(query, updateLivestreamRequest)
    }

    suspend fun getAllLiveStream(queryLiveStreamWrap: QueryLiveStreamWrap) : Flow<Result<List<LiveSteamShortInfo>>>{
        val format = "\"${queryLiveStreamWrap.typeTitle.name}\""
        return repository.getAllLiveStreams(format, queryLiveStreamWrap.userId,
            queryLiveStreamWrap.limit, queryLiveStreamWrap.page)
    }

    suspend fun getSweepListStreams(query: String) : Flow<Result<List<LiveSteamShortInfo>>>{
        return repository.getSweepListStreams(query)
    }

    suspend fun getUserFollow(query: String?=null, projection: String?): Flow<Result<List<UserFollowInfo>>>{
        return repository.getUserFollow(query, projection)
    }

    suspend fun postUserFollow(query: UserFollowRequest): Flow<Result<Boolean>>{
        return repository.postUserFollow(query)
    }

    suspend fun deleteUserFollow(query: String): Flow<Result<List<UserFollowInfo>>>{
        return repository.deleteUserFollow(query)
    }

    suspend fun getAllSellerFinishedStreams(): Flow<Result<List<LiveSteamShortInfo>>>{
        return repository.getAllSellerFinishedStreams()
    }

    suspend fun getListProfileLiveStream(userId: Int?, limit: Int?, page: Int?): Flow<Result<List<LiveSteamShortInfo>>>{
        return repository.getListProfileLiveStream(userId = userId, limit = limit, page = page)
    }

    suspend fun getUserFollowing(userId: Int?): Flow<Result<List<UserFollowInfo>>>{
        return repository.getUserFollowing(userId = userId)
    }

    suspend fun getUserFollower(userId: Int?): Flow<Result<List<UserFollowInfo>>>{
        return repository.getUserFollower(userId = userId)
    }

    suspend fun getPinStreamList(userId : Int) : Flow<Result<List<LivestreamInfo>>>{
        val listId = listOf(userId)
        val liveStreamRequest = QueryLiveStreamModel(userId = listId, isPin = true)
        val requestJson = Gson().toJson(liveStreamRequest)
        return repository.getLivestreamPin(requestJson,)
    }

    suspend fun updateVideoStream(requestJson: String, bodyRequest: UpdateLivestreamWrapRequest) : Flow<Result<Boolean>>{
        return repository.updateStatusLivestream(requestJson, bodyRequest)
    }

    fun postUserFollowLiveStream(bodyRequest: UserFollowInfo) : Flow<Result<List<UserFollowInfo>>>{
        return repository.postUserFollowLiveStream(bodyRequest)
    }

    fun deleteUserFollowLiveStream(query: String) : Flow<Result<List<UserFollowInfo>>>{
        return repository.deleteUserFollowLiveStream(query)
    }

    fun postShareOnProfile(liveId: BodyProfileRequest) : Flow<Result<Boolean>>{
        return repository.postShareOnProfile(liveId)
    }

    fun getShareOnProfile(page: Int, limit : Int): Flow<Result<List<LiveSteamShortInfo>>>{
        return repository.getShareOnProfile(page, limit)
    }

}