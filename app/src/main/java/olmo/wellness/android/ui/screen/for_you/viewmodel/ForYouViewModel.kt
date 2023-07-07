package olmo.wellness.android.ui.screen.for_you.viewmodel

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.live_stream.SectionType
import olmo.wellness.android.domain.model.livestream.LiveCategory
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.domain.model.tracking.TrackingModel
import olmo.wellness.android.domain.use_case.HomeLiveUseCase
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.analytics.AnalyticsManager
import olmo.wellness.android.ui.base.BaseViewModel
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarEvent
import olmo.wellness.android.ui.screen.for_you.data.TabData
import olmo.wellness.android.ui.toDate
import olmo.wellness.android.ui.toLocalDate
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ForYouViewModel @Inject constructor(
    private val homeLiveUseCase: HomeLiveUseCase,
) : BaseViewModel<ForYouState, ForYouEvent>() {


    override fun initState(): ForYouState {
        return ForYouState()
    }

    init {
        getCategories()
        hardTab()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onTriggeredEvent(event: ForYouEvent) {
        when (event) {
            is ForYouEvent.ShowLoading -> {
                setState(uiState.value.copy(isLoading = event.isLoading))
            }
            is ForYouEvent.ListCategoriesLoaded -> {
                setState(
                    uiState.value.copy(
                        isLoading = false,
                        listCategories = event.listCategories
                    )
                )
            }
            is ForYouEvent.UpdateListTab -> {
                setState(
                    uiState.value.copy(
                        isLoading = false,
                        listTab = event.listTab
                    )
                )
            }
            is ForYouEvent.UpdateListItemTypes -> {
                setState(
                    uiState.value.copy(
                        timeEvent = System.currentTimeMillis(),
                        isLoading = false,
                        listItemTypes = uiState.value.listItemTypes?.apply {
                            event.listItemTypes.forEach { k, v ->
                                if (this[k] == null) {
                                    set(k, v?.toMutableList())
                                } else {
                                    set(k, get(k)?.apply {
                                        v?.toMutableList()?.let { addAll(it) }
                                    }?.map { it } as MutableList<LiveSteamShortInfo>)
                                }
                            }
                        }
                    )
                )
            }
            is ForYouEvent.SubListCategories -> {
                setState(
                    uiState.value.copy(
                        isLoading = false,
                        listSubCategories = event.subListCategories,
                        title = event.title
                    )
                )
            }
            is ForYouEvent.UpdateDateSelect -> {
                setState(
                    uiState.value.copy(
                        isLoading = false,
                        dateSelect = event.dateSelect
                    )
                )
            }
            is ForYouEvent.UpdateListUpcomingItem -> {
                setState(
                    uiState.value.copy(
                        timeEvent = System.currentTimeMillis(),
                        isLoading = false,
                        listFilterItem =
                        (if (event.listFilter?.isNotEmpty() == true)
                            uiState.value.listFilterItem?.apply {
                                addAll(event.listFilter)
                            } else mutableListOf())
                    )
                )
            }
            is ForYouEvent.UpdateEventCalendar -> {
                setState(
                    uiState.value.copy(
                        isLoading = false,
                        listEventCalendar = event.data
                    )
                )
            }
        }
    }


    fun setDateSelect(type: String?, page: Int, date: Date,categoryId: Int?) {
        triggerStateEvent(ForYouEvent.UpdateListUpcomingItem(mutableListOf()))
        triggerStateEvent(ForYouEvent.UpdateDateSelect(dateSelect = date))
        getData(type, page, date,categoryId)
    }

    private fun filterUpComingScheduler() {
        val temp = uiState.value.listItemTypes?.get(SectionType.UPCOMING.value)?.filter {
            it.startTime != null && DateTimeHelper.checkSameDate(
                uiState.value.dateSelect?.time,
                it.startTime
            )
        }?.toMutableList()?.apply {
            sortBy { it.startTime }
        }
        triggerStateEvent(ForYouEvent.UpdateListUpcomingItem(temp))
    }


    @SuppressLint("NewApi")
    private fun createEventCalendar() {
        val temp = mutableListOf<KalendarEvent>()
        uiState.value.listItemTypes?.get(SectionType.UPCOMING.value)?.forEach { info ->
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
        triggerStateEvent(ForYouEvent.UpdateEventCalendar(temp))
    }

    private fun hardTab() {
        val tempTab = mutableListOf<TabData?>().apply {
            add(TabData(title = SectionType.LIVE_NOW.value, name = SectionType.LIVE_NOW.value))
            add(TabData(title = SectionType.UPCOMING.value, name = SectionType.UPCOMING.value))
            add(TabData(title = SectionType.TOP_TRENDING.value,
                name = SectionType.TOP_TRENDING.value))
            add(TabData(title = SectionType.EVENT.value, name = SectionType.EVENT.value))
            add(TabData(title = SectionType.RECOMMENDED.value,
                name = SectionType.RECOMMENDED.value))
        }
        triggerStateEvent(ForYouEvent.UpdateListTab(tempTab))
    }


    @OptIn(FlowPreview::class)
    fun getData(title: String?, page: Int, date: Date? = null, categoryId : Int?) {
        val startTime = (if (date != null) DateTimeHelper.convertToDateHourMinute(minute = 0,
            hour = 0,
            date = date)
        else null)
        val endTime = (if (date != null) DateTimeHelper.convertToDateHourMinute(minute = 59,
            hour = 23,
            date = date)
        else null)
        viewModelScope.launch(Dispatchers.IO) {
            val typeTitle = "\"${title}\""
            homeLiveUseCase.getLivestreamFilter(typeTitle = typeTitle,
                categoryId= categoryId,
                startTime = startTime, endTime = endTime, limit = 20, page = page, isMySelf = true)
                .collectLatest { result ->

                    when (result) {
                        is Result.Loading -> {
                            triggerStateEvent(ForYouEvent.ShowLoading(true))
                        }
                        is Result.Success -> {
                            if (result.data != null) {
                                if (title != SectionType.UPCOMING.value) {
                                    val temp = result.data.associate {
                                        return@associate (title as String?) to result.data
                                    }.toMap()
                                    triggerStateEvent(ForYouEvent.UpdateListItemTypes(temp))
                                } else {
                                    triggerStateEvent(ForYouEvent.UpdateListUpcomingItem(result.data))
                                }
                            }
                        }
                        is Result.Error -> {
                            triggerStateEvent(ForYouEvent.ShowLoading(false))
                        }

                    }
                }
        }
    }

        private fun getCategories() {
            triggerStateEvent(ForYouEvent.ShowLoading(true))
            viewModelScope.launch(Dispatchers.Default) {
                homeLiveUseCase.getListCategories().collectLatest { result ->
                    when (result) {
                        is Result.Loading -> {
                            triggerStateEvent(ForYouEvent.ShowLoading(true))
                        }
                        is Result.Success -> {
                            if (result.data != null) {
                                triggerStateEvent(ForYouEvent.ListCategoriesLoaded(result.data))
                            }
                        }
                        is Result.Error -> {
                            triggerStateEvent(ForYouEvent.ShowLoading(false))
                        }
                    }

                }
            }
        }

        fun getSubListCategories(data: LiveCategory?) {
            triggerStateEvent(
                ForYouEvent.SubListCategories(
                    subListCategories = data?.categories,
                    title = data?.nameLocale?.en
                )
            )
        }

        fun sendTrackingContentCategory(content: String?) {
            viewModelScope.launch(Dispatchers.IO) {
                val userLocal = sharedPrefs.getUserInfoLocal()
                val trackingModel = TrackingModel(
                    user_id = userLocal.userId,
                    group_content = content
                )
                AnalyticsManager.getInstance()?.trackingContentCategory3(trackingModel)
            }
        }

        fun trackingViewLiveStream(liveInput: LiveSteamShortInfo?) {
            viewModelScope.launch(Dispatchers.IO) {
                val userInfo = sharedPrefs.getUserInfoLocal()
                val trackingModel = TrackingModel(
                    user_id = userInfo.userId,
                    livestream_id = liveInput?.id,
                    livestream_tile = liveInput?.title,
                    seller_name = liveInput?.user?.name
                )
                AnalyticsManager.getInstance()?.trackingClickView(trackingModel)
            }
        }
    }