package olmo.wellness.android.ui.screen.playback_video.donate.ui_main

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import olmo.wellness.android.R
import olmo.wellness.android.domain.tips.TipsPackageOptionInfo
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.empty.EmptyBottomSheet
import olmo.wellness.android.ui.theme.Color_LiveStream_Light_Color
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color
import olmo.wellness.android.ui.theme.Neutral_Gray_4
import olmo.wellness.android.ui.theme.White

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TipsAndDonateBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    viewModel: TipsAndDonateViewModel = hiltViewModel(),
    onRechargeNow: ((Boolean) -> Unit)? = null,
    onNavigationPackageOption: ((Boolean) -> Unit)? = null,
    onSendTipSuccess: ((Boolean, Int?) -> Unit)? = null,
) {

    val state = viewModel.uiState.collectAsState()
    val listTips = state.value.listOptions
    val enable = remember {
        mutableStateOf(true)
    }
    val giftSelect = remember {
        mutableStateOf<TipsPackageOptionInfo?>(null)
    }
    val balance = state.value.balance
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContent = {
            if (modalBottomSheetState.isVisible) {
                Column(modifier = Modifier.padding(vertical = 24.dp, horizontal = 12.dp)) {
                    LazyVerticalGrid(columns = GridCells.Fixed(3), content = {
                        items(listTips.size, key = {
                            it.toString()
                        }) { index ->
                            val item = listTips[index]
                            ItemTips(item = item, giftSelect, viewModel)
                        }
                    })

                    Spacer(modifier = Modifier.height(32.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = if (enable.value) {
                                    Brush.horizontalGradient(
                                        listOf(
                                            Color_LiveStream_Main_Color,
                                            Color_LiveStream_Main_Color,
                                            Color_LiveStream_Light_Color,
                                        )
                                    )
                                } else {
                                    Brush.horizontalGradient(
                                        listOf(
                                            Neutral_Gray_4,
                                            Neutral_Gray_4,
                                            Neutral_Gray_4,
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(50.dp)
                            )
                            .padding(12.dp)
                            .noRippleClickable {
                                viewModel.reCharge()
                            }
                    ) {
                        Row(modifier = Modifier.align(Alignment.CenterStart)) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_original),
                                contentDescription = "original",
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = balance.toString(),
                                style = MaterialTheme.typography.subtitle2.copy(
                                    fontSize = 16.sp, color = White, lineHeight = 24.sp
                                ),
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                        Text(
                            text = stringResource(
                                R.string.lb_recharge_now
                            ),
                            style = MaterialTheme.typography.subtitle2.copy(
                                fontSize = 16.sp, color = White, lineHeight = 24.sp
                            ), modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            } else {
                EmptyBottomSheet()
            }
        }) {

    }

    /* Handle State*/
    if (state.value.isNavigationPackage == true) {
        onNavigationPackageOption?.invoke(true)
        viewModel.clear()
    }

    if (state.value.isNavigationPaymentMethods == true) {
        onRechargeNow?.invoke(true)
        viewModel.clear()
    }

    LaunchedEffect(state.value.sendSuccessTipOption){
        snapshotFlow { state.value.sendSuccessTipOption }.take(1).collectLatest { sendSuccessTipOptionStatus ->
            if(sendSuccessTipOptionStatus != null && sendSuccessTipOptionStatus == true){
                onSendTipSuccess?.invoke(true, giftSelect.value?.id)
                viewModel.clear()
            }
        }
    }

}

@Composable
fun ItemTips(
    item: TipsPackageOptionInfo,
    selected: MutableState<TipsPackageOptionInfo?>,
    viewModel: TipsAndDonateViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                selected.value = item
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val rememberResource = painterResource(id = R.drawable.olmo_ic_group_default_place_holder)
        AsyncImage(
            model = item.image,
            error = rememberResource,
            contentDescription = item.name,
            modifier = Modifier.size(75.dp)
        )
        item.name?.let {
            Text(
                text = it,
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.subtitle2.copy(
                    fontSize = 10.sp, lineHeight = 15.sp
                )
            )
        }
        if (selected.value == null || selected.value?.name != item.name) {
            Row(modifier = Modifier.height(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_original),
                    contentDescription = "ic_kepler",
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = item.coin?.toInt().toString(),
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontSize = 12.sp, lineHeight = 18.sp
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(20.dp)
                    .background(
                        shape = RoundedCornerShape(20.dp),
                        color = Color_LiveStream_Main_Color
                    )
                    .noRippleClickable {
                        viewModel.sendAction(item)
                    }
            ) {
                Text(
                    text = stringResource(id = R.string.send),
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontSize = 8.sp, lineHeight = 12.sp, color = White
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}