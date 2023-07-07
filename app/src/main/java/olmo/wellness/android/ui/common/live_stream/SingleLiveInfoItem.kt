package olmo.wellness.android.ui.common.live_stream

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.FixedScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import olmo.wellness.android.ui.common.RoundedAsyncImage
import olmo.wellness.android.R
import olmo.wellness.android.core.formatToK
import olmo.wellness.android.data.model.live_stream.SectionType
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.helpers.DateTimeHelper.ddMMMyy
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SingleLiveInfoItem(
    modifier: Modifier = Modifier,
    liveStreamInfo: LiveSteamShortInfo,
    sectionType: SectionType,
    onItemClick: (LiveSteamShortInfo) -> Unit,
    onItemLongClick: ((LiveSteamShortInfo) -> Unit) ?= null,
    isDisableLiveBadge: Boolean ?= false,
    contentScale: ContentScale = ContentScale.Crop,
    ) {
    val configuration = LocalConfiguration.current
    val screenWidth = (configuration.screenWidthDp-30)/2
    val height = screenWidth*1.0*7/6.5

    ConstraintLayout(
        modifier = modifier
            .wrapContentSize()
            .combinedClickable(
                onClick = {
                    onItemClick.invoke(liveStreamInfo)
                },
                onLongClick = {
                    onItemLongClick?.invoke(liveStreamInfo)
                }
            )
    ){
        val (liveBadge, liveImage, bottomBox, liveNoti) = createRefs()
        RoundedAsyncImage(
            imageUrl = (liveStreamInfo.thumbnailUrl) ?: "",
            cornerRadius = 20.dp,
            size = 20.dp,
            contentScale = contentScale,
            modifier = Modifier
                .width(screenWidth.dp)
                .height(height.dp)
                .constrainAs(liveImage) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        BottomBox(
            liveStreamInfo,
            sectionType = sectionType,
            modifier = Modifier
                .constrainAs(bottomBox) {
                    top.linkTo(liveImage.bottom)
                    bottom.linkTo(liveImage.bottom)
                    end.linkTo(liveImage.end)
                    start.linkTo(liveImage.start)
                },
            screenWidth= screenWidth,
        )

        when(sectionType){
            SectionType.LIVE_NOW ->{
                if(isDisableLiveBadge == null || isDisableLiveBadge == false){
                    LiveBadge(
                        modifier = Modifier
                            .constrainAs(liveBadge) {
                                start.linkTo(parent.start, 12.dp)
                                top.linkTo(parent.top, 12.dp)
                            },
                        liveStreamInfo = liveStreamInfo
                    )
                }
            }
            SectionType.UPCOMING ->{
                LiveNotification(
                    modifier = Modifier
                        .constrainAs(liveNoti) {
                            end.linkTo(parent.end, 12.dp)
                            top.linkTo(parent.top, 12.dp)
                        }
                ){

                }
            }
        }
    }
}

@Composable
fun LiveNotification(
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(20.dp)
            .background(
                Color.Black.copy(
                    alpha = 0.2f
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable {
                onClick.invoke()
            }
    ){
        Icon(
            painter = painterResource(id = R.drawable.ic_bell_plus),
            contentDescription = null,
            tint = White,
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun LiveBadge(modifier: Modifier, hideIcon: Boolean ?= null,
              liveStreamInfo : LiveSteamShortInfo ?= null) {
    Box(
        modifier = modifier
            .background(
                color = Color_RED_F65,
                shape = RoundedCornerShape(20.dp)
            )
            .wrapContentSize()){
        val shimmerColors = listOf(
            RED_F65.copy(alpha = 0.6f),
            White,
            White,
        )
        val transition = remember { Animatable(0f) }
        LaunchedEffect(transition){
            withContext(Dispatchers.IO){
                transition.animateTo(
                    targetValue = 1000f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = 800,
                            delayMillis = 300,
                            easing = FastOutSlowInEasing
                        ),
                        repeatMode = RepeatMode.Reverse
                    )
                )
            }
        }
        val brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = transition.value, y = transition.value)
        )
        Canvas(
            modifier = Modifier
                .size(width = 60.dp, height = 20.dp)
                .border(width = 1.2.dp, brush = brush, shape = RoundedCornerShape(18.dp))){
            drawRect(color = Color.Transparent)
        }
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            if (hideIcon == null) {
                RoundedAsyncImage(
                    imageUrl = liveStreamInfo?.user?.avatar ?: "https://m.economictimes.com/thumb/msid-92110083,width-1200,height-900,resizemode-4,imgsize-79822/yoga.jpg",
                    cornerRadius = 18.dp,
                    size = 18.dp,
                )
            }
            Box(modifier = Modifier.width(6.dp))
            Text(
                text = "Live",
                style = MaterialTheme.typography.overline.copy(
                    fontSize = 12.sp,
                    color = White,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center),
                modifier = Modifier.padding(bottom = 0.5.dp)
            )
            Box(modifier = Modifier.width(6.dp))
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AnimatedShimmer() {
    val shimmerColors = listOf(
        Color_gray_6CF.copy(alpha = 0.6f),
        Color.Red.copy(alpha = 0.2f),
        Color_gray_6CF.copy(alpha = 0.6f),
    )
    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 800,
                delayMillis = 300,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = 0f, y = 0f),
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(marginDouble)
            .defaultMinSize(minHeight = 60.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(brush),
        elevation = 0.dp){
        ShimmerGridItem(brush = brush)
    }
}

@Composable
fun ShimmerGridItem(brush: Brush) {
    Row(
        modifier = Modifier
            .defaultMinSize(minHeight = 50.dp, minWidth = 100.dp)
            .background(brush)
            .clip(RoundedCornerShape(12.dp))){
    }
}

@Composable
fun BottomBox(
    liveStreamInfo: LiveSteamShortInfo,
    sectionType: SectionType,
    screenWidth : Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width((screenWidth - 20).dp)
            .height(66.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = 11.dp
    ){
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 8.dp,
                    vertical = 6.dp
                )
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (sectionType!= SectionType.LIVE_NOW) {
                    RoundedAsyncImage(
                        imageUrl = liveStreamInfo.user?.avatar ?: "",
                        cornerRadius = 20.dp,
                        size = 20.dp,
                    )
                }
                Text(
                    text = liveStreamInfo.title ?: "",
                    style = MaterialTheme.typography.caption.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(
                            start = 4.dp
                        ),
                    maxLines = 1
                )
            }
            Box(modifier = Modifier.height(6.dp))
            Row  {
                if (sectionType == SectionType.UPCOMING){
                    OptionalInfo(
                        modifier = Modifier.weight(1f),
                        painterResource(id = R.drawable.ic_calendar),
                        liveStreamInfo.createdAt?.let { DateTimeHelper.convertToStringDate(it, ddMMMyy) }
                            ?: ""
                    )
                    OptionalInfo(
                        modifier = Modifier.weight(1f),
                        painterResource(id = R.drawable.ic_clock),
                        DateTimeHelper.convertToStringHour(liveStreamInfo?.createdAt)
                    )
                }
                else {
                    OptionalInfo(
                        modifier = Modifier.weight(1f),
                        painterResource(id = R.drawable.ic_eye_home),
                        liveStreamInfo.viewCount?.formatToK()?:"0"
                    )
                    OptionalInfo(
                        modifier = Modifier.weight(1f),
                        painterResource(id = R.drawable.ic_heart_home),
                        liveStreamInfo.heartCount?.formatToK()?:"0"
                    )
                    OptionalInfo(
                        modifier = Modifier.weight(1f),
                        painterResource(id = R.drawable.ic_comments),
                        liveStreamInfo.roomChat?.totalMessage?.formatToK()?:"0"
                    )
                }
            }
        }
    }
}

@Composable
fun OptionalInfo(
    modifier: Modifier = Modifier,
    icon: Painter,
    text: String
){
    Box(modifier = modifier){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Icon(
                painter = icon,
                contentDescription = "",
                tint = Black_466,
                modifier = Modifier
                    .size(12.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.overline.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}