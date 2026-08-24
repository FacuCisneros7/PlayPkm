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
import com.electrofire.playpkm.ui.Components.TutorialOverlay
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

        // Llamada temporal para subir las nuevas fusiones
        subirFusiones()

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

fun subirFusiones() {

    val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
    val coleccion = db.collection("Fusion")

    val nuevasFusiones = listOf(
        com.electrofire.playpkm.Data.Fusion(0, "https://i.ibb.co/23gX1BcW/63-323.png", arrayListOf("Abra", "Prinplup")),
        com.electrofire.playpkm.Data.Fusion(1, "https://i.ibb.co/YBLRnVXW/427-355.png", arrayListOf("breloom", "snorunt")),
        com.electrofire.playpkm.Data.Fusion(2, "https://i.ibb.co/xqKqtLRX/499-330.png", arrayListOf("minior", "pawniard")),
        com.electrofire.playpkm.Data.Fusion(3, "https://i.ibb.co/Wp2mT29k/315-132.png", arrayListOf("ditto", "arceus")),
        com.electrofire.playpkm.Data.Fusion(4, "https://i.ibb.co/7t8fphtC/63-42.png", arrayListOf("golbat", "abra")),
        com.electrofire.playpkm.Data.Fusion(5, "https://i.ibb.co/0R4zFh73/417-322.png", arrayListOf("piplup", "fletching")),
        com.electrofire.playpkm.Data.Fusion(6, "https://i.ibb.co/CK7GP9m7/274-211.png", arrayListOf("mamoswine", "qwilfish")),
        com.electrofire.playpkm.Data.Fusion(7, "https://i.ibb.co/Y4kMTWHg/213-452.png", arrayListOf("shuckle", "bewear")),
        com.electrofire.playpkm.Data.Fusion(8, "https://i.ibb.co/Bp9WnGb/226-395.png", arrayListOf("mantine", "bagon")),
        com.electrofire.playpkm.Data.Fusion(9, "https://i.ibb.co/CKjBNdGG/436-288.png", arrayListOf("gallade", "carvanha")),
        com.electrofire.playpkm.Data.Fusion(10, "https://i.ibb.co/bMyZFC7Y/281-435.png", arrayListOf("blaziken", "garbodor")),
        com.electrofire.playpkm.Data.Fusion(11, "https://i.ibb.co/DPBjPGSL/355-200.png", arrayListOf("breloom", "misdreavus")),
        com.electrofire.playpkm.Data.Fusion(12, "https://i.ibb.co/9mCV5Kdr/298-436.png", arrayListOf("gabite", "carvanha")),
        com.electrofire.playpkm.Data.Fusion(13, "https://i.ibb.co/tpV2kSvr/481-339.png", arrayListOf("chesnaught", "sylveon")),
        com.electrofire.playpkm.Data.Fusion(14, "https://i.ibb.co/1tg9X3Xy/294-466.png", arrayListOf("bidoof", "meloetta")),
        com.electrofire.playpkm.Data.Fusion(15, "https://i.ibb.co/DfgVvHh7/102-362.png", arrayListOf("cofagrigus", "exeggcute")),
        com.electrofire.playpkm.Data.Fusion(16, "https://i.ibb.co/sMxzzTW/255-49.png", arrayListOf("mismagius", "venomoth")),
        com.electrofire.playpkm.Data.Fusion(17, "https://i.ibb.co/SDQcqXzd/222-113.png", arrayListOf("chansey", "corsola")),
        com.electrofire.playpkm.Data.Fusion(18, "https://i.ibb.co/1fTCXq5p/39-363.png", arrayListOf("jigglypuff", "Galvantula")),
        com.electrofire.playpkm.Data.Fusion(19, "https://i.ibb.co/8DM7PCMt/375-228.png", arrayListOf("deino", "houndour")),
        com.electrofire.playpkm.Data.Fusion(20, "https://i.ibb.co/XrnH5dby/222-368.png", arrayListOf("hitmonchan", "beedrill")),
        com.electrofire.playpkm.Data.Fusion(21, "https://i.ibb.co/5QQ7JkZ/389-23.png", arrayListOf("luxio", "ekans")),
        com.electrofire.playpkm.Data.Fusion(22, "https://i.ibb.co/PvtkjcSH/287-284a.png", arrayListOf("gardevoir", "swampert")),
        com.electrofire.playpkm.Data.Fusion(23, "https://i.ibb.co/TDjqxtsn/566-125.png", arrayListOf("electabuzz", "woobat")),
        com.electrofire.playpkm.Data.Fusion(24, "https://i.ibb.co/22tx91K/215-248.png", arrayListOf("sneasel", "tyranitar")),
        com.electrofire.playpkm.Data.Fusion(25, "https://i.ibb.co/Pvqft7kF/171-161a.png", arrayListOf("lanturn", "sentret")),
        com.electrofire.playpkm.Data.Fusion(26, "https://i.ibb.co/PsGVggRh/327-441.png", arrayListOf("honedge", "noivern")),
        com.electrofire.playpkm.Data.Fusion(27, "https://i.ibb.co/Cp5bpdWs/563-59.png", arrayListOf("arcanine", "huntail")),
        com.electrofire.playpkm.Data.Fusion(28, "https://i.ibb.co/5xLTfYNc/355-364.png", arrayListOf("breloom", "ferrothorn")),
        com.electrofire.playpkm.Data.Fusion(29, "https://i.ibb.co/kgLbzR3G/285-524.png", arrayListOf("hariyama", "ralts")),
        com.electrofire.playpkm.Data.Fusion(30, "https://i.ibb.co/mCrcPHsF/262-112.png", arrayListOf("weavile", "rhydon")),
        com.electrofire.playpkm.Data.Fusion(31, "https://i.ibb.co/9HYLSLqZ/443-407.png", arrayListOf("altaria", "duosion")),
        com.electrofire.playpkm.Data.Fusion(32, "https://i.ibb.co/3y3hWTf5/146-268a.png", arrayListOf("moltres", "magmortar")),
        com.electrofire.playpkm.Data.Fusion(33, "https://i.ibb.co/4nxB0tp4/448-426b.png", arrayListOf("regice", "tyrantrum")),
        com.electrofire.playpkm.Data.Fusion(34, "https://i.ibb.co/kg3WtNT4/508-503.png", arrayListOf("beautifly", "mightyena")),
        com.electrofire.playpkm.Data.Fusion(35, "https://i.ibb.co/0RkNhRsf/192-299.png", arrayListOf("sunflora", "garchomp")),
        com.electrofire.playpkm.Data.Fusion(36, "https://i.ibb.co/BHcmWPVP/166-144b.png", arrayListOf("ledian", "articuno")),
        com.electrofire.playpkm.Data.Fusion(37, "https://i.ibb.co/tMtTjNYy/50-505.png", arrayListOf("linoone", "diglett")),
        com.electrofire.playpkm.Data.Fusion(38, "https://i.ibb.co/mr4Xy2ph/525-314c.png", arrayListOf("skitty", "wailord")),
        com.electrofire.playpkm.Data.Fusion(39, "https://i.ibb.co/JwnR60cP/304-561.png", arrayListOf("walrein", "armaldo")),
        com.electrofire.playpkm.Data.Fusion(40, "https://i.ibb.co/39RMMdQ0/90-241.png", arrayListOf("shellder", "miltank")),
        com.electrofire.playpkm.Data.Fusion(41, "https://i.ibb.co/twNWTB1F/73-449.png", arrayListOf("registeel", "tentacruel")),
        com.electrofire.playpkm.Data.Fusion(42, "https://i.ibb.co/gb6N8mVM/543-481.png", arrayListOf("chesnaught", "seviper")),
        com.electrofire.playpkm.Data.Fusion(43, "https://i.ibb.co/NdHhqmq8/178-309.png", arrayListOf("xatu", "slaking")),
        com.electrofire.playpkm.Data.Fusion(44, "https://i.ibb.co/k6BQ1jMc/490-524.png", arrayListOf("hariyama", "gourgeist")),
        com.electrofire.playpkm.Data.Fusion(45, "https://i.ibb.co/6RYh2YgP/540-287a.png", arrayListOf("grumpig", "gardevoir")),
        com.electrofire.playpkm.Data.Fusion(46, "https://i.ibb.co/20kvfmbz/305-318.png", arrayListOf("cranidos", "torterra")),
        com.electrofire.playpkm.Data.Fusion(47, "https://i.ibb.co/39m5pG5W/437-572.png", arrayListOf("sharpedo", "dragalge")),
        com.electrofire.playpkm.Data.Fusion(48, "https://i.ibb.co/SwDW9swg/549-487.png", arrayListOf("greninja", "crawdaunt")),
        com.electrofire.playpkm.Data.Fusion(49, "https://i.ibb.co/DP3tc9Th/515-73.png", arrayListOf("tentacruel", "swellow")),
        com.electrofire.playpkm.Data.Fusion(50, "https://i.ibb.co/YF30PXst/176-146.png", arrayListOf("togetic", "moltres")),
        com.electrofire.playpkm.Data.Fusion(51, "https://i.ibb.co/mr1ckb6H/302-570.png", arrayListOf("cradily", "eelektross")),
        com.electrofire.playpkm.Data.Fusion(52, "https://i.ibb.co/TqTrBKXV/271-560.png", arrayListOf("leafeon", "sealeo")),
        com.electrofire.playpkm.Data.Fusion(53, "https://i.ibb.co/FtX3Pfd/534-462.png", arrayListOf("illumise", "aurorus")),
        com.electrofire.playpkm.Data.Fusion(54, "https://i.ibb.co/QFWNwRCB/244-266.png", arrayListOf("entei", "tangrowth")),
        com.electrofire.playpkm.Data.Fusion(55, "https://i.ibb.co/D63hvB7/424-80.png", arrayListOf("scolipede", "slowbro")),
        com.electrofire.playpkm.Data.Fusion(56, "https://i.ibb.co/1GV6NHNq/331-335.png", arrayListOf("bisharp", "milotic")),
        com.electrofire.playpkm.Data.Fusion(57, "https://i.ibb.co/6cCmsy9q/346-278a.png", arrayListOf("regigigas", "sceptile")),
        com.electrofire.playpkm.Data.Fusion(58, "https://i.ibb.co/gYg7JGB/85-546a.png", arrayListOf("dodrio", "barboach")),
        com.electrofire.playpkm.Data.Fusion(59, "https://i.ibb.co/QsZcjw1/502-130.png", arrayListOf("poochyena", "gyarados")),
        com.electrofire.playpkm.Data.Fusion(60, "https://i.ibb.co/5xGvY8xD/480-372.png", arrayListOf("quilladin", "talonflame")),
    )


    nuevasFusiones.forEach { fusion ->
        coleccion.add(fusion)
            .addOnSuccessListener {
                android.util.Log.d("Uploader", "Subida fusión ID: ${fusion.id}")
            }
            .addOnFailureListener { e ->
                android.util.Log.e("Uploader", "Error en ID ${fusion.id}: ${e.message}")
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

                // Tutorial Overlay
                val showTutorial = !statsViewModel.userData.hasSeenTutorial && 
                                  currentRoute == Screen.Home.route && 
                                  statsViewModel.isUserLoaded &&
                                  !statsViewModel.userData.userName.isNullOrEmpty()

                TutorialOverlay(
                    show = showTutorial,
                    onComplete = { statsViewModel.completarTutorial() }
                )
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

    androidx.compose.runtime.LaunchedEffect(isUserLoaded) {
        if (!isUserLoaded) {
            statsViewModel.cargarStats()
        }
    }

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
