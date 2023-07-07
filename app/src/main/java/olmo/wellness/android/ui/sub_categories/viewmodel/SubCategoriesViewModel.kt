package olmo.wellness.android.ui.sub_categories.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.ui.base.BaseViewModel
import javax.inject.Inject


@HiltViewModel
class SubCategoriesViewModel @Inject constructor() :
    BaseViewModel<SubCategoriesState, SubCategoriesEvent>() {


    init {
        getListLiveNow()
        getListTab()
    }


    override fun initState(): SubCategoriesState {
        return SubCategoriesState()
    }

    override fun onTriggeredEvent(event: SubCategoriesEvent) {
        when (event) {
            is SubCategoriesEvent.ShowLoading -> {
                setState(uiState.value.copy(isLoading = event.isLoading))
            }
            is SubCategoriesEvent.ListLiveNow -> {
                setState(
                    uiState.value.copy(
                        isLoading = false,
                        listLiveNow = event.listLiveNow
                    )
                )
            }
            is SubCategoriesEvent.ListTab -> {
                setState(
                    uiState.value.copy(
                        isLoading = false,
                        listTab = event.listTab
                    )
                )
            }
        }
    }

    private fun getListLiveNow() {
        val list = mutableListOf<LiveSteamShortInfo>().apply {
            add(
                LiveSteamShortInfo(
                    id = 1,
                    title = "How To Sleep Well",
                    description = "What Sleep?",
                    viewCount = 100
                )
            )
            add(
                LiveSteamShortInfo(
                    id = 1,
                    title = "How To Sleep Well",
                    description = "What Sleep?",
                    viewCount = 100
                )
            )
            add(
                LiveSteamShortInfo(
                    id = 1,
                    title = "How To Sleep Well",
                    description = "What Sleep?",
                    viewCount = 100
                )
            )
            add(
                LiveSteamShortInfo(
                    id = 1,
                    title = "How To Sleep Well",
                    description = "What Sleep?",
                    viewCount = 100
                )
            )
            add(
                LiveSteamShortInfo(
                    id = 1,
                    title = "How To Sleep Well",
                    description = "What Sleep?",
                    viewCount = 100
                )
            )
            add(
                LiveSteamShortInfo(
                    id = 1,
                    title = "How To Sleep Well",
                    description = "What Sleep?",
                    viewCount = 100
                )
            )
            add(
                LiveSteamShortInfo(
                    id = 1,
                    title = "How To Sleep Well",
                    description = "What Sleep?",
                    viewCount = 100
                )
            )
            add(
                LiveSteamShortInfo(
                    id = 1,
                    title = "How To Sleep Well",
                    description = "What Sleep?",
                    viewCount = 100
                )
            )
            add(
                LiveSteamShortInfo(
                    id = 1,
                    title = "How To Sleep Well",
                    description = "What Sleep?",
                    viewCount = 100
                )
            )
        }
        triggerStateEvent(SubCategoriesEvent.ListLiveNow(list))
    }


    private fun getListTab() {
        val list = arrayListOf("Live Now", "Upcoming", "Top Trending", "Event")
        triggerStateEvent(SubCategoriesEvent.ListTab(list))
    }
}