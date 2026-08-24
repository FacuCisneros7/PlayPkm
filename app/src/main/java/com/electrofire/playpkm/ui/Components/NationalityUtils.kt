package com.electrofire.playpkm.ui.Components

import com.electrofire.playpkm.R

data class Nationality(
    val name: String,
    val flagRes: Int
)

object NationalityUtils {
    val nationalities = listOf(
        Nationality("Argentina", R.drawable.argentinaflag),
        Nationality("Bolivia", R.drawable.boliviaflag),
        Nationality("Chile", R.drawable.chileflag),
        Nationality("Colombia", R.drawable.colombiaflag),
        Nationality("Costa Rica", R.drawable.costaricaflag),
        Nationality("Cuba", R.drawable.cubaflag),
        Nationality("Ecuador", R.drawable.ecuadorflag),
        Nationality("El Salvador", R.drawable.elsalvadorflag),
        Nationality("España", R.drawable.espanaflag),
        Nationality("Guatemala", R.drawable.guatemalaflag),
        Nationality("Honduras", R.drawable.hondurasflag),
        Nationality("México", R.drawable.mexicoflag),
        Nationality("Nicaragua", R.drawable.nicaraguaflag),
        Nationality("Panamá", R.drawable.panamaflag),
        Nationality("Paraguay", R.drawable.paraguayflag),
        Nationality("Perú", R.drawable.peruflag),
        Nationality("República Dominicana", R.drawable.republicadominicanaflag),
        Nationality("Uruguay", R.drawable.uruguayflag),
        Nationality("Venezuela", R.drawable.venezuelaflag)
    )

    fun getFlagForNationality(name: String?): Int? {
        return nationalities.find { it.name == name }?.flagRes
    }
}
