package com.himanshoe.kalendar.common
/*
* MIT License
*
* Copyright (c) 2022 Himanshu Singh
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.theme.Grid
import olmo.wellness.android.ui.livestream.schedule.cell.kalendar.common.theme.KalendarTheme

sealed class KalendarSelector(
    open val shape: Shape,
    open val selectedColor: Color,
    open val defaultColor: Color,
    open val todayColor: Color,
    open val selectedTextColor: Color,
    open val defaultTextColor: Color,
    open val eventTextColor: Color,
    open val defaultDayColor: Color,
    open val textTitle: Color,
) {

    data class Circle(
        override val selectedColor: Color = KalendarTheme.colors.selectedColor,
        override val defaultColor: Color = KalendarTheme.colors.white,
        override val eventTextColor: Color = KalendarTheme.colors.eventTextColor,
        override val todayColor: Color = KalendarTheme.colors.todayColor,
        override val selectedTextColor: Color = KalendarTheme.colors.white,
        override val defaultTextColor: Color = KalendarTheme.colors.textGray,
        override val defaultDayColor: Color = KalendarTheme.colors.dayDefault,
        override val textTitle: Color = KalendarTheme.colors.white

    ) : KalendarSelector(
        CircleShape,
        selectedColor,
        defaultColor,
        todayColor,
        selectedTextColor,
        defaultTextColor,
        eventTextColor,
        defaultDayColor,
        textTitle
    )

    data class Dot(
        override val selectedColor: Color = KalendarTheme.colors.selectedColor,
        override val defaultColor: Color = KalendarTheme.colors.white,
        override val eventTextColor: Color = KalendarTheme.colors.eventTextColor,
        override val todayColor: Color = KalendarTheme.colors.todayColor,
        override val selectedTextColor: Color = KalendarTheme.colors.selectedColor,
        override val defaultTextColor: Color = KalendarTheme.colors.black,
        override val defaultDayColor: Color = KalendarTheme.colors.dayDefault,
        override val textTitle: Color = KalendarTheme.colors.white
    ) : KalendarSelector(
        CircleShape,
        selectedColor,
        defaultColor,
        todayColor,
        selectedTextColor,
        defaultTextColor,
        eventTextColor,
        defaultDayColor,
        textTitle
    )

    data class Square(
        override val selectedColor: Color = KalendarTheme.colors.selectedColor,
        override val defaultColor: Color = KalendarTheme.colors.white,
        override val todayColor: Color = KalendarTheme.colors.todayColor,
        override val eventTextColor: Color = KalendarTheme.colors.eventTextColor,
        override val selectedTextColor: Color = KalendarTheme.colors.white,
        override val defaultTextColor: Color = KalendarTheme.colors.black,
        override val defaultDayColor: Color = KalendarTheme.colors.dayDefault,
        override val textTitle: Color = KalendarTheme.colors.dayDefault
    ) : KalendarSelector(
        RectangleShape,
        selectedColor,
        defaultColor,
        todayColor,
        selectedTextColor,
        defaultTextColor,
        eventTextColor,
        defaultDayColor,
        textTitle
    )

    data class CutCornerSquare(
        override val selectedColor: Color = KalendarTheme.colors.selectedColor,
        override val defaultColor: Color = KalendarTheme.colors.white,
        override val todayColor: Color = KalendarTheme.colors.todayColor,
        override val eventTextColor: Color = KalendarTheme.colors.eventTextColor,
        override val selectedTextColor: Color = KalendarTheme.colors.white,
        override val defaultTextColor: Color = KalendarTheme.colors.black,
        override val defaultDayColor: Color = KalendarTheme.colors.dayDefault,
        override val textTitle: Color = KalendarTheme.colors.white
    ) : KalendarSelector(
        CutCornerShape(Grid.OneHalf),
        selectedColor,
        defaultColor,
        todayColor,
        selectedTextColor,
        defaultTextColor,
        eventTextColor,
        defaultDayColor,
        textTitle
    )

    data class DiamondShape(
        override val selectedColor: Color = KalendarTheme.colors.selectedColor,
        override val defaultColor: Color = KalendarTheme.colors.white,
        override val todayColor: Color = KalendarTheme.colors.todayColor,
        override val eventTextColor: Color = KalendarTheme.colors.eventTextColor,
        override val selectedTextColor: Color = KalendarTheme.colors.white,
        override val defaultTextColor: Color = KalendarTheme.colors.black,
        override val defaultDayColor: Color = KalendarTheme.colors.dayDefault,
        override val textTitle: Color = KalendarTheme.colors.white
    ) : KalendarSelector(
        CutCornerShape(50),
        selectedColor,
        defaultColor,
        todayColor,
        selectedTextColor,
        defaultTextColor,
        eventTextColor,
        defaultDayColor,
        textTitle
    )

    data class Rounded(
        override val selectedColor: Color = KalendarTheme.colors.selectedColor,
        override val defaultColor: Color = KalendarTheme.colors.white,
        override val todayColor: Color = KalendarTheme.colors.transparent,
        override val eventTextColor: Color = KalendarTheme.colors.eventTextColor,
        override val selectedTextColor: Color = KalendarTheme.colors.white,
        override val defaultTextColor: Color = KalendarTheme.colors.textGray,
        override val defaultDayColor: Color = KalendarTheme.colors.dayDefault,
        override val textTitle: Color = KalendarTheme.colors.white
    ) : KalendarSelector(
        RoundedCornerShape(Grid.four),
        selectedColor,
        defaultColor,
        todayColor,
        selectedTextColor,
        defaultTextColor,
        eventTextColor,
        defaultDayColor,
        textTitle
    )


    data class RoundedCorner(
        override val selectedColor: Color = KalendarTheme.colors.selectedColor,
        override val defaultColor: Color = KalendarTheme.colors.white,
        override val todayColor: Color = KalendarTheme.colors.transparent,
        override val eventTextColor: Color = KalendarTheme.colors.eventTextColor,
        override val selectedTextColor: Color = KalendarTheme.colors.white,
        override val defaultTextColor: Color = KalendarTheme.colors.textGray,
        override val defaultDayColor: Color = KalendarTheme.colors.dayDefault,
        override val textTitle: Color = KalendarTheme.colors.white
    ) : KalendarSelector(
        RoundedCornerShape(Grid.One),
        selectedColor,
        defaultColor,
        todayColor,
        selectedTextColor,
        defaultTextColor,
        eventTextColor,
        defaultDayColor,
        textTitle
    )

    data class CustomShape(
        override val shape: Shape,
        override val selectedColor: Color = KalendarTheme.colors.selectedColor,
        override val defaultColor: Color = KalendarTheme.colors.white,
        override val todayColor: Color = KalendarTheme.colors.todayColor,
        override val eventTextColor: Color = KalendarTheme.colors.eventTextColor,
        override val selectedTextColor: Color = KalendarTheme.colors.white,
        override val defaultTextColor: Color = KalendarTheme.colors.black,
        override val defaultDayColor: Color = KalendarTheme.colors.dayDefault,
        override val textTitle: Color = KalendarTheme.colors.white
    ) : KalendarSelector(
        shape,
        selectedColor,
        defaultColor,
        todayColor,
        selectedTextColor,
        defaultTextColor,
        eventTextColor,
        defaultDayColor,
        textTitle
    )
}
