package olmo.wellness.android.ui.screen.test_compose_screen

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import android.view.LayoutInflater
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import olmo.wellness.android.ui.common.DetailTopBar
import olmo.wellness.android.ui.theme.Color_gray_6CF
import olmo.wellness.android.ui.theme.White
import olmo.wellness.android.ui.theme.marginDouble
import olmo.wellness.android.ui.theme.zeroDimen
import java.util.*
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.inset
import androidx.core.content.ContextCompat.startActivity
import olmo.wellness.android.core.Constants
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.playback_video.contact_information.ContactInformationServiceBottomSheet
import olmo.wellness.android.ui.screen.verify_code_screen.UserLimitedOTP

val fullSpan: (LazyGridItemSpanScope.() -> GridItemSpan) = { GridItemSpan(currentLineSpan = 2) }

@SuppressLint("MutableCollectionMutableState", "UnrememberedMutableState",
    "UnusedMaterialScaffoldPaddingParameter"
)
@OptIn(
    ExperimentalPagerApi::class, androidx.compose.material.ExperimentalMaterialApi::class,
    com.google.accompanist.permissions.ExperimentalPermissionsApi::class,
    kotlinx.coroutines.ExperimentalCoroutinesApi::class
)
@Composable
fun TestComposeScreen(
    navController: NavController,
) {
    val modalListOfServiceBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = {
                true
            }
        )
    Scaffold(
        topBar = { DetailTopBar("", navController, White, zeroDimen) },
        /*bottomBar = {
            Box(modifier = Modifier.padding(paddingValues = PaddingValues(0.dp, 0.dp, 0.dp, 0.dp))){
                BottomBarNavigationCompose(modifier = Modifier.fillMaxWidth(), navController)
            }
        },*/
        /*floatingActionButton = {
            *//*FloatingActionButton(
                onClick = {
                          navController.navigate(ScreenName.VerificationStep1SuccessScreen.route)
                },
                shape = RoundedCornerShape(50),
                backgroundColor = Color(0xFFFF8C00)
            ) {
                val imageIcon = ImageVector.vectorResource(id = R.drawable.ic_center_bottom_nav)
                Icon(imageIcon,"")
            }*//*
        },
        isFloatingActionButtonDocked = false,
        floatingActionButtonPosition = FabPosition.Center,*/
        /*backgroundColor = Color.Transparent*/
    ) {
       /* val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val openDrawer = {
            scope.launch {
                drawerState.open()
            }
        }

        val linearGradientBrush = Brush.linearGradient(
            colors = listOf(
                Color(0xFF5995EE),
                Color(0xFFB226E1),
                Color(0xFFE28548)
            ),
            start = Offset(Float.POSITIVE_INFINITY, 0f),
            end = Offset(0f, Float.POSITIVE_INFINITY),
        )
        val transparentGradientBrush = Brush.linearGradient(
            colors = listOf(
                Color_gray_6CF,
                Transparent
            )
        )
        val widthDefaultSize = 150.dp
        val context = LocalContext.current
        val widthDefault = with(LocalDensity.current) { widthDefaultSize.toPx() }
        val anchors = mapOf(
            0f to 0,
            -dpToPxUtils(
                context = context,
                dpValue = widthDefault
            ) to 1, // Swipe to left , then show the right slide,
            dpToPxUtils(
                context = context,
                dpValue = widthDefault
            ) to 2 // Swipe to right , then show the left slide,
        )
        val swipeAbleState = rememberSwipeableState(initialValue = 0,
            confirmStateChange = { status ->
                Log.e("WTF", " status $status")
                when (status) {
                    1 -> {

                    }
                    2 -> {

                    }
                    0 -> {

                    }
                }
                true
            })
        val items = mutableStateListOf<String>(
            "Item Number 1",
            "Item Number 2",
            "Item Number 3",
            "Item Number 4",
            "Item Number 5",
            "Item Number 6",
            "Item Number 7",
            "Item Number 8",
        )
        LazyColumn {
            itemsIndexed(
                items = items,
                key = { index, item ->
                    item.hashCode()
                }
            ) { index, item ->
                val state = rememberDismissState(
                    confirmStateChange = {
                        if (it == DismissValue.DismissedToStart) {
                            //items.remove(item)
                        }
                        true
                    }
                )
                SwipeToDismiss(
                    state = state,
                    background = {
                        val color = when (state.dismissDirection) {
                            DismissDirection.StartToEnd -> Color.Green
                            DismissDirection.EndToStart -> Color.Red
                            null -> Color.Transparent
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }

                    },
                    dismissContent = {
                        MyCustomItem(text = item)
                    },
                    directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                    dismissThresholds = { FractionalThreshold(0.5F) },
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                swipeAbleState.offset.value.roundToInt(), 0
                            )
                        }
                        .swipeable(
                            state = swipeAbleState,
                            anchors = anchors,
                            thresholds = { _, _ ->
                                FractionalThreshold(0.3f)
                            },
                            orientation = Orientation.Horizontal
                        )
                )
            }

        }*/

        /* Card(
             modifier = Modifier
                 .fillMaxWidth()
                 .defaultMinSize(minHeight = 60.dp)
                 .blur(5.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                 .clip(RoundedCornerShape(12.dp))
                 .background(transparentGradientBrush),
             backgroundColor = Color.Transparent,
             elevation = 0.dp
         ) {
             Text("AAAA")
         }*/
        /*Box(modifier = Modifier
            .graphicsLayer {
                alpha = 0.99f
            }
            .drawWithContent {
                val colors = listOf(Color.Transparent, Color_gray_6CF)
                drawContent()
                drawRect(brush = Brush.verticalGradient(colors),
                    blendMode = BlendMode.DstIn)
            }
            .blur(radiusX = 1.dp, radiusY = 3.dp, BlurredEdgeTreatment.Unbounded)){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 60.dp)
                    .clip(RoundedCornerShape(12.dp)),
                backgroundColor = Color_gray_6CF,
                elevation = 0.dp){
                Text("AAAA")
            }
        }*/
        //
        /*SwipeCompose(color = Color.White,isShowDeleteItemDefault = true,
            isShowOptionDefault = true, content = {
                UpcomingItemCompose()
            })*/
        //NeonSample()
        ContactInformationServiceBottomSheet(modalBottomSheetState = modalListOfServiceBottomSheetState)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyCustomItem(text: String) {
    ListItem(
        text = { Text(text = text) },
        overlineText = { Text(text = "OverLine") },
        icon = { Icon(imageVector = Icons.Outlined.Share, contentDescription = null) },
        trailing = {
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowRight,
                contentDescription = null
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
    )
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
    val brush = linearGradient(
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
        elevation = 0.dp
    ) {
        ShimmerGridItem(brush = brush)
    }
}

@Composable
fun ShimmerGridItem(brush: Brush) {
    Row(
        modifier = Modifier
            .defaultMinSize(minHeight = 50.dp, minWidth = 100.dp)
            .background(brush)
            .clip(RoundedCornerShape(12.dp))
    ) {
    }
}

@Composable
@Preview(showBackground = true)
fun ShimmerGridItemPreview() {
    ShimmerGridItem(
        brush = Brush.linearGradient(
            listOf(
                Color.LightGray.copy(alpha = 0.6f),
                Color.LightGray.copy(alpha = 0.2f),
                Color.LightGray.copy(alpha = 0.6f),
            )
        )
    )
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun ShimmerGridItemDarkPreview() {
    ShimmerGridItem(
        brush = Brush.linearGradient(
            listOf(
                Color.LightGray.copy(alpha = 0.6f),
                Color.LightGray.copy(alpha = 0.2f),
                Color.LightGray.copy(alpha = 0.6f),
            )
        )
    )
}

@ExperimentalMaterialApi
@Composable
fun swipeToDismiss() {
    val dismissState = rememberDismissState(initialValue = DismissValue.Default)
    SwipeToDismiss(
        state = dismissState,
        /***  create dismiss alert Background */
        background = {
            val color = when (dismissState.dismissDirection) {
                DismissDirection.StartToEnd -> Color.Green
                DismissDirection.EndToStart -> Color.Red
                null -> Color.Transparent
            }
            val direction = dismissState.dismissDirection

            if (direction == DismissDirection.StartToEnd) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color)
                        .padding(8.dp)
                ) {
                    Column(modifier = Modifier.align(Alignment.CenterStart)) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = "Move to Archive", fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }

                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color)
                        .padding(8.dp)
                ) {
                    Column(modifier = Modifier.align(Alignment.CenterEnd)) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.heightIn(5.dp))
                        Text(
                            text = "Move to Bin",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = Color.LightGray
                        )

                    }
                }
            }
        },
        /**** Dismiss Content */
        dismissContent = {
            //UpcomingItemCompose()
        },
        /*** Set Direction to dismiss */
        directions = setOf(DismissDirection.EndToStart, DismissDirection.StartToEnd),
    )
}

@Composable
private fun NeonSample() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        val paint = remember {
            Paint().apply {
                style = PaintingStyle.Stroke
                strokeWidth = 30f
            }
        }

        val frameworkPaint = remember {
            paint.asFrameworkPaint()
        }

        val color = Color.Red

        val transparent = color
            .copy(alpha = 0f)
            .toArgb()

        frameworkPaint.color = transparent
        frameworkPaint.setShadowLayer(
            10f,
            0f,
            0f,
            color
                .copy(alpha = .5f)
                .toArgb()
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            inset(10.dp.toPx()){
                this.drawIntoCanvas {
                    it.drawRoundRect(
                        left = 0f,
                        top = 0f,
                        right = size.width,
                        bottom = size.height,
                        radiusX = 5.dp.toPx(),
                        5.dp.toPx(),
                        paint = paint
                    )
                    drawRoundRect(
                        Color.White,
                        cornerRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx()),
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            }
        }
    }
}




