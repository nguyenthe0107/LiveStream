package olmo.wellness.android.ui.screen.for_you.cell

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color
import olmo.wellness.android.ui.theme.Neutral_Bare_Gray
import olmo.wellness.android.ui.theme.White

data class TabRowStyle(
    var backgroundColor: Color = White,
    var colorSelect : Color = Color_LiveStream_Main_Color,
    var colorUnSelect : Color = Neutral_Bare_Gray
)


@Composable
fun CustomScrollableTabRow(
    tabs: List<String?>,
    selectedTabIndex: Int,
    style: TabRowStyle,
    onTabClick: (Int) -> Unit,
    customColorText: Color?=null
) {

    val density = LocalDensity.current
    val tabWidths = remember {
        val tabWidthStateList = mutableStateListOf<Dp>()
        repeat(tabs.size) {
            tabWidthStateList.add(0.dp)
        }
        tabWidthStateList
    }
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        contentColor = Color.White,
        edgePadding = 0.dp,
        backgroundColor = style.backgroundColor,
        indicator = { tabPositions ->
            if(tabPositions.isNotEmpty()){
                TabRowDefaults.Indicator(
                    modifier = Modifier.customTabIndicatorOffset(
                        currentTabPosition = tabPositions[selectedTabIndex],
                        tabWidth = tabWidths[selectedTabIndex]
                    ).clip(RoundedCornerShape(100.dp)),
                    color = style.colorSelect
                )
            }
        }
    ) {
        tabs.forEachIndexed { tabIndex, tab ->
            Tab(
                selected = selectedTabIndex == tabIndex,
                onClick = { onTabClick(tabIndex) },
                text = {
                    Text(
                        text = tab?:"",
                        onTextLayout = { textLayoutResult ->
                            tabWidths[tabIndex] =
                                with(density) { textLayoutResult.size.width.toDp() }
                        },
                        style = MaterialTheme.typography.subtitle2.copy(
                            fontSize = 16.sp,
                            color = customColorText ?: White
                        )
                    )
                },
                selectedContentColor = style.colorSelect,
                unselectedContentColor = style.colorUnSelect
            )
        }
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
private fun Modifier.customTabIndicatorOffset(
    currentTabPosition:
    TabPosition,
    tabWidth: Dp
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "customTabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val currentTabWidth by animateDpAsState(
        targetValue = tabWidth,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    val indicatorOffset by animateDpAsState(
        targetValue = ((currentTabPosition.left + currentTabPosition.right - tabWidth) / 2),
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    fillMaxWidth()
        .padding(bottom = 2.dp)
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
}