package olmo.wellness.android.ui.screen.seller

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import olmo.wellness.android.R
import olmo.wellness.android.ui.screen.navigation.ScreenName
import olmo.wellness.android.ui.theme.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SellerHubScreen(
    navController: NavController
) {
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    Scaffold(
        topBar = { TopBarView(navController) }
    ) {
        Box(
            Modifier
                .padding(bottom = defaultHeightBottomBar)
                .background(White)
                .shadow(defaultShadow)
        ) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {

                },
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()) {
                }
            }
        }
    }
}

@Composable
fun TopBarView(navController: NavController) {
    Row(
        modifier = Modifier
            .background(White)
            .fillMaxWidth()
            .padding(horizontal = marginDouble, vertical = marginDouble)
            .height(defaultHeightButton),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_seller_hub),
            contentDescription = null,
            modifier = Modifier
                .width(widthSellerHubIcon)
                .height(defaultHeightButton)
        )
        Image(
            painter = painterResource(R.drawable.ic_buyer),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(sizeIcon_40)
                .clip(CircleShape)
                .border(defaultHeightLine, Color.White, CircleShape)
        )
        IconButton(modifier = Modifier.then(Modifier.size(defaultSizeImage)),
            onClick = {
                navController.navigate(ScreenName.SettingsScreen.route)
            }) {
            Icon(
                painterResource(id = R.drawable.ic_setting),
                null,
                tint = Neutral_Gray_9,
                modifier = Modifier
                    .size(defaultSizeImage)
            )
        }
    }
}
