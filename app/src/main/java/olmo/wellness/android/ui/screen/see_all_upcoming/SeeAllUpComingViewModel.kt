package olmo.wellness.android.ui.screen.see_all_upcoming

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.live_stream.SectionType
import olmo.wellness.android.domain.model.tracking.TrackingModel
import olmo.wellness.android.domain.use_case.HomeLiveUseCase
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.analytics.AnalyticsManager
import olmo.wellness.android.ui.base.BaseViewModel
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarEvent
import olmo.wellness.android.ui.screen.for_you.viewmodel.ForYouEvent
import olmo.wellness.android.ui.toDate
import olmo.wellness.android.ui.toLocalDate
import java.util.*
import javax.inject.Inject


@HiltViewModel
class SeeAllUpComingViewModel @Inject constructor(
    private val homeLiveUseCase: HomeLiveUseCase
) : BaseViewModel<SeeAllState, SeeAllEvent>() {

    override fun initState(): SeeAllState {
        return SeeAllState()
    }

    init {
        getListUpComing()
    }

    private fun getListUpComing() {
        val dateCurrent = uiState.value.dateSelect
        val startTime =
            DateTimeHelper.convertToDateHourMinute(minute = 0, hour = 0, date = dateCurrent)
        val endTime =
            DateTimeHelper.convertToDateHourMinute(minute = 59, hour = 23, date = dateCurrent)
        val typeTitle = "\"${SectionType.UPCOMING.value}\""
        viewModelScope.launch(Dispatchers.Default) {
            triggerStateEvent(SeeAllEvent.ShowLoading(true))
            homeLiveUseCase.getLivestreamFilter(
                typeTitle = typeTitle,
                startTime = startTime,
                endTime = endTime,
                categoryId = null
            ).collectLatest { result ->
                when (result) {
                    is Result.Loading -> {
                        triggerStateEvent(SeeAllEvent.ShowLoading(true))
                    }
                    is Result.Success -> {
                        if (result.data != null) {
                            triggerStateEvent(SeeAllEvent.ListUpComing(result.data))
                        }
                        sendTrackingSeeAllSection(SectionType.UPCOMING.value)
                    }
                    is Result.Error -> {
                        triggerStateEvent(SeeAllEvent.ShowLoading(false))
                    }
                }
            }
        }
    }


    @SuppressLint("NewApi")
    private fun createEventCalendar() {
        val temp = mutableListOf<KalendarEvent>()
        uiState.value.listUpComing?.forEach { info ->
            val dateCreate = DateTimeHelper.convertLongToDate(info.startTime)
            val index = temp.indexOfFirst {
                DateTimeHelper.checkSameDate(
                    dateCreate,
                    it.date.toDate()
                )
            }
            if (index >= 0) {
                temp[index].number++
            } else {
                val event = KalendarEvent(
                    date = dateCreate.toLocalDate(),
                    eventName = info.title, number = 1, eventDescription = info.description
                )
                temp.add(event)
            }
        }
        triggerStateEvent(SeeAllEvent.UpdateEventCalendar(temp))
    }

    fun setDateSelect(date: Date) {
        triggerStateEvent(SeeAllEvent.DateSelect(dateSelect = date))
    }


    override fun onTriggeredEvent(event: SeeAllEvent) {
        when (event) {
            is SeeAllEvent.ShowLoading -> {
                setState(uiState.value.copy(isLoading = event.isLoading))
            }
            is SeeAllEvent.ListUpComing -> {
                setState(
                    uiState.value.copy(
                        isLoading = false,
                        listUpComing = event.listUpComing
                    )
                )
//                createEventCalendar()
            }
            is SeeAllEvent.DateSelect -> {
                setState(
                    uiState.value.copy(
                        isLoading = false,
                        dateSelect = event.dateSelect,
                        listUpComing = emptyList()
                    )
                )
                getListUpComing()
            }
            is SeeAllEvent.UpdateEventCalendar -> {
                setState(
                    uiState.value.copy(
                        isLoading = false,
                        listEventCalendar = event.data
                    )
                )
            }
        }
    }

    private fun sendTrackingSeeAllSection(titleSection: String?) {
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