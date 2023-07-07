package olmo.wellness.android.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R


val MontserratFont = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_bold, FontWeight.Bold),
    Font(R.font.montserrat_medium, FontWeight.Medium)
)

// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(
        fontFamily = MontserratFont,
        fontWeight = FontWeight.Bold,
        fontSize = 96.sp,
        letterSpacing = (-1.5).sp,
        color = Neutral_Gray_9
    ),
    h2 = TextStyle(
        fontFamily = MontserratFont,
        fontWeight = FontWeight.Bold,
        fontSize = 60.sp,
        letterSpacing = (-0.5).sp,
        color = Neutral_Gray_9
    ),
    h3 = TextStyle(
        fontFamily = MontserratFont,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        letterSpacing = 0.sp,
        color = Neutral_Gray_9
    ),
    h4 = TextStyle(
        fontFamily = MontserratFont,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        letterSpacing = 0.25.sp,
        color = Neutral_Gray_9
    ),
    h5 = TextStyle(
        fontFamily = MontserratFont,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        letterSpacing = 0.sp,
        color = Neutral_Gray_9
    ),
    h6 = TextStyle(
        fontFamily = MontserratFont,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        letterSpacing = 0.15.sp,
        color = Neutral_Gray_9
    ),
    subtitle1 = TextStyle(
        fontFamily = MontserratFont,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp,
        color = Neutral_Gray_9
    ),
    subtitle2 = TextStyle(
        fontFamily = MontserratFont,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp,
        color = Neutral_Gray_9
    ),
    body1 = TextStyle(
        fontFamily = MontserratFont,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        letterSpacing = 0.5.sp,
        color = Neutral_Gray_9
    ),
    body2 = TextStyle(
        fontFamily = MontserratFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp,
        color = Neutral_Gray_9
    ),
    button = TextStyle(
        fontFamily = MontserratFont,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        letterSpacing = 1.25.sp,
        color = Neutral_Gray_9
    ),
    caption = TextStyle(
        fontFamily = MontserratFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.4.sp,
        color = Neutral_Gray_9
    ),
    overline = TextStyle(
        fontFamily = MontserratFont,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        letterSpacing = 0.1.sp,
        color = Neutral_Gray_9
    ),
)