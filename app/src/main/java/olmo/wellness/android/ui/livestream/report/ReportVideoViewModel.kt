package olmo.wellness.android.ui.livestream.report

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.core.Result
import olmo.wellness.android.core.toJson
import olmo.wellness.android.data.model.PostRequest
import olmo.wellness.android.data.model.report_livestream.ReportLiveStreamRequestDTO
import olmo.wellness.android.domain.model.report_livestream.ReportLiveStreamRequest
import olmo.wellness.android.domain.use_case.ReportLiveStreamUseCase
import olmo.wellness.android.sharedPrefs
import javax.inject.Inject

@HiltViewModel
class ReportVideoViewModel @Inject constructor(
    application: Application,
    private val reportLiveStreamUseCase: ReportLiveStreamUseCase
) : BaseViewModel(application = application){

    private val _isLoading : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = _isLoading

    private val _liveStreamId : MutableStateFlow<Int> = MutableStateFlow(0)
    val liveStreamId : StateFlow<Int> = _liveStreamId

    private val categories = listOf(
        ReportTypeWrapper(ReportType.Minor_Report),
        ReportTypeWrapper(ReportType.Dangerous_Report),
        ReportTypeWrapper(ReportType.Suicde_Report),
        ReportTypeWrapper(ReportType.Adult_nudity_Report),
        ReportTypeWrapper(ReportType.Bullying_Report),
        ReportTypeWrapper(ReportType.Hateful_behavior_Report),
        ReportTypeWrapper(ReportType.Violent_behavior_Report),
        ReportTypeWrapper(ReportType.Spam_Report),
        ReportTypeWrapper(ReportType.Harmful_Report),
        ReportTypeWrapper(ReportType.Illegal_Report),
        ReportTypeWrapper(ReportType.Violent_Graphic_Report),
        ReportTypeWrapper(ReportType.Intellectual_property_Report),
        ReportTypeWrapper(ReportType.Other_Report)
    )

    private val _reportTypeList : MutableStateFlow<List<ReportTypeWrapper>> = MutableStateFlow(categories)
    val reportTypeList : StateFlow<List<ReportTypeWrapper>> = _reportTypeList

    fun bindLiveStreamId(liveStreamIdInput: Int?){
        if (liveStreamIdInput != null) {
            _liveStreamId.value = liveStreamIdInput
        }
    }

    fun onSubmit(listSection: MutableList<String>){
        viewModelScope.launch(Dispatchers.Default) {
            _isLoading.value = true
            val finalString = listSection.joinToString(separator = ",")
            val report = listOf(ReportLiveStreamRequestDTO(livestreamId = liveStreamId.value, content = finalString))
            val bodyRequest = PostRequest(report)
            reportLiveStreamUseCase.postUserReport(bodyRequest).collectLatest {
                _isLoading.value = false
            }
        }
    }

    fun getReportList(){
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val userId = sharedPrefs.getUserInfoLocal().userId ?: 0
            val listUserId = listOf(userId)
            val livestreamId = listOf(liveStreamId.value)
            val request = (ReportLiveStreamRequest(userId = listUserId, livestreamId = livestreamId)).toJson()
            reportLiveStreamUseCase.getUserReport(request).collectLatest {
                when(it){
                    is Result.Success -> {
                        _reportTypeList.value.map { report ->
                            it.data?.map { dataApi ->
                                if(dataApi.content?.isNotEmpty() == true){
                                    val arrayFinal = dataApi.content.split(",")
                                    arrayFinal.map { arr ->
                                        if(arr.contains(report.reportType.value, ignoreCase = true)){
                                            report.isSelected = true
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else -> {
                    }
                }
                _isLoading.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}
