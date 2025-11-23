package com.electrofire.playpkm.ui.Themes

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.electrofire.playpkm.R

val Nunito = FontFamily(

    Font(R.font.nunito_regular, weight = FontWeight.Normal),
    Font(R.font.nunito_medium, weight = FontWeight.Medium),
    Font(R.font.nunito_semibold, weight = FontWeight.SemiBold),
    Font(R.font.nunito_bold, weight = FontWeight.Bold),
    Font(R.font.nunito_extrabold, weight = FontWeight.ExtraBold),

    )

val Typography = Typography(

    bodyLarge = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 22.sp
    )

)
