package olmo.wellness.android.ui.screen.all_section_home
import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Constants
import olmo.wellness.android.domain.model.tracking.TrackingModel
import olmo.wellness.android.domain.use_case.HomeLiveUseCase
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.analytics.AnalyticsManager
import olmo.wellness.android.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SeeAllFilterLiveStreamViewModel @Inject constructor(
    private val homeLiveUseCase: HomeLiveUseCase
) : BaseViewModel<SeeAllSectionState, SeeAllSectionEvent>() {

    override fun initState(): SeeAllSectionState {
        return SeeAllSectionState()
    }

    fun getListDataSection(type: String) {
        val typeTitle = "\"${type}\""
        viewModelScope.launch(Dispatchers.Default) {
            triggerStateEvent(SeeAllSectionEvent.ShowLoading(true))
            val userId = sharedPrefs.getUserInfoLocal().userId ?: 0
            homeLiveUseCase.getLivestreamByType(
                typeTitle = typeTitle,
                userId = userId,
                limit = Constants.PAGE_SIZE,
                page = 1
            ).collectLatest { result ->
                if (result.data != null) {
                    triggerStateEvent(SeeAllSectionEvent.ListUpComing(result.data))
                }
                triggerStateEvent(SeeAllSectionEvent.ShowLoading(false))
                sendTrackingSeeAllSection(type)
            }
        }
    }

    override fun onTriggeredEvent(event: SeeAllSectionEvent) {
        when (event) {
            is SeeAllSectionEvent.ShowLoading -> {
                setState(uiState.value.copy(isLoading = event.isLoading))
            }
            is SeeAllSectionEvent.ListUpComing -> {
                setState(uiState.value.copy(listUpComing = event.listUpComing))
            }
        }
    }

    private fun sendTrackingSeeAllSection(titleSection: String?){
        viewModelScope.launch(Dispatchers.IO) {
            val userLocal = sharedPrefs.getUserInfoLocal()
            val trackingModel = TrackingModel(
                user_id = userLocal.userId,
                group_content = titleSection
            )
            AnalyticsManager.getInstance()?.trackingLoadGroupContent(trackingModel)
        }
    }
}