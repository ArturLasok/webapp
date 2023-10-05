package com.arturlasok.webapp

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.arturlasok.feature_core.datastore.DataStoreInteraction
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.isOnline
import com.arturlasok.webapp.navigation.NavigationComponent
import com.arturlasok.webapp.ui.mobileTokenVerification.MobileTokenVerification
import com.arturlasok.webapp.ui.theme.WebAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    //Internet Monitor
    @Inject
    lateinit var isOnline: isOnline
    //DataStore interaction
     @Inject
     lateinit var dataStoreInteraction: DataStoreInteraction
    //Firebase auth
    private lateinit var auth: FirebaseAuth
    val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //gradient background colors for selected theme
        lateinit var selectedBackgroundGradient: Brush
        var selectedBackgroundTopColor: Color = Color.Transparent

        val darkThemeIntFromDataStore = mutableStateOf(0)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                dataStoreInteraction.getDarkThemeInt().onEach { darkValue ->
                    darkThemeIntFromDataStore.value = darkValue
                }.launchIn(this)
            }
        }
        //run monitor
        isOnline.runit()
        // Initialize Firebase Auth
        auth = Firebase.auth
        auth.useAppLanguage()
        //padding top when landscape only
        var statusBarPaddingTop = 0
        var statusBarPaddingLeft = 0
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            statusBarPaddingTop = 0
            statusBarPaddingLeft = 0
        }

        setContent {

            //navController
            val navController = rememberNavController()
            //accompanist for system bars controller
            val systemUiController = rememberSystemUiController()

            //app theme
            WebAppTheme(
                systemUiController = systemUiController,
                setBackgroundGradient = { brush -> selectedBackgroundGradient = brush },
                setBackgroundTopColor = { color -> selectedBackgroundTopColor= color  },
                darkTheme = darkThemeIntFromDataStore.value
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(selectedBackgroundGradient)


                ) {
                    //check is logged only one one device
                    MobileTokenVerification(mainViewModel.mainUserMobileCheckState.value) { newState ->
                        mainViewModel.mainUserMobileCheckState.value = newState
                    }
                    Log.i(TAG,"Main Recompose")
                    NavigationComponent(
                        changeStateMobileCheckState = {newState -> mainViewModel.mainUserMobileCheckState.value = newState},
                        firebaseAuth = mainViewModel.getFireAuth(),
                        mobileCheckState = mainViewModel.mainUserMobileCheckState.value,
                        checkMobileToken =  mainViewModel::checkMobileToken,
                        setUserStayOnThisDevice = mainViewModel::stayOnThisDevice,
                        navHostController = navController,
                        modifierTopBar = Modifier
                            .background(selectedBackgroundTopColor)
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(
                                start = (statusBarPaddingLeft + 6).dp,
                                end = (statusBarPaddingLeft + 6).dp
                            ),
                        modifierScaffold = Modifier
                            .padding(
                                start = statusBarPaddingLeft.dp,
                                top = statusBarPaddingTop.dp,
                                end = statusBarPaddingLeft.dp
                            )
                            .background(selectedBackgroundGradient)
                    )

                }
            }
        }
    }
}


