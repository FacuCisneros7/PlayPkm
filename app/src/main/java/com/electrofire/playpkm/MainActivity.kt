package com.electrofire.playpkm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.electrofire.playpkm.Domain.ForceUpdateGate
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.Scaffold.BottomBar
import com.electrofire.playpkm.ui.Scaffold.NetworkMonitor
import com.electrofire.playpkm.ui.Scaffold.ToolBar
import com.electrofire.playpkm.ui.Screens.EightGame
import com.electrofire.playpkm.ui.Screens.ElevenGame
import com.electrofire.playpkm.ui.Screens.FiftGame
import com.electrofire.playpkm.ui.Screens.FirstGame
import com.electrofire.playpkm.ui.Screens.FourthGame
import com.electrofire.playpkm.ui.Screens.HomeScreen
import com.electrofire.playpkm.ui.Screens.LoginScreen
import com.electrofire.playpkm.ui.Screens.NewUserScreen
import com.electrofire.playpkm.ui.Screens.NinthGame
import com.electrofire.playpkm.ui.Screens.NotInternetScreen
import com.electrofire.playpkm.ui.Screens.RankingScreen
import com.electrofire.playpkm.ui.Screens.RegisterScreen
import com.electrofire.playpkm.ui.Screens.SecondGame
import com.electrofire.playpkm.ui.Screens.SeventhGame
import com.electrofire.playpkm.ui.Screens.SixthGame
import com.electrofire.playpkm.ui.Screens.TenGame
import com.electrofire.playpkm.ui.Screens.ThirdGame
import com.electrofire.playpkm.ui.Screens.TwelveGame
import com.electrofire.playpkm.ui.Screens.UserScreen
import com.electrofire.playpkm.ui.Themes.PLAYPKMTheme
import com.electrofire.playpkm.ui.ViewModels.AuthViewModel
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.MusicViewModel
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        MobileAds.initialize(this)
        networkMonitor = NetworkMonitor(this)

        setContent {
            PLAYPKMTheme {

                val musicViewModel: MusicViewModel = hiltViewModel()

                val isConnected by networkMonitor.isConnected.collectAsState()
                if (isConnected) {
                    ForceUpdateGate {
                        ViewContainer(musicViewModel)
                    }
                } else {
                    NotInternetScreen()
                }
            }
        }
    }
}

@Composable
fun ViewContainer(musicViewModel: MusicViewModel) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val statsViewModel: HomeStatsViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isLoading = statsViewModel.userData.userName == null

    Scaffold(
        topBar = {
            if (!isLoading &&
                currentRoute != "new_user" &&
                currentRoute != "register" &&
                currentRoute != "login"
            )
                ToolBar(navController, statsViewModel, musicViewModel)
        },
        bottomBar = {
            if (
                currentRoute == "home" || currentRoute == "ranking" || currentRoute == "user_screen"
                || currentRoute == "rankingGC"
            ) {
                BottomBar(navController = navController)
            }
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                AppNavigation(navController, statsViewModel, authViewModel)
            }
        }
    )
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    statsViewModel: HomeStatsViewModel,
    authViewModel: AuthViewModel
) {

    val auth = FirebaseAuth.getInstance()

    // Creamos un estado de carga
    val isUserLoaded by remember { derivedStateOf { statsViewModel.isUserLoaded } }

    if (!isUserLoaded) {
        // Pantalla de loading mientras se carga el username
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Loading()
        }
    } else {
        NavHost(
            navController = navController,
            startDestination =
                if (auth.currentUser == null) {
                    "register"
                } else if (statsViewModel.userData.userName.isNullOrEmpty()) {
                    "new_user"
                } else "home"

        ) {
            composable("home") { HomeScreen(navController, statsViewModel, authViewModel) }
            composable(Screen.FirstGame.route) {
                FirstGame(
                    navController,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.SecondGame.route) {
                SecondGame(
                    navController,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.ThirdGame.route) {
                ThirdGame(
                    navController,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.FourthGame.route) {
                FourthGame(
                    navController,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.FiftGame.route) {
                FiftGame(
                    navController,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.SixthGame.route) {
                SixthGame(
                    navController,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.SeventhGame.route) {
                SeventhGame(
                    navController,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.EightGame.route) {
                EightGame(
                    navController,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.NinthGame.route) {
                NinthGame(
                    navController,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.TenGame.route) {
                TenGame(
                    navController = navController,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.ElevenGame.route) {
                ElevenGame(
                    navController = navController,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.TwelveGame.route) {
                TwelveGame(
                    navController = navController,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.RankingScreen.route) {
                RankingScreen()
            }
            composable("new_user") {
                NewUserScreen(
                    navController,
                    statsViewModel
                )
            }
            composable("register") {
                RegisterScreen(
                    navController,
                    authViewModel = authViewModel
                )
            }
            composable("login") {
                LoginScreen(
                    navController,
                    authViewModel = authViewModel,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.UserScreen.route) {
                UserScreen(statsViewModel)
            }
        }
    }

}
