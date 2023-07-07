package olmo.wellness.android.ui.screen.for_you.tab

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.FixedScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import olmo.wellness.android.core.toJson
import olmo.wellness.android.data.model.live_stream.SectionType
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.ui.common.empty.EmptyData
import olmo.wellness.android.ui.common.gridItems
import olmo.wellness.android.ui.common.live_stream.SingleLiveInfoItem
import olmo.wellness.android.ui.livestream.stream.data.TypeTitleLivestream
import olmo.wellness.android.ui.screen.for_you.viewmodel.ForYouViewModel
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.notification.OnBottomReached
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation

@SuppressLint("MutableCollectionMutableState")
@Composable
fun EventTab(
    navigation: NavController,
    type: String?,
    categoryId : Int?,
    viewModel: ForYouViewModel,
) {

    val scope = rememberCoroutineScope()
    val uiState = viewModel.uiState.collectAsState()
    val pageCurrent = remember {
        mutableStateOf(1)
    }

    val scrollState = rememberLazyListState()

    val isLoading = remember {
        mutableStateOf(true)
    }
    isLoading.value = uiState.value.isLoading

    LaunchedEffect(Unit) {
        viewModel.getData(title = type, page = pageCurrent.value, categoryId = categoryId)
    }

    val listEvent = remember() {
        mutableStateOf< MutableList<LiveSteamShortInfo>?>(mutableListOf())
    }

    listEvent.value = uiState.value.listItemTypes?.get(type)

    scrollState.OnBottomReached() {
        if (listEvent.value?.isNotEmpty() == true) {
            pageCurrent.value = pageCurrent.value + 1
            viewModel.getData(title = type, page = pageCurrent.value, categoryId = categoryId)
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 5.dp, start = 5.dp, top = 15.dp),
        state = scrollState,
    ) {
        listEvent.value?.let {
            gridItems(it.size, 2) { index ->
                listEvent.value?.get(index)?.let { it1 ->
                    SingleLiveInfoItem(
                        modifier = Modifier.padding(bottom = 20.dp),
                        liveStreamInfo = it1,
                        sectionType = SectionType.UPCOMING,
                        onItemClick = {
                            var typeFinal: TypeTitleLivestream = TypeTitleLivestream.SweeplistVideo
                            when (type) {
                                SectionType.TOP_TRENDING.value -> {
                                    typeFinal = TypeTitleLivestream.TopTrending
                                }
                                SectionType.LIVE_NOW.value -> {
                                    typeFinal = TypeTitleLivestream.LiveNow
                                    val liveData =
                                        it.copy(recordUrl = it.playbackUrl, typeTitle = typeFinal)
                                    navigation.navigate(
                                        ScreenName.PlayBackOnLiveStreamScreen.route.plus(
                                            "?defaultData=${liveData.toJson()}"
                                        )
                                    )
                                    return@SingleLiveInfoItem
                                }
                                SectionType.EVENT.value -> {
                                    typeFinal = TypeTitleLivestream.Event
                                }
                                SectionType.RECOMMENDED.value -> {
                                    typeFinal = TypeTitleLivestream.Recommended
                                }
                            }
                            val liveData = it.copy(recordUrl = it.recordUrl, typeTitle = typeFinal)
                            navigation.navigate(ScreenName.ExploreLiveStreamScreen.route.plus("?defaultData=${liveData.toJson()}"))
                            viewModel.trackingViewLiveStream(it)
                        })
                }

            }
        }

        item {
            LoaderWithAnimation(isPlaying = isLoading.value)
        }

        if (listEvent.value?.isEmpty() == true || listEvent.value==null) {
            item {
                EmptyData()
            }
        }

        item {
            Spacer(modifier = Modifier.padding(vertical = 30.dp))
        }
    }
}