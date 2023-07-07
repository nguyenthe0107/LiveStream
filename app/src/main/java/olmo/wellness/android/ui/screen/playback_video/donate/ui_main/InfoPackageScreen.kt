package olmo.wellness.android.ui.screen.playback_video.donate.ui_main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.screen.account_setting.NavigationItem
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun InfoPackageScreen(navController: NavController) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color_LiveStream_Main_Color)){
        val (options, navigationItem) = createRefs()
        Box(
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
                .padding(top = 50.dp)
        ) {
            ListOptionItems(
                navController
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
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ListOptionItems(
    navController: NavController
){
    val listOptionSectionMyAccount: List<String> = listOf(
        stringResource(id = R.string.content_package_1),
        stringResource(id = R.string.content_package_2),
        stringResource(id = R.string.content_package_3),
        stringResource(id = R.string.content_package_4),
        stringResource(id = R.string.content_package_5),
    )
    LazyColumn {
        items(listOptionSectionMyAccount.size) { optionItemIndex ->
            ItemOption(listOptionSectionMyAccount[optionItemIndex], callBackFunc = {
                navController.navigate(ScreenName.InfoPackageDetailScreen.route)
            })
        }
    }
}

@Composable
private fun ItemOption(content: String, callBackFunc: (() -> Unit)?=null){
    Column(modifier = Modifier) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp
                ).noRippleClickable {
                    callBackFunc?.invoke()
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = content,
                style = MaterialTheme.typography.subtitle2.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                ),
                color = Black_037,
                modifier = Modifier.padding(end = 16.dp),
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right_short),
                contentDescription = null,
                tint = Black_037,
                modifier = Modifier
                    .padding(
                        end = 16.dp
                    )
            )
        }

        Box(modifier = Modifier
            .height(16.dp)
        )
    }
}