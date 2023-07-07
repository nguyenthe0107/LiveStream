package olmo.wellness.android.ui.screen.search_home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.core.Constants
import olmo.wellness.android.core.toJson
import olmo.wellness.android.ui.common.QuerySearch
import olmo.wellness.android.ui.common.ShimmerBox
import olmo.wellness.android.ui.common.empty.EmptyBottomSheet
import olmo.wellness.android.ui.livestream.stream.data.LivestreamStatus
import olmo.wellness.android.ui.livestream.stream.data.TypeTitleLivestream
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.search_home.cell.ItemSearch
import olmo.wellness.android.ui.theme.*

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchHomeBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    navController: NavController,
    onCloseCallBack: ((Boolean) -> Unit)?=null,
    viewModel: SearchViewModel = hiltViewModel()) {
    val textSearch = remember { mutableStateOf("") }
    val listSearchState  = viewModel.searchData.collectAsState()
    val listDataFilter = listSearchState.value ?: emptyList()
    val loading = viewModel.isLoading.collectAsState()
    val focusManager = LocalFocusManager.current
    val dataSelected = viewModel.liveStreamSelected.collectAsState()
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContent = {
         if (modalBottomSheetState.isVisible) {
             if (dataSelected.value != null) {
                 val videoInfo = dataSelected.value
                 when (videoInfo?.status) {
                     LivestreamStatus.ON_LIVE.name -> {
                         val liveData =
                             videoInfo.copy(recordUrl = videoInfo.playbackUrl, fromSearch = true)
                         navController.navigate(ScreenName.PlayBackOnLiveStreamScreen.route.plus("?defaultData=${liveData.toJson()}"))
                         scope.launch {
                             modalBottomSheetState.hide()
                         }
                         viewModel.resetData()
                     }
                     else -> {
                         val liveData = videoInfo?.copy(
                             recordUrl = videoInfo.recordUrl,
                             typeTitle = TypeTitleLivestream.SweeplistVideo,
                             fromSearch = true
                         )
                         navController.navigate(ScreenName.ExploreLiveStreamScreen.route.plus("?defaultData=${liveData?.toJson()}"))
                         scope.launch {
                             modalBottomSheetState.hide()
                         }
                         viewModel.resetData()
                     }
                 }
             }
             Column(
                 modifier = Modifier
                     .fillMaxWidth()
                     .fillMaxHeight(0.9f)
                     .padding(16.dp)
             ) {
                 QuerySearch(
                     label = "Search",
                     modifier = Modifier
                         .fillMaxWidth()
                         .padding(bottom = 15.dp)
                         .background(Neutral_Gray_3, RoundedCornerShape(50.dp)),
                     query = textSearch.value,
                     onQueryChanged = { updatedAddress ->
                         textSearch.value = updatedAddress
                         viewModel.getAllSearch(textSearch.value)
                     },
                     onDoneActionClick = {
                         focusManager.clearFocus()
                     },
                     onClearClick = {
                         textSearch.value = ""
                         viewModel.clearData()
                         focusManager.clearFocus()
                     },
                     corner = 16.dp,
                     textSize = 14.sp,
                     iconSize = 20.dp,
                     showIconSearch = true,
                     iconClose = R.drawable.ic_close_search,
                     iconSearch = R.drawable.ic_search_home
                 )
                 val stateList = rememberLazyListState()
                 LazyColumn(modifier = Modifier.fillMaxWidth(), state = stateList) {
                     if (loading.value) {
                         items(3) {
                             ShimmerBox(
                                 height = 48.dp,
                                 radius = 16.dp,
                                 modifier = Modifier.padding(
                                     top = 10.dp,
                                 )
                             )
                         }
                     }
                     if (listDataFilter.isNotEmpty()) {
                         item {
                             Text(
                                 text = "${listDataFilter.size} ${stringResource(R.string.lb_results)}",
                                 modifier = Modifier.padding(vertical = 10.dp),
                                 style = MaterialTheme.typography.subtitle2.copy(
                                     fontSize = 14.sp
                                 )
                             )
                         }
                         items(listDataFilter.size) { index ->
                             ItemSearch(listDataFilter[index], onClickCallback = {
                                 onCloseCallBack?.invoke(true)
                                 focusManager.clearFocus()
                                 val it = listDataFilter[index]
                                 when (it.status) {
                                     LivestreamStatus.UPCOMING.name -> {
                                         navController.navigate(
                                             ScreenName.EventDetailScreen.route.plus(
                                                 "?${Constants.KEY_ID}=${it.id}"
                                             )
                                         )
                                     }
                                     else -> {
                                         viewModel.handleNavigation(it.id)
                                     }
                                 }
                             })
                             Spacer(modifier = Modifier.padding(vertical = 5.dp))
                         }
                     }
                 }
             }
         }else{
             EmptyBottomSheet()
         }
        }) {

    }
}