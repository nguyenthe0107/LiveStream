package olmo.wellness.android.ui.screen.playback_video.bottom_sheet.voucher_service

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.voucher.VoucherInfo
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.common.empty.EmptyBottomSheet
import olmo.wellness.android.ui.common.empty.EmptyData
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.livestream.utils.Effects
import olmo.wellness.android.ui.screen.signup_screen.utils.LoadingScreen
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceHorizontalCompose
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VoucherServiceBottomSheet(
    sessionConfirmId: Double? = null,
    modalBottomSheetState: ModalBottomSheetState,
    viewModel: VoucherServiceViewModel = hiltViewModel(),
    confirmCallback: ((VoucherInfo?) -> Unit) ?= null,
    cancelCallback: ((Boolean) -> Unit) ?= null,
){
    if(sessionConfirmId != null){
        if(modalBottomSheetState.isVisible){
            viewModel.bindDataInput(sessionConfirmId)
            viewModel.fetchListVoucher()
        }
    }
    val uiState = viewModel.uiState.collectAsState()
    ModalBottomSheetLayout(sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContentColor = White,sheetContent = {
            if (modalBottomSheetState.isVisible) {
                Scaffold(modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .background(White),
                    bottomBar = {
                        BottomBar(confirmCallback, cancelCallback, modalBottomSheetState,viewModel)
                    }) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .background(White)
                                .fillMaxSize()
                                .padding(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ){
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Title(cancelCallback)
                                Body(viewModel = viewModel)
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


@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun BottomBar(
    confirmCallback: ((VoucherInfo?) -> Unit) ?= null,
    cancelCallback: ((Boolean) -> Unit) ?= null,
    modalBottomSheetState: ModalBottomSheetState,
    viewModel: VoucherServiceViewModel
) {
    val uiState = viewModel.uiState.collectAsState()
    val isEnable = uiState.value.voucherSelected != null
    val mainScope = rememberCoroutineScope()
    val voucherInfo = uiState.value.voucherSelected
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(White)
    ){
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
        ){
            SecondLiveButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp),
                stringResource(id = R.string.action_back),
                onClickFunc = {
                    cancelCallback?.invoke(true)
                    mainScope.launch {
                        modalBottomSheetState.hide()
                    }
                }
            )
            PrimaryLiveButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp),
                stringResource(id = R.string.lb_confirm),
                onClickFunc = {
                    confirmCallback?.invoke(voucherInfo)
                    mainScope.launch {
                        modalBottomSheetState.hide()
                    }
                },
                enable = isEnable
            )
        }
    }
}

@Composable
private fun Body(viewModel: VoucherServiceViewModel){
    val stateLazy = rememberLazyListState()
    val uiState = viewModel.uiState.collectAsState()
    val listVoucher = uiState.value.listVoucher?.distinctBy { it.id } ?: emptyList()
    var status by remember {
        mutableStateOf(false)
    }
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(Color.White)
    ){
        LazyColumn(content = {
            items(listVoucher, key = { it.id ?: 0 }){ voucherInfo ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .wrapContentHeight()
                        .noRippleClickable {
                            status = !status
                            viewModel.onSelectedVoucher(voucherInfo.copy(isSelected = status))
                        },
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ){
                    val colorBackground = if(voucherInfo.active == null || voucherInfo.active == true) Color_LiveStream_Main_Color else Neutral_Gray_6
                    TicketCompose(content = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(border = BorderStroke(1.dp, color = colorBackground))
                                .fillMaxHeight()
                        ){
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .background(color = if (voucherInfo.active == null || voucherInfo.active == true) Color_LiveStream_Main_Color else Neutral_Gray_7)
                                        .padding(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    AsyncImage(
                                        model = R.drawable.img_macos_voucher,
                                        contentScale = ContentScale.Inside,
                                        contentDescription = "", modifier = Modifier
                                    )
                                    Text(
                                        text = stringResource(id = R.string.value_voucher_default_information_booking),
                                        style = MaterialTheme.typography.subtitle2.copy(
                                            color = White,
                                            fontSize = 16.sp,
                                            lineHeight = 20.sp,
                                            fontFamily = MontserratFont,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .weight(1.5f)
                                        .fillMaxHeight()
                                        .padding(start = 16.dp),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ){
                                    Column(verticalArrangement = Arrangement.Top) {
                                        SpaceCompose(height = 10.dp)
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text(
                                                text = voucherInfo.voucher.orEmpty(),
                                                style = MaterialTheme.typography.subtitle2.copy(
                                                    color = Neutral_Gray_6,
                                                    fontSize = 12.sp,
                                                    lineHeight = 15.sp,
                                                    fontFamily = MontserratFont,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            )
                                            var iconCheck by remember {
                                                mutableStateOf(R.drawable.olmo_ic_uncheck)
                                            }
                                            iconCheck = if(voucherInfo.isSelected == true){
                                                R.drawable.olmo_ic_circle_check
                                            }else{
                                                R.drawable.olmo_ic_uncheck
                                            }
                                            Image(painter = painterResource(id = iconCheck),
                                                contentDescription = "", modifier = Modifier.padding(end = 12.dp))
                                        }
                                        SpaceCompose(height = 8.dp)
                                        Text(
                                            text = voucherInfo.description.orEmpty(),
                                            style = MaterialTheme.typography.subtitle2.copy(
                                                color = Neutral_Gray_5,
                                                fontSize = 10.sp,
                                                lineHeight = 15.sp,
                                                fontFamily = MontserratFont,
                                                fontWeight = FontWeight.Normal
                                            ),
                                            maxLines = 2,
                                            modifier = Modifier.padding(end = 12.dp)
                                        )
                                        SpaceCompose(height = 8.dp)
                                        val timeString = voucherInfo.createdAt?.let {
                                            DateTimeHelper.convertDataToString(
                                                it,
                                                DateTimeHelper.FORMAT_TIME_BIRTHDAY
                                            )
                                        }
                                        Text(
                                            text = stringResource(id = R.string.label_voucher_time_information_booking) + ": $timeString",
                                            style = MaterialTheme.typography.subtitle2.copy(
                                                color = Neutral_Gray_5,
                                                fontSize = 10.sp,
                                                lineHeight = 15.sp,
                                                fontFamily = MontserratFont,
                                                fontWeight = FontWeight.Normal
                                            ),
                                        )
                                    }
                                    Column(verticalArrangement = Arrangement.Bottom) {
                                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                            Image(
                                                painter = painterResource(id = R.drawable.olmo_ic_introduce_voucher),
                                                contentDescription = ""
                                            )
                                            SpaceHorizontalCompose(width = 8.dp)
                                            Text(
                                                text = stringResource(id = R.string.label_voucher_condition_information_booking),
                                                style = MaterialTheme.typography.subtitle2.copy(
                                                    color = Neutral_Gray_6,
                                                    fontSize = 10.sp,
                                                    lineHeight = 15.sp,
                                                    fontFamily = MontserratFont,
                                                    fontWeight = FontWeight.Normal
                                                )
                                            )
                                        }
                                        SpaceCompose(height = 10.dp)
                                    }
                                }
                            }
                        }
                    }, isAvailable = voucherInfo.available ?: false)
                }
            }

            item {
               SpaceCompose(height = 80.dp)
            }
            
        }, state = stateLazy)

        if(listVoucher.isEmpty()){
            EmptyData()
        }
    }
}

@Composable
private fun Title(cancelCallback: ((Boolean) -> Unit) ?= null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(Color.White)
                .height(60.dp)
        ){
            Text(
                text = stringResource(R.string.title_voucher_information_booking),
                style = MaterialTheme.typography.subtitle2.copy(
                    fontSize = 18.sp,
                    lineHeight = 26.sp,
                    color = Neutral_Gray_9,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.align(Alignment.Center)
            )

            Image(
                painterResource(id = R.drawable.ic_close),
                contentDescription = "ic_close",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .noRippleClickable {
                        cancelCallback?.invoke(true)
                    }
            )

            Spacer (
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(color = Neutral_Gray_3)
                    .align(Alignment.BottomStart)
            )
    }
}
