package olmo.wellness.android.ui.screen.search_home

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.data.model.live_stream.SectionType
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.domain.model.livestream.toLiveShortItem
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.domain.use_case.HomeLiveUseCase
import olmo.wellness.android.domain.use_case.LivestreamUseCase
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    application: Application,
    private val apiUseCase: HomeLiveUseCase,
    private val livestreamUseCase: LivestreamUseCase,
) : BaseViewModel(application) {

    private val _userType = MutableStateFlow<UserTypeModel?>(UserTypeModel.BUYER)
    val userType: StateFlow<UserTypeModel?> = _userType

    private val _profileModel = MutableStateFlow(ProfileInfo())
    val profileModel : StateFlow<ProfileInfo> = _profileModel

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _searchData =  MutableStateFlow<List<LiveSteamShortInfo>?>(null)
    val searchData : StateFlow<List<LiveSteamShortInfo>?> = _searchData

    private val _textChange = MutableStateFlow("")
    private val textChange : StateFlow<String> = _textChange

    private val _liveStreamSelected = MutableStateFlow<LiveSteamShortInfo?>(null)
    val liveStreamSelected : StateFlow<LiveSteamShortInfo?> = _liveStreamSelected

    private val _allowNavigationPlayBack = MutableStateFlow<Boolean>(false)
    val allowNavigationPlayBack: StateFlow<Boolean> = _allowNavigationPlayBack

    @OptIn(FlowPreview::class)
    fun getAllSearch(title: String){
        viewModelScope.launch(Dispatchers.IO) {
            val typeTitle = "\"${SectionType.ALL.value}\""
            _isLoading.value = true
            _textChange.update {
                title
            }
            textChange.debounce(500).collectLatest { contentInput ->
                val titleFinal = "\"${contentInput}\""
                apiUseCase.getLivestreamFilter(typeTitle = typeTitle, title = titleFinal,
                    startTime = null, endTime = null, limit = 20, page = 1, categoryId = null).collectLatest { result ->
                    if (result.data != null) {
                        _searchData.update {
                            result.data
                        }
                    }
                    _isLoading.value = false
                }
            }
        }
    }

    fun handleNavigation(videoId: Int?){
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val listId = listOf(videoId)
                val query =  "{\"id\":${listId}}"
                livestreamUseCase.getLivestreams(query,null).collectLatest { result ->
                    when (result) {
                        is Result.Success -> {
                            _isLoading.value = false
                            if(result.data?.first() != null){
                                val final = result.data.distinctBy { it.id }.first()
                                _liveStreamSelected.update {
                                    final.toLiveShortItem()
                                }
                                _allowNavigationPlayBack.update {
                                    true
                                }
                            }
                        }
                        else -> {
                            _isLoading.value = false
                            _allowNavigationPlayBack.update {
                                false
                            }
                            _allowNavigationPlayBack.value= false
                        }
                    }
                }
            } catch (throwable: java.lang.Exception) {
                print(throwable.stackTrace)
                _isLoading.value = false
            }
        }
    }

    fun setNavigationFalse(){
        _allowNavigationPlayBack.value=false
    }

    fun clearData(){
        viewModelScope.launch(Dispatchers.IO) {
            _searchData.update {
                emptyList()
            }
        }
    }

    fun resetData(){
        _liveStreamSelected.value = null
        _allowNavigationPlayBack.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
