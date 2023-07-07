package olmo.wellness.android.ui.screen.all_section_home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import olmo.wellness.android.R
import olmo.wellness.android.core.toJson
import olmo.wellness.android.data.model.live_stream.SectionType
import olmo.wellness.android.ui.common.ToolbarSchedule
import olmo.wellness.android.ui.common.empty.EmptyData
import olmo.wellness.android.ui.common.gridItems
import olmo.wellness.android.ui.common.live_stream.SingleLiveInfoItem
import olmo.wellness.android.ui.livestream.stream.data.TypeTitleLivestream
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*

@SuppressLint(
    "UnusedMaterialScaffoldPaddingParameter", "NewApi",
    "StateFlowValueCalledInComposition"
)
@Composable
fun SeeAllFilterLiveStreamScreen(
    sectionType: String,
    sectionTitle: String,
    navigation: NavController,
    viewModel: SeeAllFilterLiveStreamViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    val listSection = remember(uiState.value.listUpComing) {
        mutableStateOf(uiState.value.listUpComing)
    }
    val isLoading = viewModel.uiState.value.isLoading ?: false
    LaunchedEffect("Init") {
        viewModel.getListDataSection(sectionType)
    }
    Scaffold(
        topBar = {
            ToolbarSchedule(
                title = sectionTitle,
                backIconDrawable = R.drawable.ic_back_violet,
                navController = navigation,
                titleColor = Color_LiveStream_Main_Color,
                backgroundColor = White,
                onBackStackFunc = {
                    viewModel.clearState()
                    navigation.popBackStack()
                }
            )
        }, modifier = Modifier.fillMaxSize(),
        backgroundColor = GRAY_8F8
    ) {
        val state = rememberLazyListState()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = state
        ) {
            item {
                LoaderWithAnimation(isPlaying = isLoading)
            }
            if (listSection.value?.isEmpty()==true){
                item {
                    EmptyData()
                }
            }
            item {
                SpaceCompose(height = 16.dp)
            }
            listSection.value?.let {
                gridItems(it.size, 2) { index ->
                    listSection.value?.get(index)?.let { info ->
                        SingleLiveInfoItem(
                            modifier = Modifier.padding(bottom = 20.dp),
                            liveStreamInfo = info,
                            sectionType = SectionType.invoke(sectionType),
                            onItemClick = {
                                var typeFinal: TypeTitleLivestream = TypeTitleLivestream.SweeplistVideo
                                when (sectionType) {
                                    SectionType.TOP_TRENDING.value -> {
                                        typeFinal = TypeTitleLivestream.TopTrending
                                    }
                                    SectionType.LIVE_NOW.value -> {
                                        typeFinal = TypeTitleLivestream.LiveNow
                                        val liveData = it.copy(recordUrl = it.playbackUrl, typeTitle = typeFinal)
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
                                val liveData = it.copy(
                                    recordUrl = it.recordUrl,
                                    typeTitle = typeFinal
                                )
                                navigation.navigate(ScreenName.ExploreLiveStreamScreen.route.plus("?defaultData=${liveData.toJson()}"))
                            })
                    }
                }
            }
        }
    }
    LoadingScreen(isLoading = isLoading)
}