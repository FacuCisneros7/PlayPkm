package com.electrofire.playpkm.ui.Navegation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object FirstGame : Screen("first_game")
    data object SecondGame : Screen("second_game")
    data object ThirdGame : Screen("third_game")
    data object FourthGame : Screen("fourth_game")
    data object FiftGame : Screen("fift_game")
    data object SixthGame : Screen("sixth_game")
    data object SeventhGame : Screen("seventh_game")
    data object EightGame : Screen("eight_game")
    data object NinthGame : Screen("nine_game")
    data object TenGame : Screen("ten_game")
    data object ElevenGame : Screen("eleven_game")
    data object TwelveGame : Screen("twelve_game")
    data object RankingScreen : Screen("ranking")
    data object NewUserScreen : Screen("new_user")
    data object UserScreen : Screen("user_screen")
    data object Login : Screen("login")
    data object Register : Screen("register")

}
