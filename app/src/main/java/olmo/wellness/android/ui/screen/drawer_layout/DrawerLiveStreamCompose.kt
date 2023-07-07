package olmo.wellness.android.ui.screen.drawer_layout

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.himanshoe.kalendar.common.KalendarSelector
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.ui.Kalendar
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.ui.KalendarType
import olmo.wellness.android.R
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.KalendarStyle
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.theme.KalendarShape
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceHorizontalCompose
import olmo.wellness.android.ui.theme.Color_Green_Main
import olmo.wellness.android.ui.theme.Neutral_Gray
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DrawerLiveStreamCompose(navController: NavController ?= null, viewModel: ScheduleSlideViewModel = hiltViewModel()) {
    Scaffold {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr ) {
            Box(modifier = Modifier.fillMaxSize()){
                LazyColumn(content = {
                    item {
                        Column(Modifier.fillMaxSize()) {
                            ImageHeader()
                            SpaceCompose(height = 20.dp)
                            DateCompose()
                            SpaceCompose(height = 20.dp)
                            ScheduleCompose()
                        }
                    }
                })
            }
        }
    }
}

@Composable
fun ImageHeader() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.olmo_bg_thumnail_livestream), contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun DateCompose() {
    val style = KalendarStyle(
        kalendarBackgroundColor = Color.White,
        kalendarColor = Neutral_Gray,
        kalendarSelector = KalendarSelector.Circle(
            defaultColor = Neutral_Gray,
            selectedColor = Color_Green_Main
        ),
        hasRadius = false,
        shape = KalendarShape.DefaultRectangle
    )
    Kalendar(kalendarType = KalendarType.Oceanic(),
        kalendarStyle = style, onCurrentDayClick = { localDate, kalendarEvent, kalendarMoney ->
        },
        onBack = {
        }, onDrop = {
        })
}

@Composable
fun ScheduleCompose() {
    Column(modifier = Modifier.padding(horizontal = 16.dp), content = {
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.olmo_img_schedule_one),
                contentDescription = ""
            )
            SpaceHorizontalCompose(width = 10.dp)
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp), content = {
                Text(text = "How To Meditate")
                Row(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(text = "Wed, 19 2022. 10:30PM")
                    Image(
                        painter = painterResource(id = R.drawable.olmo_ic_noti_red),
                        contentDescription = ""
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier
                            .size(36.dp)
                            .padding(end = 10.dp),
                        painter = painterResource(id = R.drawable.olmo_ic_profile),
                        contentDescription = ""
                    )
                    Text(text = "Healing Tarot")
                }
            })
        }

        SpaceCompose(height = 10.dp)

        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.olmo_img_schedule_one),
                contentDescription = ""
            )
            SpaceHorizontalCompose(width = 10.dp)
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp), content = {
                Text(text = "How To Meditate")
                Row(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(text = "Wed, 19 2022. 10:30PM")
                    Image(
                        painter = painterResource(id = R.drawable.olmo_ic_noti_red),
                        contentDescription = ""
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier
                            .size(36.dp)
                            .padding(end = 10.dp),
                        painter = painterResource(id = R.drawable.olmo_ic_profile),
                        contentDescription = ""
                    )
                    Text(text = "Healing Tarot")
                }
            })
        }
    })
}

