package olmo.wellness.android.ui.screen.playback_video.donate.view

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.extension.clearFocusOnKeyboardDismiss
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.screen.account_setting.NavigationItem
import olmo.wellness.android.ui.screen.playback_video.donate.viewmodel.KeplerBalanceViewModel
import olmo.wellness.android.ui.theme.*
import olmo.wellness.android.util.getConvertCurrency

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun KeplerBalanceSellerScreen(
    navController: NavController,
    viewModel: KeplerBalanceViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState.collectAsState()


    val modalKeplerBalanceBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    val modalKeplerTransactionBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )

    val scope = rememberCoroutineScope()

    val inputValue = remember {
        mutableStateOf<String?>(null)
    }

    val focusManager = LocalFocusManager.current

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color_LiveStream_Main_Color)
    ) {
        val (options, navigationItem, withdraw) = createRefs()
        Column(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 32.dp,
                        topEnd = 32.dp
                    )
                )
                .background(color = Color_gray_FF7)
                .fillMaxSize()
                .fillMaxHeight()
                .constrainAs(options) {
                    start.linkTo(parent.start)
                    top.linkTo(navigationItem.top, 36.dp)
                }
                .padding(top = 50.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.lb_my_balance),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.subtitle2.copy(
                    fontSize = 16.sp, color = Color_LiveStream_Main_Color, lineHeight = 24.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .background(
                        color = Color_Green_Main.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 28.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_kepler_circle_while),
                    contentDescription = "Kepler", modifier = Modifier
                        .padding(end = 12.dp)
                        .size(30.dp)
                )

                Text(
                    text = uiState.value.myBalance?.toInt()?.toString() ?: "0",
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontSize = 40.sp, lineHeight = 24.sp, color = Color_Green_Main
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.lb_my_reward),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.subtitle2.copy(
                    fontSize = 16.sp, color = Color_LiveStream_Main_Color, lineHeight = 24.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Total", style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 16.sp, lineHeight = 24.sp
                )
            )

            Spacer(modifier = Modifier.height(24.dp))


            Row(
                modifier = Modifier
                    .background(
                        color = Color_Green_Main.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 28.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                fontFamily = MontserratFont,
                                color = Neutral_Gray_9
                            )
                        ) {
                            append("đ ")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                fontFamily = MontserratFont,
                                color = Color_LiveStream_Main_Color
                            )
                        ) {
                            append(getConvertCurrency(uiState.value.myReward?:0F))
                        }
                    }
                )


            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.lb_input_value),
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = inputValue.value ?: "",
                singleLine = true,
                onValueChange = {
                    inputValue.value = it
                },
                placeholder = {
                    androidx.compose.material.Text(
                        text = stringResource(R.string.input_value),
                        style = MaterialTheme.typography.subtitle1.copy(
                            color = Neutral_Gray_5, fontSize = 14.sp
                        ),
                    )
                },
                modifier = Modifier
                    .padding()
                    .clearFocusOnKeyboardDismiss(onKeyboardDismiss = {
                        focusManager.clearFocus()
                    })
                    .border(
                        BorderStroke(width = 1.dp, color = Neutral_Gray_4),
                        shape = RoundedCornerShape(50)
                    )
                    .fillMaxWidth()
                    .height(50.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = White,
                    textColor = Color(0xFF303037),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                textStyle = MaterialTheme.typography.subtitle1.copy(fontSize = 14.sp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    autoCorrect = true,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable {
                        scope.launch {
                            modalKeplerBalanceBottomSheetState.show()
                        }
                    },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = stringResource(R.string.lb_bank_account),
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontSize = 14.sp, lineHeight = 22.sp
                    ),
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = "next",
                    tint = Black_037
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Notice:    - Minimum amount: đ 2,000,000/day\n                - Maximum amount: đ 50,000,000/day",
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 10.sp, lineHeight = 16.sp, color = Neutral_Gray_5
                ),
                modifier = Modifier.fillMaxWidth()
            )

        }
        NavigationItem(
            modifier = Modifier
                .constrainAs(navigationItem) {
                    top.linkTo(parent.top, 20.dp)
                    start.linkTo(parent.start, 44.dp)
                }
                .noRippleClickable {
                    navController.popBackStack()
                }
        )

        SecondLiveButton(
            modifier = Modifier
                .padding(bottom = 24.dp, start = 16.dp, end = 16.dp)
                .constrainAs(withdraw) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                },
            stringResource(R.string.lb_withdraw_now),
            onClickFunc = {
                scope.launch {
                    modalKeplerTransactionBottomSheetState.show()
                }
            }
        )

    }

    BankAccountBottomSheet(
        modalBottomSheetState = modalKeplerBalanceBottomSheetState,
        listBankInfos = uiState.value.listBankInfo
    )


    ConfirmWithdrawBottomSheet(modalBottomSheetState = modalKeplerTransactionBottomSheetState)


}