package olmo.wellness.android.ui.screen.playback_video.donate.introduce

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import olmo.wellness.android.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun InfoPackageDetailScreen(navController: NavController) {
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
        stringResource(id = R.string.content_package_detail)
    )
    LazyColumn {
        item {
            ItemOption(listOptionSectionMyAccount)
        }
    }
}

@Composable
private fun ItemOption(content: List<String>){
    Column() {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = content[0],
                style = MaterialTheme.typography.subtitle2.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                ),
                color = Color.Black,
                modifier = Modifier.padding(end = 16.dp),
            )
        }

        Box(modifier = Modifier
            .height(16.dp)
        )
        Text(
            text = content[1],
            style = MaterialTheme.typography.subtitle2.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            ),
            color = Neutral_Gray_6,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}