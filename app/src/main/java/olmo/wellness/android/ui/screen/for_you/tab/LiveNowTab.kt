package olmo.wellness.android.ui.screen.for_you.tab

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.FixedScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import olmo.wellness.android.core.toJson
import olmo.wellness.android.data.model.live_stream.SectionType
import olmo.wellness.android.ui.common.empty.EmptyData
import olmo.wellness.android.ui.common.gridItems
import olmo.wellness.android.ui.common.live_stream.SingleLiveInfoItem
import olmo.wellness.android.ui.livestream.stream.data.TypeTitleLivestream
import olmo.wellness.android.ui.screen.for_you.category.SubCategoryBottomSheet
import olmo.wellness.android.ui.screen.for_you.viewmodel.ForYouViewModel
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.notification.OnBottomReached
import olmo.wellness.android.ui.screen.playback_video.home.CategoriesSection
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LiveNowTab(
    navigation: NavController,
    type: String?,
    categoryId : Int?,
    viewModel: ForYouViewModel,
    openBottomSheet: MutableState<Boolean>?
) {
    val scope = rememberCoroutineScope()
    val uiState = viewModel.uiState.collectAsState()

    val scrollState = rememberLazyListState()


    val listCategories = remember(uiState.value.listCategories) {
        mutableStateOf(uiState.value.listCategories)
    }


    val pageCurrent = remember {
        mutableStateOf(1)
    }

    val isLoading = remember {
        mutableStateOf(true)
    }
    isLoading.value= uiState.value.isLoading

    val listSubCategories = remember(uiState.value.listSubCategories) {
        uiState.value.listSubCategories
    }

    val titleCategory = remember(uiState.value.title) {
        uiState.value.title
    }

    LaunchedEffect(Unit) {
        viewModel.getData(title = type, page = pageCurrent.value, categoryId = categoryId)
    }

    val listLiveNow = remember(uiState.value.listItemTypes?.get(type)) {
        mutableStateOf(uiState.value.listItemTypes?.get(type))
    }

    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    openBottomSheet?.value = modalBottomSheetState.isVisible

    scrollState.OnBottomReached() {
        if (listLiveNow.value?.isNotEmpty() == true) {
            pageCurrent.value = pageCurrent.value + 1
            viewModel.getData(title = type, page = pageCurrent.value, categoryId = categoryId)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 5.dp, start = 5.dp, top = 5.dp),
        state = scrollState,
        ) {
        if (openBottomSheet != null) {
            item {
                CategoriesSection(listCategories = listCategories.value, onViewAllClick = {
                    viewModel.getSubListCategories(it)
                    scope.launch {
                        modalBottomSheetState.show()
                    }
                })
            }
        }

        listLiveNow.value?.let {
            gridItems(it.size, 2) { index ->
                SingleLiveInfoItem(
                    modifier = Modifier.padding(bottom = 20.dp),
                    liveStreamInfo = listLiveNow.value!![index],
//                    contentScale = FixedScale(2f),
                    sectionType = SectionType.LIVE_NOW,
                    onItemClick = {
                        val liveData = it.copy(recordUrl = it.playbackUrl, typeTitle = TypeTitleLivestream.LiveNow)
                        navigation.navigate(ScreenName.PlayBackOnLiveStreamScreen.route.plus("?defaultData=${liveData.toJson()}"))
                        viewModel.trackingViewLiveStream(it)
                    })
            }
        }

        item {
            Spacer(modifier = Modifier.padding(vertical = 30.dp))
        }

        item {
            LoaderWithAnimation(isPlaying = isLoading.value)
        }

        if (listLiveNow.value==null || listLiveNow.value?.isEmpty() == true) {
            item {
                EmptyData()
            }
        }
    }

    if (openBottomSheet != null) {
        SubCategoryBottomSheet(
            modalBottomSheetState = modalBottomSheetState,
            titleCategory = titleCategory,
            listCategories = listSubCategories, onClick = {
                scope.launch {
                    modalBottomSheetState.hide()
                    navigation.navigate(ScreenName.SubCategoriesScreen.route.plus("?title=${it.nameLocale?.en}").plus("&id=${it.id}"))
                }
            }
        )
    }
}


