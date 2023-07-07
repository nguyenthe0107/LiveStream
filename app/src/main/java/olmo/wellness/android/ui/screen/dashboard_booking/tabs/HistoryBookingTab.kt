package olmo.wellness.android.ui.screen.dashboard_booking.tabs

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import olmo.wellness.android.core.toJson
import olmo.wellness.android.domain.model.booking.BookingHistoryInfo
import olmo.wellness.android.domain.model.booking.ServiceSessionInfo
import olmo.wellness.android.domain.model.state_dashboard_booking.StatusBookingDashBoardModel
import olmo.wellness.android.ui.common.empty.EmptyData
import olmo.wellness.android.ui.common.gridItems
import olmo.wellness.android.ui.screen.dashboard_booking.MyDashBoardBookingViewModel
import olmo.wellness.android.ui.screen.dashboard_booking.compose.ItemBookingInfo
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.screen.notification.OnBottomReached
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation

@SuppressLint("MutableCollectionMutableState")
@Composable
fun HistoryBookingTab(
    navController: NavController,
    type: String?,
    categoryId : Int?,
    viewModel: MyDashBoardBookingViewModel,
) {

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
        if (type != null) {
            viewModel.getDataBooking(type = type, page = pageCurrent.value, limit = 20)
        }
    }

    val listEvent = remember(uiState.value.listBookingItem) {
        mutableStateOf<MutableList<BookingHistoryInfo>?>(mutableListOf())
    }

    listEvent.value = uiState.value.listBookingItem?.get(type)

    scrollState.OnBottomReached() {
        if (listEvent.value?.isNotEmpty() == true) {
            pageCurrent.value = pageCurrent.value + 1
            if (type != null) {
                viewModel.getDataBooking(type = type, page = pageCurrent.value, limit = 20)
            }
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        state = scrollState,
    ){
        listEvent.value?.let {
            gridItems(it.size, 1) { index ->
                listEvent.value?.get(index)?.let { it1 ->
                    ItemBookingInfo(it1, navigationDetails = {
                        navController.navigate(ScreenName.MyServiceBookingDetails.route.plus("?defaultData=${it1.copy(type = type).toJson()}"))
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