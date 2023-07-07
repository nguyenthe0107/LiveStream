package olmo.wellness.android.domain.use_case
import android.util.Log
import kotlinx.coroutines.flow.Flow
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.PostRequest
import olmo.wellness.android.data.model.report_livestream.ReportLiveStreamRequestDTO
import olmo.wellness.android.domain.model.report_livestream.ReportLiveStreamInfo
import olmo.wellness.android.domain.repository.ApiRepository
import javax.inject.Inject

class ReportLiveStreamUseCase @Inject constructor(
    private val repository: ApiRepository){

    fun postUserReport(bodyRequest: PostRequest<List<ReportLiveStreamRequestDTO>>) : Flow<Result<Boolean>>{
        return repository.postUserReport(bodyRequest = bodyRequest)
    }

    fun getUserReport(query: String): Flow<Result<List<ReportLiveStreamInfo>>>{
        return repository.getUserReport(query)
    }
}