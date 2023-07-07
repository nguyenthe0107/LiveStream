package olmo.wellness.android.ui.common.picker.time.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import olmo.wellness.android.ui.common.picker.time.type.PickerSelector
import olmo.wellness.android.ui.common.picker.time.type.PickerShape
import olmo.wellness.android.ui.common.picker.time.type.PositionItem
import kotlin.math.abs
import kotlin.math.roundToInt

private fun <T> getItemIndexForOffset(
    range: List<T>,
    value: T,
    offset: Float,
    halfNumbersColumnHeightPx: Float
): Int {
    val indexOf = range.indexOf(value) - (offset / halfNumbersColumnHeightPx).toInt()
    return maxOf(0, minOf(indexOf, range.count() - 1))
}

@Composable
fun <T> ListItemPicker(
    modifier: Modifier = Modifier,
    label: (T) -> String = { it.toString() },
    value: T,
    onValueChange: (T) -> Unit,
    pickerSelector: PickerSelector,
    pickerShape: PickerShape,
    list: List<T>,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    val minimumAlpha = 0.3f
    val verticalMargin = pickerShape.padding
    val numbersColumnHeight = 80.dp
    val halfNumbersColumnHeight = numbersColumnHeight / 2
    val halfNumbersColumnHeightPx = with(LocalDensity.current) { halfNumbersColumnHeight.toPx() }

    val coroutineScope = rememberCoroutineScope()

    val animatedOffset = remember { Animatable(0f) }
        .apply {
            val index = list.indexOf(value)
            val offsetRange = remember(value, list) {
                -((list.count() - 1) - index) * halfNumbersColumnHeightPx to
                        index * halfNumbersColumnHeightPx
            }
            updateBounds(offsetRange.first, offsetRange.second)
        }

    val coercedAnimatedOffset = animatedOffset.value % halfNumbersColumnHeightPx

    val indexOfElement =
        getItemIndexForOffset(list, value, animatedOffset.value, halfNumbersColumnHeightPx)

    var dividersWidth by remember { mutableStateOf(0.dp) }

    Layout(
        modifier = modifier
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { deltaY ->
                    coroutineScope.launch {
                        animatedOffset.snapTo(animatedOffset.value + deltaY)
                    }
                },
                onDragStopped = { velocity ->
                    coroutineScope.launch {
                        val endValue = animatedOffset.fling(
                            initialVelocity = velocity,
                            animationSpec = exponentialDecay(frictionMultiplier = 20f),
                            adjustTarget = { target ->
                                val coercedTarget = target % halfNumbersColumnHeightPx
                                val coercedAnchors =
                                    listOf(
                                        -halfNumbersColumnHeightPx,
                                        0f,
                                        halfNumbersColumnHeightPx
                                    )
                                val coercedPoint =
                                    coercedAnchors.minByOrNull { abs(it - coercedTarget) }!!
                                val base =
                                    halfNumbersColumnHeightPx * (target / halfNumbersColumnHeightPx).toInt()
                                coercedPoint + base
                            }
                        ).endState.value

                        val result = list.elementAt(
                            getItemIndexForOffset(list, value, endValue, halfNumbersColumnHeightPx)
                        )
                        onValueChange(result)
                        animatedOffset.snapTo(0f)
                    }
                }
            )
            .padding(vertical = numbersColumnHeight / 3 + verticalMargin * 2),
        content = {
            Box(
                modifier = Modifier
                    .background(color = pickerSelector.backgroundColor, shape = getShape(pickerShape.radius,pickerShape.position))
                    .padding(vertical = verticalMargin)
                    .offset { IntOffset(x = 0, y = coercedAnimatedOffset.roundToInt()) },
            ) {
                val baseLabelModifier = Modifier.align(Alignment.Center)
                ProvideTextStyle(textStyle) {
                    if (indexOfElement > 0)
                        Label(
                            text = label(list.elementAt(indexOfElement - 1)),
                            modifier = baseLabelModifier
                                .offset(y = -halfNumbersColumnHeight)
                                .alpha(maxOf(minimumAlpha, coercedAnimatedOffset / halfNumbersColumnHeightPx)),
                            textColor = pickerSelector.textNormal
                        )
                    Label(
                        text = label(list.elementAt(indexOfElement)),
                        modifier = baseLabelModifier
                            .alpha((maxOf(minimumAlpha, 1 - abs(coercedAnimatedOffset) / halfNumbersColumnHeightPx))),
                        textColor = pickerSelector.textSelectColor
                    )
                    if (indexOfElement < list.count() - 1)
                        Label(
                            text = label(list.elementAt(indexOfElement + 1)),
                            modifier = baseLabelModifier
                                .offset(y = halfNumbersColumnHeight)
                                .alpha(maxOf(minimumAlpha, -coercedAnimatedOffset / halfNumbersColumnHeightPx)),
                            textColor = pickerSelector.textNormal
                        )
                }
            }
            Box(
                modifier
                    .width(dividersWidth)
                    .height(0.dp)
                    .background(color = Color.Transparent)
            )
        }
    ) { measurables, constraints ->
        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.map { measurable ->
            // Measure each children
            measurable.measure(constraints)
        }

        dividersWidth = placeables
            .drop(1)
            .first()
            .width
            .toDp()

        // Set the size of the layout as big as it can
        layout(dividersWidth.toPx().toInt(), placeables
            .sumOf {
                it.height
            }
        ) {
            // Track the y co-ord we have placed children up to
            var yPosition = 0

            // Place children in the parent layout
            placeables.forEach { placeable ->

                // Position item on the screen
                placeable.placeRelative(x = 0, y = yPosition)

                // Record the y co-ord placed up to
                yPosition += placeable.height
            }
        }
    }
}

private fun getShape(radius : Dp, positionItem: PositionItem) : Shape{
  return  when(positionItem){
        PositionItem.Left->{
            RoundedCornerShape(topStart = radius, topEnd = 0.dp, bottomEnd = 0.dp, bottomStart = radius)
        }
        PositionItem.Right->{
            RoundedCornerShape(topStart = 0.dp, topEnd = radius, bottomEnd = radius, bottomStart = 0.dp)

        }else ->{
            RectangleShape
        }
    }
}

@Composable
private fun Label(text: String, modifier: Modifier, textColor: Color) {
    Text(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(onLongPress = {
            })
        },
        color = textColor,
        text = text,
        maxLines = 1,
    )
}

private suspend fun Animatable<Float, AnimationVector1D>.fling(
    initialVelocity: Float,
    animationSpec: DecayAnimationSpec<Float>,
    adjustTarget: ((Float) -> Float)?,
    block: (Animatable<Float, AnimationVector1D>.() -> Unit)? = null,
): AnimationResult<Float, AnimationVector1D> {
    val targetValue = animationSpec.calculateTargetValue(value, initialVelocity)
    val adjustedTarget = adjustTarget?.invoke(targetValue)
    return if (adjustedTarget != null) {
        animateTo(
            targetValue = adjustedTarget,
            initialVelocity = initialVelocity,
            block = block
        )
    } else {
        animateDecay(
            initialVelocity = initialVelocity,
            animationSpec = animationSpec,
            block = block,
        )
    }
}
