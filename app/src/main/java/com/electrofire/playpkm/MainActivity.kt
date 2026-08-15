package com.electrofire.playpkm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.electrofire.playpkm.Domain.ForceUpdateGate
import com.electrofire.playpkm.ui.Components.GradientBackground
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

                // Gestión del ciclo de vida para la música
                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        when (event) {
                            Lifecycle.Event.ON_RESUME -> musicViewModel.play()
                            Lifecycle.Event.ON_PAUSE -> musicViewModel.pause()
                            else -> {}
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }

                val isConnected by networkMonitor.isConnected.collectAsState()
                if (isConnected) {
                    ForceUpdateGate {
                        ViewContainer(musicViewModel)
                    }
                } else {
                    NotInternetScreen(showBackground = true)
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
        containerColor = Color.Transparent,
        topBar = {
            if (!isLoading &&
                currentRoute != Screen.NewUserScreen.route &&
                currentRoute != Screen.Register.route &&
                currentRoute != Screen.Login.route
            )
                ToolBar()
        },
        bottomBar = {
            if (
                currentRoute == Screen.Home.route || currentRoute == Screen.RankingScreen.route || currentRoute == Screen.UserScreen.route
            ) {
                BottomBar(navController = navController)
            }
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                GradientBackground()

                Box(modifier = Modifier.padding(innerPadding)) {
                    AppNavigation(navController, statsViewModel, authViewModel, musicViewModel)
                }
            }
        }
    )
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    statsViewModel: HomeStatsViewModel,
    authViewModel: AuthViewModel,
    musicViewModel: MusicViewModel
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
                Screen.Register.route
            } else if (statsViewModel.userData.userName.isNullOrEmpty()) {
                Screen.NewUserScreen.route
            } else Screen.Home.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }

        ) {
            composable(
                route = Screen.Home.route,
                enterTransition = {
                    slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500))
                },
                exitTransition = {
                    slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
                },
                popEnterTransition = {
                    slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500))
                },
                popExitTransition = {
                    slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
                }
            ) { HomeScreen(navController, statsViewModel, authViewModel) }
            
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
            
            composable(
                route = Screen.RankingScreen.route,
                enterTransition = {
                    slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500))
                },
                exitTransition = {
                    slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
                },
                popEnterTransition = {
                    slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500))
                },
                popExitTransition = {
                    slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
                }
            ) {
                RankingScreen()
            }
            
            composable(Screen.NewUserScreen.route) {
                NewUserScreen(
                    navController,
                    statsViewModel
                )
            }
            composable(Screen.Register.route) {
                RegisterScreen(
                    navController,
                    authViewModel = authViewModel
                )
            }
            composable(Screen.Login.route) {
                LoginScreen(
                    navController,
                    authViewModel = authViewModel,
                    statsViewModel = statsViewModel
                )
            }
            
            composable(
                route = Screen.UserScreen.route,
                enterTransition = {
                    slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500))
                },
                exitTransition = {
                    slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
                },
                popEnterTransition = {
                    slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500))
                },
                popExitTransition = {
                    slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
                }
            ) {
                UserScreen(navController, statsViewModel, musicViewModel)
            }
        }
    }

}
