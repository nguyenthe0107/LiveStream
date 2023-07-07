package olmo.wellness.android.ui.screen.booking_service.add_booking

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.booking_service.ItemBookingService
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.common.empty.EmptyBottomSheet
import olmo.wellness.android.ui.livestream.schedule.component.SwipeCompose
import olmo.wellness.android.ui.livestream.utils.Effects
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition",
    "SuspiciousIndentation")
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddBookingServiceBottomSheet(
    liveStreamId: Int? = null,
    servicesSelected: List<ServiceBooking>? = emptyList(),
    modalBottomSheetState: ModalBottomSheetState,
    isLiveStream: Boolean? = false,
    viewModel: AddBookingServiceViewModel = hiltViewModel(),
    confirmCallback: ((List<ServiceBooking>?) -> Unit)? = null,
    cancelCallback: ((Boolean) -> Unit)? = null,
    roomId : String? = null,
    openListOfService: ((Boolean) -> Unit)? = null,
    onSelectedItemToShowCallback: ((ServiceBooking?) -> Unit)? = null,
    notifyChangeListService: ((List<ServiceBooking>?) -> Unit)? = null,
){
    if (servicesSelected?.isNotEmpty() == true) {
        LaunchedEffect(servicesSelected) {
            viewModel.bindServiceSelected(inputService = servicesSelected)
        }
    }

    if (liveStreamId != null && roomId!=null) {
        LaunchedEffect(Unit) {
            viewModel.bindLiveStream(liveStreamId = liveStreamId, roomId = roomId)
        }
    }
    val uiState = viewModel.uiState.collectAsState()
//    LaunchedEffect(uiState.value.serviceSelected){
        if (uiState.value.serviceSelected!=null){
            onSelectedItemToShowCallback?.invoke(uiState.value.serviceSelected)
        }
//    }

    val listServices = uiState.value.listServices
    val lifecycleOwner = LocalLifecycleOwner.current
    Effects.Disposable(
        lifeCycleOwner = lifecycleOwner,
        onStart = {
            viewModel.fetchServices()
        },
        onStop = {
        }
    )
    ModalBottomSheetLayout(sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContentColor = White, sheetContent = {
            if (modalBottomSheetState.isVisible) {
                Scaffold(modifier = Modifier
                    .fillMaxHeight(0.75f)
                    .background(White),
                    bottomBar = {
                        if (isLiveStream == null || isLiveStream == false) {
                            BottomBar(confirmCallback,
                                cancelCallback,
                                listServices?.size,
                                viewModel,
                                servicesSelected?.size,
                                notifyChangeListService)
                        }
                    }) {
                    Box(modifier = Modifier) {
                        Column(
                            modifier = Modifier
                                .background(White)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {

                                Title(openListOfService)

                                Column(modifier = Modifier.padding(16.dp)) {
                                    if (listServices.isNullOrEmpty()) {
                                        QuantityServiceItem(0)
                                    }
                                    if (listServices.isNullOrEmpty()) {
                                        EmptyServiceBooking()
                                    } else {
                                        Body(listServices,
                                            viewModel,
                                            isLiveStream,
                                            modalBottomSheetState)
                                    }
                                }
                            }
                        }
                        LoadingScreen(isLoading = uiState.value.isLoading)
                    }
                }
            } else {
                EmptyBottomSheet()
            }
        }) {
    }
}

@Composable
private fun EmptyServiceBooking() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 10.dp)) {
        AsyncImage(
            model = R.drawable.olmo_img_onboard_right,
            contentDescription = "",
            modifier = Modifier
                .size(160.dp, 191.dp)
                .align(Alignment.Center)
        )
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun BottomBar(
    confirmCallback: ((List<ServiceBooking>?) -> Unit)? = null,
    cancelCallback: ((Boolean) -> Unit)? = null,
    sizeService: Int? = null,
    viewModel: AddBookingServiceViewModel,
    servicesInput: Int? = null,
    notifyChangeListService: ((List<ServiceBooking>?) -> Unit)? = null,
) {
    val listSelected = viewModel.uiState.value.listServices
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    bottom = 32.dp,
                    top = 12.dp,
                    end = 16.dp,
                    start = 16.dp
                )
        ) {
            SecondLiveButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp),
                stringResource(id = R.string.action_back),
                onClickFunc = {
                    cancelCallback?.invoke(true)
                    if (listSelected?.size != servicesInput) {
                        notifyChangeListService?.invoke(listSelected)
                    }
                }
            )
            PrimaryLiveButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp),
                stringResource(id = R.string.lb_confirm),
                onClickFunc = {
                    confirmCallback?.invoke(listSelected)
                    if (listSelected?.size != servicesInput) {
                        notifyChangeListService?.invoke(listSelected)
                    }
                },
                enable = sizeService != null && sizeService != 0
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Body(
    listService: List<ServiceBooking>,
    viewModel: AddBookingServiceViewModel,
    isLiveStream: Boolean? = false,
    modalBottomSheetState: ModalBottomSheetState,
) {
    val stateLazy = rememberLazyListState()
    val scope = rememberCoroutineScope()
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(Color.White)
    ) {
        LazyColumn(content = {
            item {
                QuantityServiceItem(listService.size)
            }
            items(listService, key = {
                it.id ?: 0
            }) { info ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .wrapContentHeight(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    SwipeCompose(
                        color = White,
                        isShowDeleteItemDefault = true,
                        isShowOptionDefault = false,
                        disableSwipeLeftToRight = true,
                        content = {
                            ItemBookingService(isLiveStreaming = isLiveStream,
                                serviceBooking = info, onSelected = {
                                    viewModel.sendBookMark(it?.id)
                                    scope.launch {
                                        modalBottomSheetState.hide()
                                    }
                                })
                        },
                        callbackDeleteItemUpcoming = {
                            info.id?.let { viewModel.deleteService(it, isLiveStream) }
                        },
                    )
                }
            }

            item {
                SpaceCompose(height = 80.dp)
            }

        }, state = stateLazy)
    }
}

@Composable
private fun Title(openListOfService: ((Boolean) -> Unit)? = null) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .height(60.dp)
    ) {
        Text(
            text = stringResource(R.string.title_add_booking_service),
            style = MaterialTheme.typography.subtitle2.copy(
                fontSize = 18.sp,
                lineHeight = 26.sp,
                color = Color_Green_Main,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.align(Alignment.Center)
        )

        Text(
            text = stringResource(R.string.action_add_booking_service),
            style = MaterialTheme.typography.subtitle2.copy(
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = Color_Green_Main,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
                .noRippleClickable {
                    openListOfService?.invoke(true)
                }
        )

        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(color = Color_BLUE_7F4)
                .align(Alignment.BottomStart)
        )
    }
}

@Composable
fun QuantityServiceItem(quantity: Int? = null) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 26.dp)) {
        Text(
            text = stringResource(R.string.title_quantity_booking_service).plus(quantity?.toString()),
            style = MaterialTheme.typography.subtitle2.copy(
                fontSize = 14.sp,
                lineHeight = 26.sp,
                color = Neutral_Gray_9,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.align(Alignment.CenterStart)
        )
    }
}