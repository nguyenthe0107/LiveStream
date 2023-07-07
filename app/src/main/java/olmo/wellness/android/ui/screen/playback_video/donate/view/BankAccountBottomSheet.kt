package olmo.wellness.android.ui.screen.playback_video.donate.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.domain.model.bank.BankInfo
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.empty.EmptyData
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BankAccountBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    listBankInfos: List<BankInfo>?,
) {
    val lazyListState = rememberLazyListState()

    val bankSelect = remember {
        mutableStateOf<BankInfo?>(null)
    }

    if (listBankInfos != null && listBankInfos.isNotEmpty()) {
        bankSelect.value = listBankInfos[0]
    }

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContent = {
            Column(
                modifier = Modifier
                    .padding(vertical = 24.dp, horizontal = 20.dp)
                    .fillMaxHeight(0.7f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                LazyColumn(state = lazyListState, modifier = Modifier.weight(1f)) {
                    listBankInfos?.forEach {
                        item {
                            ItemBanker(item = it, bankSelect)
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }

                if (listBankInfos == null || listBankInfos.isEmpty()) {
                    EmptyData()
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color_LiveStream_Main_Color,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .padding(12.dp)
                        .clickable {
                        }
                ) {
                    Text(
                        text = stringResource(R.string.lb_select),
                        style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 16.sp, color = White, lineHeight = 24.sp
                        ), modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

        }) {
    }

}

@Composable
fun ItemBanker(item: BankInfo, bankSelect: MutableState<BankInfo?>) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .noRippleClickable {
            bankSelect.value = item
        }, horizontalArrangement = Arrangement.SpaceBetween) {
        val icon =
            (if (bankSelect.value?.id == item.id) R.drawable.ic_radio_selected else R.drawable.ic_radio_unselected)

        Image(
            painter = painterResource(id = icon),
            contentDescription = "icon-select",
            modifier = Modifier.size(24.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 24.dp)
        ) {

            Text(
                text = (item.bankName ?: "").uppercase(),
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 14.sp, lineHeight = 24.sp
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Normal,
                            fontFamily = MontserratFont,
                            color = Neutral_Gray_5,
                            fontSize = 14.sp
                        )
                    ) {
                        append("Branch: ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            fontFamily = MontserratFont,
                        )
                    ) {
                        append(item.bankBranchName ?: "")
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(

                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Normal,
                            fontFamily = MontserratFont,
                            color = Neutral_Gray_5,
                            fontSize = 14.sp
                        )
                    ) {
                        append("Country: ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            fontFamily = MontserratFont,
                        )
                    ) {
                        append(item.countryName ?: "")
                    }
                })

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Normal,
                            fontFamily = MontserratFont,
                            color = Neutral_Gray_5,
                            fontSize = 14.sp
                        )
                    ) {
                        append("Name: ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            fontFamily = MontserratFont,
                        )
                    ) {
                        append(item.accountName ?: "")
                    }
                })

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Normal,
                            fontFamily = MontserratFont,
                            color = Neutral_Gray_5,
                            fontSize = 14.sp
                        )
                    ) {
                        append("Account: ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            fontFamily = MontserratFont,
                        )
                    ) {
                        append(item.accountNumber ?: "")
                    }
                })

        }
    }
}