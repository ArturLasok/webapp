package com.arturlasok.webapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.arturlasok.feature_core.util.isOnline
import com.arturlasok.webapp.navigation.NavigationComponent
import com.arturlasok.webapp.ui.theme.WebAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

// Datastore init
//val Context.udataStore: DataStore<Preferences> by preferencesDataStore(name = "ustawienia")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    //Internet Monitor
    @Inject
    lateinit var isOnline: isOnline


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //gradient background colors for selected theme
        lateinit var selectedBackgroundGradient : Brush
        val darkThemeIntFromDataStore = mutableStateOf(0)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
               //TODO get theme int
            }
        }
        //run monitor
        isOnline.runit()



        setContent {
            //navController
            val navController = rememberNavController()
            //padding top when landscape only
            var statusBarPaddingTop = 0
            var statusBarPaddingLeft = 6
            if(this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                WindowCompat.setDecorFitsSystemWindows(window, true)
                statusBarPaddingTop = 0
                statusBarPaddingLeft = 6
            }
            //accompanist for system bars controller
            val systemUiController = rememberSystemUiController()
            //app theme
            WebAppTheme(
                systemUiController = systemUiController,
                setBackgroundGradient = { brush -> selectedBackgroundGradient = brush }) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(selectedBackgroundGradient)
                        .padding(start = statusBarPaddingLeft.dp,top = statusBarPaddingTop.dp)
                ) {
                    NavigationComponent(
                        navHostController = navController,
                        internetAvailable = isOnline.isNetworkAvailable.value

                    )

                }
            }
        }
    }
}

