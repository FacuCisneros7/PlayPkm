package com.electrofire.playpkm.ui.Navegation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object SilhouetteGame : Screen("first_game")
    data object BlurredCardGame : Screen("second_game")
    data object AbilityGame : Screen("third_game")
    data object MovePowerGame : Screen("fourth_game")
    data object StatsMysteryGame : Screen("fift_game")
    data object FusionGame : Screen("sixth_game")
    data object TheBestGame : Screen("seventh_game")
    data object ImpostorGame : Screen("eight_game")
    data object GoodChoiceGame : Screen("nine_game")
    data object ZoomGame : Screen("ten_game")
    data object ThousandShadowsGame : Screen("eleven_game")
    data object BeforeAfterGame : Screen("twelve_game")
    data object TypeGame : Screen("type_game")
    data object RankingScreen : Screen("ranking")
    data object Shop : Screen("shop")
    data object NewUserScreen : Screen("new_user")
    data object UserScreen : Screen("user_screen")
    data object Login : Screen("login")
    data object Register : Screen("register")

}
