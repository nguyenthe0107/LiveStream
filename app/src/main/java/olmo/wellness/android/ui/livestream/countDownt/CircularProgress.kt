/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package olmo.wellness.android.ui.livestream.countDownt
import android.util.Log
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgress(
    modifier: Modifier = Modifier,
    color: Color,
    backgroundColor: Color = color,
    startingAngle: Float = 270f,
    progress: Float,
    animate: Boolean = true,
    animationSpec: AnimationSpec<Float> = tween(
        durationMillis = 250,
        easing = LinearOutSlowInEasing
    )
) {
    val animatedProgress: Float by animateFloatAsState(
        targetValue = progress,
        animationSpec = animationSpec
    )
    Canvas(modifier) {
        val sweepAngle = (360 * if (animate) animatedProgress else progress) / 100
        val ringRadius = size.minDimension * 0.15f
        val size = Size(size.width, size.height)
        drawArc(
            backgroundColor,
            startingAngle,
            360f,
            false,
            size = size,
            alpha = 0.2f,
            style = Stroke(ringRadius)
        )
        drawArc(color, startingAngle, sweepAngle, false, size = size, style = Stroke(ringRadius, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun CircularProgressBar(
    percentage: Float,
    radius: Dp = 88.dp,
    animationDuration: Int = 1000,
) {
    var animFinished by remember {
        mutableStateOf(false)
    }
    var duration by remember {
        mutableStateOf(0F)
    }
    val maxStep = 4
    duration = maxStep - percentage
    val currentPercent = animateFloatAsState(
        targetValue = if (animFinished) duration else 0f,
        animationSpec = tween(
            durationMillis = animationDuration,
        )
    )
    LaunchedEffect(key1 = true) {
        animFinished = true
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(radius * 2)) {
        Canvas(modifier = Modifier.size(radius * 2)) {
            drawArc(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1A3194),
                        Color(0xFF1D41C5),
                        Color(0xFF7F98FF)
                    )
                ),
                startAngle = -90f,
                sweepAngle = 120f * currentPercent.value,
                useCenter = false,
                style = Stroke(
                    7.dp.toPx(),
                    cap = StrokeCap.Round,
                ),
            )
        }

    }
}
