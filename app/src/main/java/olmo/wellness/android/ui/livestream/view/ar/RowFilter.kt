package olmo.wellness.android.ui.livestream.view.ar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.pager.PagerCarousel
import olmo.wellness.android.ui.common.pager.PagerStateCarousel
import olmo.wellness.android.ui.theme.White


@OptIn(ExperimentalPagerApi::class)
@Composable
fun PreparePager(
    pagerState: PagerStateCarousel,
    items: MutableList<FilterAR>,
    selectedPage: MutableState<Int>
) {
    Column(Modifier.padding(horizontal = 50.dp)) {
        PagerCarousel(
            state = pagerState,
            modifier = Modifier.height(80.dp)) {
            val item = items[commingPage]
            selectedPage.value = pagerState.currentPage
            CarouselItem(item, (commingPage == selectedPage.value))
        }
    }
}

@Composable
fun CarouselItem(item: FilterAR, selected: Boolean) {
    val sizeImage = (if (selected) 60.dp else 30.dp)
    val modifier = (if (selected) Modifier.border(
        width = 2.dp,
        color = White,
        shape = CircleShape
    ) else Modifier)
    Column(
        modifier = Modifier.padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = item.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.caption.copy(color = Color.White, fontSize = 10.sp),
            modifier = Modifier.width(sizeImage)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Image(
            painter = painterResource(id = item.image),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = modifier
                .clip(CircleShape)
                .size(sizeImage)
        )
    }
}
