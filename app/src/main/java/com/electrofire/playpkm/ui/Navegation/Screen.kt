package com.electrofire.playpkm.ui.Navegation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object FirstGame : Screen("first_game")
    object SecondGame : Screen("second_game")
    object ThirdGame : Screen("third_game")
    object FourthGame : Screen("fourth_game")
    object FiftGame : Screen("fift_game")
    object SixthGame : Screen("sixth_game")
    object SeventhGame : Screen("seventh_game")

    object EightGame : Screen("eight_game")
    object RankingScreen : Screen("ranking")

    object NewUserScreen : Screen("new_user")
}

//Esa sealed class Screen es una manera ordenada
// y segura de manejar las rutas de navegación en tu app con Compose.
// Centraliza todas las rutas de navegación en un solo lugar.
