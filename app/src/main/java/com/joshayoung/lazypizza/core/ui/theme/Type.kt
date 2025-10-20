package com.joshayoung.lazypizza.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.joshayoung.lazypizza.R

val instrumentSansMedium = FontFamily(Font(R.font.instrument_sans_medium))
val instrumentSansSemiBold = FontFamily(Font(R.font.instrument_sans_semibold))
val instrumentSansRegular = FontFamily(Font(R.font.instrumentsans_regular))

val Typography =
    Typography(
        titleLarge =
            TextStyle(
                fontFamily = instrumentSansSemiBold,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                lineHeight = 28.sp
            ),
        titleMedium =
            TextStyle(
                fontFamily = instrumentSansSemiBold,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                lineHeight = 24.sp
            ),
        titleSmall =
            TextStyle(
                fontFamily = instrumentSansSemiBold,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                lineHeight = 22.sp
            ),
        labelMedium =
            TextStyle(
                fontFamily = instrumentSansSemiBold,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                lineHeight = 16.sp
            ),
        bodyLarge =
            TextStyle(
                fontFamily = instrumentSansRegular,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 22.sp
            ),
        bodyMedium =
            TextStyle(
                fontFamily = instrumentSansMedium,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 22.sp
            ),
        bodySmall =
            TextStyle(
                fontFamily = instrumentSansRegular,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 18.sp
            )
        // body-3-medium ?
        // body-4-regular ?
    )
