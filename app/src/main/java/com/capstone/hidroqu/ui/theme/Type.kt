package com.capstone.hidroqu.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.fundamentalandroid.hydroqu.R

private val Urbanist = FontFamily(
    Font(R.font.urbanist_extralight, FontWeight.Thin),
    Font(R.font.urbanist_extralightitalic, FontWeight.Thin),
    Font(R.font.urbanist_thin, FontWeight.Thin),
    Font(R.font.urbanist_thinitalic, FontWeight.Thin),
    Font(R.font.urbanist_light, FontWeight.Light),
    Font(R.font.urbanist_lightitalic, FontWeight.Light),
    Font(R.font.urbanist_regular, FontWeight.Normal),
    Font(R.font.urbanist_italic, FontWeight.Normal),
    Font(R.font.urbanist_medium, FontWeight.Medium),
    Font(R.font.urbanist_mediumitalic, FontWeight.Medium),
    Font(R.font.urbanist_bold, FontWeight.Bold),
    Font(R.font.urbanist_bolditalic, FontWeight.Bold),
    Font(R.font.urbanist_black, FontWeight.Black),
    Font(R.font.urbanist_blackitalic, FontWeight.Black),
    Font(R.font.urbanist_extrabold, FontWeight.Black),
    Font(R.font.urbanist_extrabolditalic, FontWeight.Black)
)

val typography = Typography(
    displayLarge = TextStyle(
        fontFamily = Urbanist,
        fontWeight = FontWeight.Black,
        fontSize = 96.sp
    ),
    displayMedium = TextStyle(
        fontFamily = Urbanist,
        fontWeight = FontWeight.Bold,
        fontSize = 60.sp
    ),
    displaySmall = TextStyle(
        fontFamily = Urbanist,
        fontWeight = FontWeight.SemiBold,
        fontSize = 48.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = Urbanist,
        fontWeight = FontWeight.Medium,
        fontSize = 34.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Urbanist,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Urbanist,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Urbanist,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Urbanist,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Urbanist,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Urbanist,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Urbanist,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Urbanist,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Urbanist,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp
    )
)
