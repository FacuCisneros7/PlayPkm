package com.electrofire.playpkm


import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.getValue
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.electrofire.playpkm.ui.Screens.FirstGame
import com.electrofire.playpkm.ui.Screens.HomeScreen
import com.electrofire.playpkm.ui.Screens.SecondGame
import androidx.navigation.compose.composable
import com.electrofire.playpkm.ui.Navegation.Screen
import com.electrofire.playpkm.ui.Themes.PLAYPKMTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Scaffold.BottomBar
import com.electrofire.playpkm.ui.Scaffold.ToolBar
import com.electrofire.playpkm.ui.Screens.DateWarningScreen
import com.electrofire.playpkm.ui.Screens.FiftGame
import com.electrofire.playpkm.ui.Screens.FourthGame
import com.electrofire.playpkm.ui.Screens.NewUserScreen
import com.electrofire.playpkm.ui.Screens.RankingScreen
import com.electrofire.playpkm.ui.Screens.SeventhGame
import com.electrofire.playpkm.ui.Screens.SixthGame
import com.electrofire.playpkm.ui.Screens.ThirdGame
import com.electrofire.playpkm.ui.ViewModels.AuthViewModel
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var timeChangeReceiver: TimeChangeReceiver? = null
    private lateinit var showError: MutableState<Boolean?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)

        // Inicializamos la fecha solo si es la primera vez
        if (!prefs.contains("last_real_time")) {
            prefs.edit().putLong("last_real_time", System.currentTimeMillis()).apply()
        }

        setContent {
            PLAYPKMTheme {
                // Estado nulo mientras se verifica la hora
                showError = remember { mutableStateOf<Boolean?>(null) }

                // Coroutine para verificar hora de internet y última hora verificada
                LaunchedEffect(Unit) {
                    showError.value = hasDateChanged()
                }

                // Registrar BroadcastReceiver mientras la app esté abierta
                DisposableEffect(Unit) {
                    val receiver = TimeChangeReceiver {
                        showError.value = true
                    }
                    val filter = IntentFilter().apply {
                        addAction(Intent.ACTION_TIME_CHANGED)
                        addAction(Intent.ACTION_DATE_CHANGED)
                        addAction(Intent.ACTION_TIMEZONE_CHANGED)
                    }
                    registerReceiver(receiver, filter)
                    timeChangeReceiver = receiver

                    onDispose { unregisterReceiver(receiver) }
                }

                // Mostrar UI según estado
                when (showError.value) {
                    null -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Loading() // pantalla temporal mientras se verifica
                    }
                    true -> DateWarningScreen()
                    false -> ViewContainer()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        // Guardar fecha solo si NO se está mostrando la pantalla de error
        if (showError.value != true) {
            saveCurrentDate()
        }
    }

    // Guardar la fecha actual en SharedPreferences
    private fun saveCurrentDate() {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        prefs.edit().putLong("last_real_time", System.currentTimeMillis()).apply()
    }

    // Verificar si la fecha cambió (hora de internet vs última hora guardada)
    private suspend fun hasDateChanged(): Boolean {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val lastVerifiedTime = prefs.getLong("last_real_time", -1L)

        // Obtenemos la hora actual de internet
        val serverTime = fetchInternetTime()
        val tolerance = 5 * 60 * 1000L // 5 minutos

        // Si nunca se verificó, inicializamos y no hay error
        if (lastVerifiedTime == -1L) {
            prefs.edit().putLong("last_real_time", serverTime).apply()
            return false
        }

        val deviceTime = System.currentTimeMillis()

        // Detectar retroceso o adelanto fuera de tolerancia
        val errorDetected = (deviceTime + tolerance < lastVerifiedTime) ||
                (deviceTime - tolerance > lastVerifiedTime)

        // Solo actualizamos la última hora verificada con SERVER TIME si no hay error
        if (!errorDetected) {
            prefs.edit().putLong("last_real_time", serverTime).apply()
        }

        return errorDetected
    }

    // Obtener hora de internet en UTC
    private suspend fun fetchInternetTime(): Long {
        return try {
            val url = URL("http://worldtimeapi.org/api/timezone/Etc/UTC")
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 3000
            connection.readTimeout = 3000
            connection.requestMethod = "GET"
            connection.connect()
            val stream = connection.inputStream.bufferedReader().use { it.readText() }
            val json = JSONObject(stream)
            json.getLong("unixtime") * 1000L // convertir a ms
        } catch (e: Exception) {
            System.currentTimeMillis() // fallback si no hay internet
        }
    }
}

@Composable
fun ViewContainer(){
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val statsViewModel: HomeStatsViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isLoading = statsViewModel.userData.userName == null

    Scaffold(
        topBar = {
            if (!isLoading && currentRoute != "new_user" )
                ToolBar()},
        bottomBar = {
            if (currentRoute == "home" || currentRoute == "ranking") {
                BottomBar(navController = navController)
            } },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                AppNavigation(navController, statsViewModel,authViewModel)
            }
        }
    )
}

@Composable
fun AppNavigation(navController: NavHostController, statsViewModel: HomeStatsViewModel, authViewModel: AuthViewModel) {

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
            startDestination = if (!statsViewModel.userData.userName.isNullOrEmpty()) "home" else "new_user"
        ){
            composable("home"){HomeScreen(navController, statsViewModel, authViewModel)}
            composable(Screen.FirstGame.route){FirstGame(navController, statsViewModel = statsViewModel)}
            composable(Screen.SecondGame.route){SecondGame(navController, statsViewModel = statsViewModel)}
            composable(Screen.ThirdGame.route){ThirdGame(navController, statsViewModel = statsViewModel) }
            composable(Screen.FourthGame.route){FourthGame(navController, statsViewModel = statsViewModel)}
            composable(Screen.FiftGame.route){FiftGame(navController, statsViewModel = statsViewModel)}
            composable(Screen.SixthGame.route){SixthGame(navController, statsViewModel = statsViewModel)}
            composable(Screen.SeventhGame.route){ SeventhGame(navController, statsViewModel = statsViewModel) }
            composable(Screen.RankingScreen.route){ RankingScreen() }
            composable("new_user"){ NewUserScreen(navController, statsViewModel) }
        }
    }

}
