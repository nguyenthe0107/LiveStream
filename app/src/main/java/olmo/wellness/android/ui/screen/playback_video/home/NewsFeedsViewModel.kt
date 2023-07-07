package olmo.wellness.android.ui.screen.playback_video.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Result
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.domain.model.livestream.HashTag
import olmo.wellness.android.domain.model.livestream.HomeLiveSectionData
import olmo.wellness.android.domain.model.livestream.LiveCategory
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.domain.model.tracking.TrackingModel
import olmo.wellness.android.domain.use_case.GetApiUseCase
import olmo.wellness.android.domain.use_case.HomeLiveUseCase
import olmo.wellness.android.domain.use_case.NotificationUseCase
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.analytics.AnalyticsManager
import olmo.wellness.android.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class NewsFeedsViewModel @Inject constructor(
    private val homeLiveUseCase: HomeLiveUseCase,
    private val getApiUseCase: GetApiUseCase,
    private val notificationUseCase: NotificationUseCase
): BaseViewModel<NewsFeedsState, NewsFeedsEvent>() {

    override fun initState(): NewsFeedsState = NewsFeedsState()

    private val _listSections: MutableStateFlow<List<HomeLiveSectionData>?> = MutableStateFlow(null)
    private val _listCategories: MutableStateFlow<List<LiveCategory>?> = MutableStateFlow(null)
    private val _listTrendingHashTags: MutableStateFlow<List<HashTag>?> = MutableStateFlow(null)

    override fun onTriggeredEvent(event: NewsFeedsEvent) {
        when(event){
            is NewsFeedsEvent.ListCategoriesLoaded -> {
                setState(
                    uiState.value.copy(
                        listCategories = event.listCategories
                    )
                )
            }
            is NewsFeedsEvent.ListBodySectionsLoaded -> {
                setState(
                    uiState.value.copy(
                        listBodySection = event.listBodySection.filterNot { it.data.isNullOrEmpty() }
                    )
                )
            }
            /*is NewsFeedsEvent.ListHashTagsLoaded -> {
                setState(
                    uiState.value.copy(
                        listHashTags = event.listHashTags
                    )
                )
            }*/
            is NewsFeedsEvent.ProfileInfoLoaded -> {
                setState(
                    uiState.value.copy(
                        profile = event.profile
                    )
                )
            }
            is NewsFeedsEvent.SubListCategories ->{
                setState(uiState.value.copy(
                    listSubCategories= event.subListCategories,
                    title = event.title
                ))
            }
            is NewsFeedsEvent.UpdateTotalUnseenNotification -> {
                setState(uiState.value.copy(
                    totalUnseenNotification = event.totalUnseenNotification
                ))
            }
        }
    }

    private fun getSections(){
        viewModelScope.launch(Dispatchers.Default) {
            triggerStateEvent(NewsFeedsEvent.ShowLoading(true))
            homeLiveUseCase.getSections(null,null).collectLatest{  result ->
                if(result.data != null){
                   _listSections.update {
                       result.data
                   }
                }
                AnalyticsManager.getInstance()?.trackingSignInSuccess()
                triggerStateEvent(NewsFeedsEvent.ShowLoading(false))
            }
        }
    }

    private fun getCategories(){
        viewModelScope.launch(Dispatchers.Default) {
            triggerStateEvent(NewsFeedsEvent.ShowLoading(true))
            homeLiveUseCase.getListCategories().collectLatest{ result ->
                if(result.data != null){
                    _listCategories.update {
                        result.data
                    }
                }
                triggerStateEvent(NewsFeedsEvent.ShowLoading(false))
            }
        }
    }

    fun getSubCategories(data : LiveCategory?){
        triggerStateEvent(NewsFeedsEvent.SubListCategories(data?.categories, data?.nameLocale?.en))
    }

    private fun getTrendingHashtag(){
        viewModelScope.launch(Dispatchers.Default) {
            triggerStateEvent(NewsFeedsEvent.ShowLoading(true))
            homeLiveUseCase.getTrendingHashtags().collect{ result ->
                if(result.data != null){
                   _listTrendingHashTags.update {
                       result.data
                   }
                }
                triggerStateEvent(NewsFeedsEvent.ShowLoading(false))
            }
        }
    }

    private fun getProfile(){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(NewsFeedsEvent.ShowLoading(true))
            val userType = sharedPrefs.getUserInfoLocal().userTypeModel
            if(userType == UserTypeModel.BUYER){
                val userName = sharedPrefs.getProfile()
                triggerStateEvent(event = NewsFeedsEvent.ProfileInfoLoaded(
                    ProfileInfo(
                        name = userName.name?:"Anonymous"
                    )
                ))
            }else{
                getApiUseCase.getUserInfo().collect{ result ->
                    when (result) {
                        is Result.Success -> {
                            triggerStateEvent(event = NewsFeedsEvent.ProfileInfoLoaded(
                                ProfileInfo(
                                    name = result.data?.store?.name.orEmpty()
                                )
                            ))
                        }
                    }
                }
            }
            triggerStateEvent(NewsFeedsEvent.ShowLoading(false))
        }
    }

    fun getProfileLocal(){
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(NewsFeedsEvent.ShowLoading(true))
            val profileInfo = sharedPrefs.getProfile()
            val userType = sharedPrefs.getUserInfoLocal().userTypeModel
            if(userType == UserTypeModel.BUYER){
                val userName = sharedPrefs.getProfile()
                if(userName.name?.isNotEmpty() == true){
                    triggerStateEvent(event = NewsFeedsEvent.ProfileInfoLoaded(
                        ProfileInfo(
                            name = userName.name
                        )
                    ))
                }
            }else{
                if(profileInfo.storeName?.isNotEmpty() == true){
                    triggerStateEvent(event = NewsFeedsEvent.ProfileInfoLoaded(
                        ProfileInfo(
                            store = profileInfo.store,
                            name = profileInfo.name
                        )
                    ))
                }
            }
            triggerStateEvent(NewsFeedsEvent.ShowLoading(false))
        }
    }

    fun getNotificationUnseen(){
        getCountNotification()
    }

    private fun getCountNotification(){
        viewModelScope.launch(Dispatchers.IO) {
            notificationUseCase.getCountNotSeen().collectLatest {
                when(it){
                    is Result.Success -> {
                        if(it.data?.total == null || it.data.total == 0)
                            return@collectLatest
                        it.data.total.let { it1 -> sharedPrefs.setTotalUnSeenNotification(it1) }
                        val total = sharedPrefs.getTotalUnSeenNotification()
                        triggerStateEvent(
                            event = NewsFeedsEvent.UpdateTotalUnseenNotification(
                                totalUnseenNotification = total
                            )
                        )
                    }
                }
            }
        }
    }

    init {
        getProfile()
        getCategories()
        getSections()
        initObserver()
        getCountNotification()
    }

    fun reload(){
        getSections()
        observerSectionLive()
    }

    private fun observerSectionLive(){
        viewModelScope.launch {
            _listSections.collectLatest { result ->
                triggerStateEvent(
                    NewsFeedsEvent.ListBodySectionsLoaded(
                        result?.filterNot { it.data.isNullOrEmpty() }?: listOf()
                    )
                )
            }
        }
    }

    private fun initObserver() {
        viewModelScope.launch {
            _listSections.collectLatest { result ->
                triggerStateEvent(
                    NewsFeedsEvent.ListBodySectionsLoaded(
                        result?.filterNot { it.data.isNullOrEmpty() }?: listOf()
                    )
                )
            }
        }

        viewModelScope.launch {
            _listCategories.collectLatest { result ->
                triggerStateEvent(
                    NewsFeedsEvent.ListCategoriesLoaded(
                        result?: listOf()
                    )
                )
            }
        }

        /*viewModelScope.launch {
            _listTrendingHashTags.collectLatest { result ->
                triggerStateEvent(
                    NewsFeedsEvent.ListHashTagsLoaded(
                        result?: listOf()
                    )
                )
            }
        }*/
    }

    fun sendTrackingCategory2(idCategory: Int?){
        viewModelScope.launch(Dispatchers.IO) {
            val userLocal = sharedPrefs.getUserInfoLocal()
            val trackingModel = TrackingModel(
                user_id = userLocal.userId,
                cat2_id = idCategory
            )
            AnalyticsManager.getInstance()?.trackingCategory2(trackingModel)
        }
    }

    fun sendTrackingCategory3(idCategory: Int?){
        viewModelScope.launch(Dispatchers.IO) {
            val userLocal = sharedPrefs.getUserInfoLocal()
            val trackingModel = TrackingModel(
                user_id = userLocal.userId,
                cat3_id = idCategory
            )
            AnalyticsManager.getInstance()?.trackingCategory3(trackingModel)
        }
    }

    fun sendTrackingSeeAllSection(titleSection: String?){
        viewModelScope.launch(Dispatchers.IO) {
            val userLocal = sharedPrefs.getUserInfoLocal()
            val trackingModel = TrackingModel(
                user_id = userLocal.userId,
                group_content = titleSection
            )
            AnalyticsManager.getInstance()?.trackingClickSeeAllGroupContent(trackingModel)
        }
    }

    fun trackingViewLiveStream(liveInput: LiveSteamShortInfo?){
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