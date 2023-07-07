package olmo.wellness.android.ui.screen.notification

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.core.Constants
import olmo.wellness.android.core.Result
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.domain.model.livestream.QueryLiveStreamWrap
import olmo.wellness.android.domain.model.livestream.toLiveShortItem
import olmo.wellness.android.domain.model.notification.NotificationInfo
import olmo.wellness.android.domain.repository.ApiChatRepository
import olmo.wellness.android.domain.use_case.LivestreamUseCase
import olmo.wellness.android.domain.use_case.NotificationUseCase
import olmo.wellness.android.domain.use_case.paging.NotificationSource
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.livestream.stream.data.LivestreamStatus
import olmo.wellness.android.ui.livestream.stream.data.TypeTitleLivestream
import olmo.wellness.android.ui.screen.playback_video.onlive.PlayBackOnLiveEvent
import javax.inject.Inject
import kotlin.math.abs

const val limitSize = 20
@HiltViewModel
class NotificationViewModel @Inject constructor(
    application: Application,
    private val notificationUseCase: NotificationUseCase,
    private val livestreamUseCase: LivestreamUseCase,
) : BaseViewModel(application) {

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isErr = MutableStateFlow<String>("")
    val isErr: StateFlow<String> = _isErr

    private val _recently: MutableStateFlow<MutableList<NotificationInfo?>> = MutableStateFlow(arrayListOf())
    val recently: StateFlow<MutableList<NotificationInfo?>> = _recently

    private val _older: MutableStateFlow<MutableList<NotificationInfo?>> = MutableStateFlow(arrayListOf())
    val older: StateFlow<MutableList<NotificationInfo?>> = _older

    private val _currentPage: MutableStateFlow<Int> = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage

    private val _isShowDialogRemoved = MutableStateFlow<Boolean>(false)
    val isShowDialogRemoved: StateFlow<Boolean> = _isShowDialogRemoved
    fun updateStateDialogRemoved(){
        _isShowDialogRemoved.value = false
    }

    private val _allowNavigationLiveNow = MutableStateFlow<Boolean>(false)
    val allowNavigationLiveNow: StateFlow<Boolean> = _allowNavigationLiveNow
    fun resetStateAllowNavigation(){
        _allowNavigationLiveNow.value = false
        _liveStreamSelected.value = null
        _allowNavigationPlayBack.value = false
    }

    private val _allowNavigationPlayBack = MutableStateFlow<Boolean>(false)
    val allowNavigationPlayBack: StateFlow<Boolean> = _allowNavigationPlayBack

    private val _liveStreamSelected = MutableStateFlow<LiveSteamShortInfo?>(null)
    val liveStreamSelected: StateFlow<LiveSteamShortInfo?> = _liveStreamSelected

    init {
        getNotification()
    }

    private fun getNotification(){
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val userId = sharedPrefs.getUserInfoLocal().userId
            notificationUseCase.invoke(page = currentPage.value, limit = limitSize, userId = userId).collectLatest { result ->
                when(result){
                    is Result.Success -> {
                        if(result.data.isNullOrEmpty()) {
                            _isLoading.value = false
                            return@collectLatest
                        }
                        result.data.map { notification ->
                            val startTime = notification.createdAt
                            if(startTime != null){
                                val currentTime = System.currentTimeMillis()
                                notification.isRecently = abs(currentTime - startTime) / (1000*60) <= 15
                            }
                        }
                        val listData = result.data.partition{ it.isRecently == true}
                        _recently.update {
                            listData.first.toMutableList()
                        }
                        _older.update {
                            listData.second.toMutableList()
                        }
                        _isLoading.value = false
                    }
                    is Result.Error -> {
                        _isLoading.value = false
                    }
                }
            }
        }
    }

    fun loadMoreNotification(){
        viewModelScope.launch(Dispatchers.IO) {
            _currentPage.update {
                it+1
            }
            val userId = sharedPrefs.getUserInfoLocal().userId
            _isLoading.value = true
            notificationUseCase.invoke(page = currentPage.value, limit = limitSize, userId = userId).collectLatest { result ->
                when(result){
                    is Result.Success -> {
                        if(result.data.isNullOrEmpty()) {
                            _isLoading.value = false
                            _currentPage.update {
                                it-1
                            }
                            return@collectLatest
                        }
                        result.data.map { notification ->
                            val startTime = notification.createdAt
                            if(startTime != null){
                                val currentTime = System.currentTimeMillis()
                                notification.isRecently = abs(currentTime - startTime) / (1000*60) <= 15
                            }
                        }
                        val listData = result.data.partition{ it.isRecently == true}
                        val listRecently = recently.value.apply {
                            addAll(listData.first)
                        }
                        val listOlder = older.value.apply {
                            addAll(listData.second)
                        }
                        _recently.update {
                            listRecently
                        }
                        _older.update {
                            listOlder
                        }
                        _isLoading.value = false
                    }
                    is Result.Error -> {
                        _isLoading.value = false
                        _currentPage.update {
                            it-1
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun deleteNotification(isRecently: Boolean?=null, isOlder: Boolean?=null,notiId: String){
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            notificationUseCase.deleteNotification(notiId).collectLatest {
                if(isRecently == true){
                    _recently.update {
                        recently.value.filterNot { it?.id == notiId }.toMutableList()
                    }
                    _isLoading.value = false
                }
                if(isOlder == true){
                    _older.update {
                        older.value.filterNot { it?.id == notiId }.toMutableList()
                    }
                    _isLoading.value = false
                }
            }
        }
    }

    fun seenNotification(notiId: String){
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            notificationUseCase.seenNotification(notiId).collectLatest {
                _isLoading.value = false
            }
        }
    }

    fun fetchPlayBacks(liveInfo: LiveSteamShortInfo?) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _liveStreamSelected.update {
                liveInfo
            }
            try {
                val query = QueryLiveStreamWrap(TypeTitleLivestream.LiveNow)
                livestreamUseCase.getAllLiveStream(query).collectLatest { res ->
                    res.onResultReceived(
                        onSuccess = {
                            if (res.data?.isNotEmpty() == true) {
                                val videoById = res.data.find { it.id == liveInfo?.id }
                                _isShowDialogRemoved.value = videoById == null
                                _allowNavigationLiveNow.value = videoById != null
                            }else{
                                _isShowDialogRemoved.value = true
                                _allowNavigationLiveNow.value = false
                            }
                            _isLoading.value = false
                        },
                        onError = {
                            _isLoading.value = false
                        }
                    )
                }
            } catch (throwable: Exception) {
                _isLoading.value = false
            }
        }
    }

    fun handleNavigation(notificationId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val listId = listOf(notificationId)
                val query =  "{\"id\":${listId}}"
                livestreamUseCase.getLivestreams(query,null).collectLatest { result ->
                    when (result) {
                        is Result.Success -> {
                            _isLoading.value = false
                            _liveStreamSelected.value = result.data?.first()?.toLiveShortItem()
                            _allowNavigationPlayBack.update {
                                true
                            }
                        }
                        else -> {
                            _isLoading.value = false
                            _allowNavigationPlayBack.update {
                                false
                            }
                        }
                    }
                }
            } catch (throwable: java.lang.Exception) {
                print(throwable.stackTrace)
                _isLoading.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}