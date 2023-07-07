package olmo.wellness.android.ui.booking.book_now_livestream.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.booking.BookingWrapperInfo
import olmo.wellness.android.ui.booking.book_now_livestream.cell.ItemCartBuyer
import olmo.wellness.android.ui.booking.viewmodel.BookingPlayBackViewModel
import olmo.wellness.android.ui.common.components.booking_service.ItemBookingService
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.common.empty.EmptyBottomSheet
import olmo.wellness.android.ui.livestream.schedule.component.SwipeCompose
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CartBookingBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    bookingPlayBackViewModel: BookingPlayBackViewModel = hiltViewModel(),
    confirmCallback: (BookingWrapperInfo) -> Unit,
    cancelCallback: () -> Unit,
) {

    val pageCurrent = remember {
        mutableStateOf(1)
    }
    val scope = rememberCoroutineScope()
    LaunchedEffect(modalBottomSheetState.isVisible) {
        bookingPlayBackViewModel.getUserCart(pageCurrent.value)
    }
    val uiState = bookingPlayBackViewModel.uiState.collectAsState()
    val listItemCarts = uiState.value.userCarts
    val sessionConfirmInfo = uiState.value.serviceSessionConfirmInfo
    val confirmActionState = uiState.value.confirmedAction
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetBackgroundColor = White,
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetContent = {
            if (modalBottomSheetState.isVisible) {
                Scaffold(topBar = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        val quantityCarts = " (".plus(listItemCarts?.size).plus(")")
                        Text(
                            text = if (listItemCarts?.size != null && listItemCarts.isNotEmpty()) {
                                stringResource(id = R.string.lb_my_card).plus(quantityCarts)
                            } else {
                                stringResource(id = R.string.lb_my_card)
                            },
                            style = MaterialTheme.typography.subtitle2.copy(
                                fontSize = 18.sp, lineHeight = 26.sp
                            ),
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )

                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .align(Alignment.BottomCenter)
                                .background(Gray_EF3)
                        )
                    }
                }, modifier = Modifier.fillMaxHeight(0.8f), bottomBar = {
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
                                    cancelCallback.invoke()

                                }
                            )
                            PrimaryLiveButton(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 6.dp),
                                stringResource(id = R.string.lb_confirm),
                                onClickFunc = {
                                    bookingPlayBackViewModel.createServiceSessionConfirm()
                                },
                                enable = listItemCarts?.isNotEmpty() == true
                            )
                        }
                    }
                }) {
                    val lazyListState = rememberLazyListState()
                    if (listItemCarts?.isNotEmpty() == true) {
                        LazyColumn(content = {
                            items(items = listItemCarts,
                                key = { item -> item.id ?: 0 },
                                itemContent = { cart ->


                                    SwipeCompose(
                                        color = White,
                                        isShowDeleteItemDefault = true,
                                        isShowOptionDefault = false,
                                        disableSwipeLeftToRight = true,
                                        content = {
                                            ItemCartBuyer(cart, onSelected = {
                                                bookingPlayBackViewModel.updateListCart(it)
                                            })
                                        },
                                        callbackDeleteItemUpcoming = {
                                            cart.id?.let { it1 ->
                                                bookingPlayBackViewModel.deleteUserCart(it1)
                                            }
                                        },
                                    )


                                })

                            item {
                                Spacer(modifier = Modifier.height(200.dp))
                            }
                        }, state = lazyListState)
                    }
                }
            } else {
                EmptyBottomSheet()
                bookingPlayBackViewModel.dismissDialog()
            }
        }) {
    }

    if (sessionConfirmInfo != null && confirmActionState == true) {
        scope.launch {
            val bookingWrapperInfo = BookingWrapperInfo(
                totalMoney = uiState.value.serviceSessionConfirmInfo?.totalPrice ?: 0F,
                serviceSessionConfirmId = uiState.value.serviceSessionConfirmInfo?.id ?: 0.0,
                bookingId = uiState.value.bookingIdResponseInfo?.bookingId
            )
            confirmCallback.invoke(bookingWrapperInfo)
            modalBottomSheetState.hide()
            bookingPlayBackViewModel.clearConfirmAction()
        }
    }
}