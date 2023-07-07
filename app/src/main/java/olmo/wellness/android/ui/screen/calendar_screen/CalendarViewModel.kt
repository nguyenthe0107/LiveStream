package olmo.wellness.android.ui.screen.calendar_screen

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.core.Constants
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.data.model.schedule.FillDataSchedule
import olmo.wellness.android.domain.model.livestream.LivestreamInfo
import olmo.wellness.android.domain.use_case.LivestreamUseCase
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.data.KalendarEvent
import olmo.wellness.android.ui.toDate
import olmo.wellness.android.ui.toLocalDate
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    application: Application, private val livestreamUseCase: LivestreamUseCase
) : BaseViewModel(application) {

    private var _listSchedulerCalendar: MutableStateFlow<MutableList<LivestreamInfo>> = MutableStateFlow(mutableListOf())
    val listSchedulerCalendar: StateFlow<MutableList<LivestreamInfo>> = _listSchedulerCalendar
    private var _dateSelect: MutableStateFlow<Date> = MutableStateFlow(Date())
    var dateSelect: StateFlow<Date> = _dateSelect

    private var _listSchedulerFilter: MutableStateFlow<MutableList<LivestreamInfo>> =
        MutableStateFlow(mutableListOf())
    val listSchedulerFilter: StateFlow<List<LivestreamInfo>> = _listSchedulerFilter

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    fun setLoading(status: Boolean) {
        _isLoading.update {
            status
        }
    }

    private var _listEventCalendar: MutableStateFlow<MutableList<KalendarEvent>> = MutableStateFlow(mutableListOf())
    val listEventCalendar: StateFlow<MutableList<KalendarEvent>> = _listEventCalendar

    private var _isNext: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isNext: StateFlow<Boolean> = _isNext

    fun setDateSelect(date: Date) {
        _dateSelect.value = date
        filterScheduler()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addLivestreamInfo(fillDataSchedule: FillDataSchedule?, _startTime: Long) {
        val info = LivestreamInfo().apply {
            title = fillDataSchedule?.title
            hashtag = fillDataSchedule?.description
            startTime = _startTime
        }
        _listSchedulerCalendar.value.add(info)
        _dateSelect.value = Date(_startTime)
        filterScheduler()
        _isNext.value = true
    }

    @SuppressLint("NewApi")
    private fun createEventCalendar() {
        _listEventCalendar.value= mutableListOf()
        val temp= mutableListOf<KalendarEvent>()
        _listSchedulerCalendar.value.forEach { info ->
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
                    eventName = info.title, number = 1, eventDescription = info.hashtag
                )
                temp.add(event)
            }
        }
        _listEventCalendar.value=temp
    }

    private fun filterScheduler() {
        val temp = _listSchedulerCalendar.value.filter {
            it.startTime != null && DateTimeHelper.checkSameDate(
                _dateSelect.value.time,
                it.startTime
            )
        }.toMutableList().apply {
            sortBy { it.startTime }
        }
        _listSchedulerFilter.value = temp
    }

    fun fetchListScheduler() {
        viewModelScope.launch {
            showLoading()
            val status = "[".plus("\"UPCOMING\"").plus("]")
            val query = "{\"status\":$status}"
            livestreamUseCase.getLivestreams(query, null).collect() { res ->
                when(res){
                    is Result.Loading ->{
                        showLoading()
                    }
                    is Result.Success->{
                        _listSchedulerCalendar.value = res.data?.toMutableList() ?: mutableListOf()
                        filterScheduler()
                        createEventCalendar()
                        hideLoading()
                    }
                    is Result.Error ->{
                        hideLoading()
                    }
                }

            }
        }
    }


  private fun removeItem(livestreamInfo: LivestreamInfo) {
      livestreamInfo.let {
          val originList: MutableList<LivestreamInfo> = listSchedulerCalendar.value.toMutableList()
          originList.remove(livestreamInfo)
          _listSchedulerCalendar.value = originList
      }
    }


    fun deleteLivestream(livestreamInfo: LivestreamInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            showLoading()
            val list = mutableListOf<Int>().apply {
                livestreamInfo.id?.let { add(it) }
            }
            val query = "{\"id\":$list}"
            livestreamUseCase.deleteLivestream(query, null).collect{ res ->
                res.onResultReceived(
                    onSuccess = {
                        removeItem(livestreamInfo)
                        hideLoading()
                    },
                    onError = {
                        hideLoading()
                    }
                )
            }
        }
    }

    private fun showLoading() {
        _isLoading.update {
            true
        }
    }

    private fun hideLoading() {
        _isLoading.update {
            false
        }
    }

    fun getListUser(): List<User> {
        val user = User(
            "https://images.everydayhealth.com/images/healthy-living/fitness/all-about-yoga-mega-722x406.jpg",
            "abc@gmail.com", 1, false, false, false
        )
        val user1 = User(
            "https://images.everydayhealth.com/images/healthy-living/fitness/all-about-yoga-mega-722x406.jpg",
            "abc@gmail.com", 2, false, false, false
        )
        val user2 = User(
            "https://images.everydayhealth.com/images/healthy-living/fitness/all-about-yoga-mega-722x406.jpg",
            "abc@gmail.com", 3, false, false, false
        )
        val user3 = User(
            "https://images.everydayhealth.com/images/healthy-living/fitness/all-about-yoga-mega-722x406.jpg",
            "abc@gmail.com", 4, false, false, false
        )
        return listOf(user, user1, user2, user3)
    }

}