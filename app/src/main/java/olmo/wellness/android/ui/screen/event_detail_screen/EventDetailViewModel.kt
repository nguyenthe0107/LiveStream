package olmo.wellness.android.ui.screen.event_detail_screen

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.core.Constants.ERROR_COMMON
import olmo.wellness.android.core.Result
import olmo.wellness.android.domain.model.livestream.LiveCategory
import olmo.wellness.android.domain.model.livestream.LivestreamInfo
import olmo.wellness.android.domain.model.user_follow.UserFollowInfo
import olmo.wellness.android.domain.model.user_follow.UserFollowRequestInfo
import olmo.wellness.android.domain.use_case.LivestreamUseCase
import olmo.wellness.android.sharedPrefs
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(application: Application,
                                               private val livestreamUseCase: LivestreamUseCase) : BaseViewModel(application) {
    private val _isLoading : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = _isLoading

    private var _listScheduler: MutableStateFlow<List<LivestreamInfo>> = MutableStateFlow(listOf())
    val listSchedulerCalendar: StateFlow<List<LivestreamInfo>> = _listScheduler

    private val _liveStreamId : MutableStateFlow<Int> = MutableStateFlow(-1)
    private val liveStreamId : StateFlow<Int> = _liveStreamId

    private val _errorContent : MutableStateFlow<String> = MutableStateFlow("")
    val errorContent : StateFlow<String> = _errorContent

    private val _categoryList = MutableStateFlow(listOf<LiveCategory>())
    val categoryList: StateFlow<List<LiveCategory>> = _categoryList

    fun fetchListOfStreams(id : Int, isAddSuccess: Boolean?=null,isDeleteSuccess: Boolean?=null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val temp = arrayListOf<Int>().apply {
                    add(id)
                }
                _liveStreamId.value = id
                val query =  "{\"id\":${temp}}"
                livestreamUseCase.getLivestreams(query,null).collectLatest { result ->
                    when (result) {
                        is Result.Success -> {
                            _listScheduler.update {
                                if(isAddSuccess == true){
                                    result.data?.map {
                                        if(it.id == id){
                                            it.isFollow = true
                                        }
                                    }
                                }
                                if(isDeleteSuccess == true){
                                    result.data?.map {
                                        if(it.id == id){
                                            it.isFollow = false
                                        }
                                    }
                                }
                                result.data ?: listOf()
                            }
                        }
                        is  Result.Error -> {
                            _errorContent.value = result.message ?: ERROR_COMMON
                        }
                    }
                }
                _isLoading.value = false
            } catch (throwable: Exception) {
                print(throwable.stackTrace)
                _isLoading.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun postUserFollow(){
        liveStreamId.value.let { liveStreamValue ->
            viewModelScope.launch {
                _isLoading.value = true
                val userId = sharedPrefs.getUserInfoLocal().userId
                val userFollow = UserFollowInfo(userId = userId, livestreamId = liveStreamValue)
                livestreamUseCase.postUserFollowLiveStream(userFollow).collectLatest {
                    fetchListOfStreams(liveStreamValue, isAddSuccess = true, isDeleteSuccess = false)
                    _isLoading.value = false
                }
            }
        }
    }

    fun deleteUserFollow(){
        liveStreamId.value.let { liveStreamValue ->
            viewModelScope.launch {
                _isLoading.value = true
                val userId = sharedPrefs.getUserInfoLocal().userId ?: 0
                val userIdFinal = listOf(userId)
                val liveStreamIdFinal = listOf(liveStreamValue)
                val userFollow = UserFollowRequestInfo(userId = userIdFinal, livestreamId = liveStreamIdFinal)
                val query = Gson().toJson(userFollow)
                livestreamUseCase.deleteUserFollowLiveStream(query).collectLatest {
                    _isLoading.value = false
                    fetchListOfStreams(liveStreamValue, isDeleteSuccess = true, isAddSuccess = false)
                }
            }
        }
    }

    fun onSubCategoryClicked(categoryId: Int, subCateGoryId: Int) {
        _categoryList.value = _categoryList.value.toMutableList().also { categories ->
            categories.first { it.id == categoryId }.also { category ->
                val subCategoryList = category.categories?.toMutableList()
                subCategoryList?.indexOf(subCategoryList.firstOrNull { sub -> sub.id == subCateGoryId })
                    ?.let { index ->
                        if ( subCategoryList.isNotEmpty() ) {
                            subCategoryList[index] =
                                subCategoryList[index].copy(isSelected = !subCategoryList[index].isSelected!!)
                        }
                    }

                categories[categories.indexOf(category)] =
                    categories[categories.indexOf(category)].copy(categories = subCategoryList)
            }
        }
    }
}

