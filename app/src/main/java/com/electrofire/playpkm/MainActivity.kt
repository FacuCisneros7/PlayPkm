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
import com.electrofire.playpkm.ui.Screens.games.ImpostorGame
import com.electrofire.playpkm.ui.Screens.games.ThousandShadowsGame
import com.electrofire.playpkm.ui.Screens.games.StatsMysteryGame
import com.electrofire.playpkm.ui.Screens.games.SilhouetteGame
import com.electrofire.playpkm.ui.Screens.games.MovePowerGame
import com.electrofire.playpkm.ui.Screens.main.HomeScreen
import com.electrofire.playpkm.ui.Screens.auth.LoginScreen
import com.electrofire.playpkm.ui.Screens.auth.NewUserScreen
import com.electrofire.playpkm.ui.Screens.games.GoodChoiceGame
import com.electrofire.playpkm.ui.Screens.error.NotInternetScreen
import com.electrofire.playpkm.ui.Screens.main.RankingScreen
import com.electrofire.playpkm.ui.Screens.auth.RegisterScreen
import com.electrofire.playpkm.ui.Screens.games.BlurredCardGame
import com.electrofire.playpkm.ui.Screens.games.TheBestGame
import com.electrofire.playpkm.ui.Screens.games.FusionGame
import com.electrofire.playpkm.ui.Screens.games.ZoomGame
import com.electrofire.playpkm.ui.Screens.games.AbilityGame
import com.electrofire.playpkm.ui.Screens.games.BeforeAfterGame
import com.electrofire.playpkm.ui.Screens.main.UserScreen
import com.electrofire.playpkm.ui.Themes.PLAYPKMTheme
import com.electrofire.playpkm.ui.ViewModels.main.AuthViewModel
import com.electrofire.playpkm.ui.ViewModels.main.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.main.MusicViewModel
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
            
            composable(Screen.SilhouetteGame.route) {
                SilhouetteGame(
                    navController,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.BlurredCardGame.route) {
                BlurredCardGame(
                    navController,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.AbilityGame.route) {
                AbilityGame(
                    navController,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.MovePowerGame.route) {
                MovePowerGame(
                    navController,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.StatsMysteryGame.route) {
                StatsMysteryGame(
                    navController,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.FusionGame.route) {
                FusionGame(
                    navController,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.TheBestGame.route) {
                TheBestGame(
                    navController,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.ImpostorGame.route) {
                ImpostorGame(
                    navController,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.GoodChoiceGame.route) {
                GoodChoiceGame(
                    navController,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.ZoomGame.route) {
                ZoomGame(
                    navController = navController,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.ThousandShadowsGame.route) {
                ThousandShadowsGame(
                    navController = navController,
                    statsViewModel = statsViewModel
                )
            }
            composable(Screen.BeforeAfterGame.route) {
                BeforeAfterGame(
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
