package olmo.wellness.android.ui.screen.playback_video.donate.package_options

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import olmo.wellness.android.R
import olmo.wellness.android.domain.tips.PackageOptionInfo
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.empty.EmptyBottomSheet
import olmo.wellness.android.ui.theme.*
import olmo.wellness.android.util.getConvertCurrency

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PackageOptionsBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    viewModel: PackageOptionViewModel = hiltViewModel(),
    onRechargeWithItem: ((PackageOptionInfo?) -> Unit)? = null,
    onInfo: () -> Unit
) {
    val state = viewModel.uiState.collectAsState()
    val listOptions = state.value.listOptions
    val balance = state.value.balance
    val itemSelected = state.value.tipOptionSelected
    val enable = remember {
        mutableStateOf(true)
    }
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContent = {
            if (modalBottomSheetState.isVisible) {
                Column(modifier = Modifier.padding(vertical = 24.dp, horizontal = 20.dp)) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.lb_package_options),
                            style = MaterialTheme.typography.subtitle2.copy(
                                fontSize = 18.sp, lineHeight = 26.sp
                            ), modifier = Modifier.align(Alignment.Center)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_info_package),
                            contentDescription = "Info",
                            tint = Color_Purple_FBC,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 8.dp)
                                .noRippleClickable {
                                    onInfo.invoke()
                                }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.padding(start = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.lb_balance) + ":",
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontSize = 14.sp, lineHeight = 26.sp
                            ), modifier = Modifier.padding(end = 4.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_original),
                            contentDescription = "kepler",
                            modifier = Modifier
                                .size(16.dp)
                        )

                        Text(
                            text = balance.toString(),
                            style = MaterialTheme.typography.subtitle2.copy(
                                fontSize = 14.sp, lineHeight = 26.sp
                            ),
                            modifier = Modifier.padding(start = 2.dp)
                        )

                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    LazyVerticalGrid(columns = GridCells.Fixed(3), content = {
                        items(listOptions.size, key = {
                            it.toString()
                        }) { index ->
                            val item = listOptions[index]
                            ItemPackage(item = item, viewModel)
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
                                viewModel.onRecharge()
                            }
                    ) {
                        Text(
                            text = stringResource(R.string.lb_recharge),
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
    val isNavigationPaymentMethod = state.value.isNavigationPaymentMethods
    if (isNavigationPaymentMethod == true) {
        if (itemSelected != null) {
            onRechargeWithItem?.invoke(itemSelected)
            viewModel.clearCurrentState()
        }
    }
}

@Composable
fun ItemPackage(item: PackageOptionInfo, viewModel: PackageOptionViewModel) {
    val isSelected = remember {
        mutableStateOf(false)
    }
    val state = viewModel.uiState.collectAsState()
    isSelected.value = state.value.tipOptionSelected?.id == item.id
    val shadowColor = if (isSelected.value) Color_Purple_7F4 else Color.White
    val border = BorderStroke(
        width = if (isSelected.value) 0.dp else 1.dp,
        color = if (isSelected.value) {
            White
        } else Color_Purple_FBC
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .border(
                border = border, shape = RoundedCornerShape(10.dp)
            )
            .background(color = shadowColor, shape = RoundedCornerShape(10.dp))
            .noRippleClickable {
                viewModel.selectedTipOption(item)
            }
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_original),
                    contentDescription = "kepler",
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = item.coin?.toInt().toString(),
                    maxLines = 1,
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontSize = 14.sp, lineHeight = 21.sp, color = Color_Purple_FBC
                    )
                )
            }
            Text(
                text = "đ ${getConvertCurrency(item.curencyVN)}",
                maxLines = 1,
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 10.sp, lineHeight = 18.sp
                )
            )
        }
    }
}
