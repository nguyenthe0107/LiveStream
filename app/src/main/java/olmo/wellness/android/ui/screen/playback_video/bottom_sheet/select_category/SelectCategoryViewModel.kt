package olmo.wellness.android.ui.screen.playback_video.bottom_sheet.select_category

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import olmo.wellness.android.domain.use_case.HomeLiveUseCase
import olmo.wellness.android.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SelectCategoryViewModel @Inject constructor(
    private val homeLiveUseCase: HomeLiveUseCase
) : BaseViewModel<SelectCategoryState, SelectCategoryEvent>() {

    override fun initState(): SelectCategoryState = SelectCategoryState()

    private fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            triggerStateEvent(SelectCategoryEvent.ShowLoading(true))
            homeLiveUseCase.getListCategories().collect { result ->
                if (result.data != null) {
                    triggerStateEvent(SelectCategoryEvent.ListCategoriesLoaded(result.data))
                } else {
                    triggerStateEvent(SelectCategoryEvent.ShowLoading(false))

                }
            }
        }
    }

    init {
        getCategories()
    }


    override fun onTriggeredEvent(event: SelectCategoryEvent) {
        when (event) {
            is SelectCategoryEvent.ShowLoading -> {
                setState(
                    uiState.value.copy(
                        isLoading = event.isLoading
                    )
                )
            }
            is SelectCategoryEvent.ListCategoriesLoaded -> {
                setState(
                    uiState.value.copy(
                        listCategories = event.listCategories,
                        isLoading = false
                    )
                )
            }
        }
    }
}