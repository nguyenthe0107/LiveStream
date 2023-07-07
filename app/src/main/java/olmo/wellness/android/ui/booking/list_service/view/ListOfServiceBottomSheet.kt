package olmo.wellness.android.ui.booking.list_service.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import olmo.wellness.android.core.Constants
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.booking.list_service.viewmodel.ListOfServiceViewModel
import olmo.wellness.android.ui.chat.conversation_list.cell.SearchConversation
import olmo.wellness.android.ui.common.RoundedAsyncImage
import olmo.wellness.android.ui.common.empty.EmptyBottomSheet
import olmo.wellness.android.ui.common.empty.EmptyData
import olmo.wellness.android.ui.common.gridItems
import olmo.wellness.android.ui.screen.notification.OnBottomReached
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.theme.*
import olmo.wellness.android.util.getConvertCurrency

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "MutableCollectionMutableState")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListOfServiceBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    viewModel: ListOfServiceViewModel = hiltViewModel(),
    onDone: (List<ServiceBooking>?) -> Unit
) {

    val scope = rememberCoroutineScope()

    val isDone = remember {
        mutableStateOf(false)
    }

    val focusManager =  LocalFocusManager.current

    val isSelectAll = remember {
        mutableStateOf(false)
    }

    val page = remember {
        mutableStateOf(1)
    }

    val keySearch = remember {
        mutableStateOf<String?>("")
    }

    val serviceSelect = remember {
        mutableStateOf<MutableList<ServiceBooking>?>(mutableListOf())
    }

    isDone.value = serviceSelect.value?.isNotEmpty() == true

    LaunchedEffect(Unit) {
        viewModel.getServices(page = page.value, _search = keySearch.value)
    }

    val uiState = viewModel.uiState.collectAsState()

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RectangleShape,
        sheetBackgroundColor = White,
        sheetContent = {
            if (modalBottomSheetState.isVisible) {
                Scaffold(
                    topBar = {
                        Surface(elevation = 4.dp, color = White) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = White)
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = olmo.wellness.android.R.drawable.ic_close),
                                    contentDescription = "Close",
                                    tint = Black_037,
                                    modifier = Modifier.noRippleClickable {
                                        focusManager.clearFocus()
                                        scope.launch {
                                            modalBottomSheetState.hide()
                                        }
                                    }
                                )
                                Text(
                                    text = stringResource(olmo.wellness.android.R.string.lb_list_of_services),
                                    style = MaterialTheme.typography.subtitle2.copy(
                                        lineHeight = 26.sp, fontSize = 18.sp
                                    ), modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center
                                )

                                Text(
                                    text = stringResource(id = olmo.wellness.android.R.string.done),
                                    style = MaterialTheme.typography.subtitle2.copy(
                                        lineHeight = 22.sp, fontSize = 14.sp,
                                        color = (if (isDone.value) Color_LiveStream_Main_Color else Color_gray_FE3)
                                    ), modifier = Modifier
                                        .padding(start = 4.dp)
                                        .noRippleClickable {
                                            if (isDone.value) {
                                                onDone.invoke(serviceSelect.value)
                                                scope.launch {
                                                    modalBottomSheetState.hide()
                                                }
                                            }
                                        }
                                )
                            }
                        }
                    }, modifier = Modifier
                        .fillMaxSize()
                        .background(White)
                ) {
                    val lazyListState = rememberLazyListState()

                    if (uiState.value.listOfServices?.isEmpty() == true) {
                        EmptyData()
                    }

                    lazyListState.OnBottomReached(buffer = Constants.PAGE_SIZE) {
                        if (uiState.value.listOfServices?.isNotEmpty() == true) {
                            page.value = page.value + 1
                            viewModel.getServices(page.value, _search = keySearch.value)
                        }
                    }
                    LazyColumn(modifier = Modifier.padding(8.dp)) {
                        item {
                            SearchConversation(hint = "Name, service ID,...", onTextChange = {
                                keySearch.value = it
                                page.value = 1
                                viewModel.getServices(page.value, keySearch.value)
                            })

                        }

                        if (uiState.value.listOfServices?.isNotEmpty() == true) {
                            item {
                                Row(
                                    modifier = Modifier.padding(
                                        top = 20.dp,
                                        start = 10.dp,
                                        bottom = 12.dp
                                    ).noRippleClickable {
                                        isSelectAll.value = !isSelectAll.value
                                        if (isSelectAll.value) {
                                            uiState.value.listOfServices?.forEach {
                                                addBookingSelect(it, serviceSelect)
                                            }
                                        } else {
                                            serviceSelect.value = mutableListOf()
                                        }
                                    },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = isSelectAll.value, onCheckedChange = {
                                            isSelectAll.value = it
                                            if (it) {
                                                uiState.value.listOfServices?.forEach {
                                                    addBookingSelect(it, serviceSelect)
                                                }
                                            } else {
                                                serviceSelect.value = mutableListOf()
                                            }
                                        }, modifier = Modifier.size(16.dp),
                                        colors = CheckboxDefaults.colors(
                                            checkmarkColor = White,
                                            checkedColor = Color_LiveStream_Main_Color
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(
                                        text = stringResource(id = olmo.wellness.android.R.string.lb_select_all),
                                        style = MaterialTheme.typography.subtitle1.copy(
                                            fontSize = 14.sp, lineHeight = 22.sp
                                        )
                                    )
                                }
                            }
                            gridItems(uiState.value.listOfServices?.size!!, 2) { index ->
                                val data = uiState.value.listOfServices?.get(index)
                                Service(data = data, serviceSelect = serviceSelect)
                            }
                        } else {
                            item {
                                LoaderWithAnimation(isPlaying = uiState.value.showLoading)
                            }
                        }
                    }

                }
            }else{
                EmptyBottomSheet()
            }
        }
    ) {

    }
}


private fun checkBookingSelect(booking: ServiceBooking?, list: List<ServiceBooking>?): Boolean {
    val item = list?.find { it.id == booking?.id }
    if (item != null) return true
    return false
}

private fun addBookingSelect(
    booking: ServiceBooking?,
    list: MutableState<MutableList<ServiceBooking>?>
) {
    val item = list.value?.find { it.id == booking?.id }
    if (item == null) {
        list.value = list.value?.map { it }?.toMutableList()?.apply {
            if (booking != null) {
                add(booking)
            }
        }
    }
}


@Composable
private fun Service(
    data: ServiceBooking?,
    serviceSelect: MutableState<MutableList<ServiceBooking>?>
) {
    val configuration = LocalConfiguration.current
    val width = ((configuration.screenWidthDp - 28) / 2).dp

    Card(
        modifier = Modifier
            .padding(bottom = 12.dp, start = 6.dp, end = 6.dp)
            .background(color = White)
            .width(width), elevation = 4.dp, shape = RoundedCornerShape(4.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable {
                addBookingSelect(data, serviceSelect)
            }) {

            Box(modifier = Modifier.size(width)) {
                RoundedAsyncImage(
                    imageUrl = ((if (data?.photos?.isNotEmpty() == true) data.photos.firstOrNull()?.url
                        ?: "" else "")),
                    cornerRadius = 4.dp,
                    size = width,
                    shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                )

                val res = if (checkBookingSelect(data, serviceSelect.value)) {
                    olmo.wellness.android.R.drawable.ic_radio_selected
                } else {
                    olmo.wellness.android.R.drawable.ic_radio_unselected
                }
                Image(
                    painter = painterResource(id = res),
                    contentDescription = "select",
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd),
                )
            }

            Text(
                text = data?.title ?: "",
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 12.sp, lineHeight = 20.sp
                ), modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .height(30.dp),
                maxLines = 2
            )

            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Neutral_Gray_9,
                            fontWeight = FontWeight.Normal,
                            fontSize = 8.sp
                        )
                    ) {
                        append("from")
                    }

                    withStyle(
                        style = SpanStyle(
                            color = Neutral_Gray_9,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp
                        )
                    ) {
                        append(" " + "VND" + " ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color_LiveStream_Main_Color,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    ) {
                        append(getConvertCurrency(data?.startingPrice ?: 0f))
                    }

                    withStyle(
                        style = SpanStyle(
                            color = Neutral_Gray_9,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    ) {
                        append("/")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Neutral_Gray_9,
                            fontWeight = FontWeight.Normal,
                            fontSize = 8.sp
                        )
                    ) {
                        append(data?.sessionType ?: "")
                    }


                },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(12.dp)
                    .noRippleClickable {
                    }
            )
        }
    }
}