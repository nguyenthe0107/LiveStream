package olmo.wellness.android.ui.screen.playback_video.donate.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import olmo.wellness.android.R
import olmo.wellness.android.domain.tips.PackageOptionInfo
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.screen.account_setting.NavigationItem
import olmo.wellness.android.ui.screen.playback_video.donate.viewmodel.KeplerBalanceViewModel
import olmo.wellness.android.ui.screen.signup_screen.utils.LoaderWithAnimation
import olmo.wellness.android.ui.theme.*
import olmo.wellness.android.util.getConvertCurrency

@Composable
fun KeplerBalanceBuyerScreen(
    navController: NavController, viewModel: KeplerBalanceViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    val listOption = uiState.value.listOptions

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color_LiveStream_Main_Color)
    ) {
        val (options, navigationItem, withdraw) = createRefs()

        LazyColumn(modifier = Modifier
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
            horizontalAlignment = Alignment.CenterHorizontally) {
            item {
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
                        text = uiState.value.myBalance?.toInt()?.toString()?:"0", style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 40.sp, lineHeight = 24.sp, color = Color_Green_Main
                        )
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Buy Keplers", style = MaterialTheme.typography.subtitle1.copy(
                        fontSize = 16.sp, lineHeight = 24.sp,
                        color = Neutral_Gray_7
                    ), modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

            }

            items(count = listOption.size, key = {
                it.toString()
            }) { index ->
                val item = listOption[index]
                ItemBuyKepler(item)
                Spacer(modifier = Modifier.height(12.dp))
            }
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
        LoaderWithAnimation(isPlaying = uiState.value.showLoading)

    }
}

@Composable
private fun ItemBuyKepler(data: PackageOptionInfo) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_kepler_circle_while),
            contentDescription = "ic_kepler"
        )


        Text(
            text = "16",
            style = MaterialTheme.typography.subtitle2.copy(
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = Color_LiveStream_Main_Color
            ),
            modifier = Modifier.padding(horizontal = 9.dp)
        )


        Text(
            text = "Keplers",
            style = MaterialTheme.typography.subtitle1.copy(fontSize = 12.sp, lineHeight = 24.sp),
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color_LiveStream_Main_Color,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 44.dp, vertical = 8.dp)
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Medium,
                            fontFamily = MontserratFont,
                            color = Black,
                            fontSize = 12.sp

                        )
                    ) {
                        append("đ ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            fontFamily = MontserratFont,
                            color = Black
                        )
                    ) {
                        append(getConvertCurrency(3000000F))
                    }
                }
            )
        }
    }
}