package com.arturlasok.webapp

import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
//import com.arturlasok.feature_core.datastore.DataStoreInteraction
//import com.arturlasok.feature_core.presentation.start_screen.StartViewModel
//import com.arturlasok.feature_core.util.SavedStateHandlerInteraction
//import com.arturlasok.feature_core.util.TAG
//import com.arturlasok.feature_core.util.isOnline
import com.arturlasok.webapp.navigation.NavigationComponent
import com.arturlasok.webapp.ui.theme.WebAppTheme
import com.arturlasok.webapp.util.MainViewModel
import com.arturlasok.webapp.util.TAG
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
    //@Inject
    //lateinit var isOnline: isOnline
    //DataStore interaction
   // @Inject
   // lateinit var dataStoreInteraction: DataStoreInteraction
    //Firebase auth
    private lateinit var auth: FirebaseAuth
    //vm
    private val viewModel : MainViewModel by viewModels()

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        Log.i(TAG,">>>>> Aplikacja restore state")
        if (savedInstanceState != null) {
            Log.i(TAG,savedInstanceState.keySet().joinToString { it })
        }
        if (persistentState != null) {
            Log.i(TAG,persistentState.keySet().joinToString { it })
        }

        super.onRestoreInstanceState(savedInstanceState, persistentState)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        Log.i(TAG,">>>>> Aplikacja save state")
        if (outState != null) {

            Log.i(TAG,outState.keySet().joinToString { it })
        }
        if (outPersistentState != null) {
            Log.i(TAG,outPersistentState.keySet().joinToString { it })
        }
        super.onSaveInstanceState(outState, outPersistentState)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //gradient background colors for selected theme
        lateinit var selectedBackgroundGradient : Brush
        val darkThemeIntFromDataStore = mutableStateOf(0)
       // lifecycleScope.launch {
        //    lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
              // dataStoreInteraction.getDarkThemeInt().onEach {darkValue->
                //   darkThemeIntFromDataStore.value = darkValue
             //  }.launchIn(this)
            //}
       // }
        //run monitor
       // isOnline.runit()
        // Initialize Firebase Auth
        auth = Firebase.auth
        Log.i(TAG,">>>>> Aplikacja oncreate")

        setContent {
            //navController
            val navController = rememberNavController()
            //padding top when landscape only
            /*
            var statusBarPaddingTop = 0
            var statusBarPaddingLeft = 6
            if(this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                WindowCompat.setDecorFitsSystemWindows(window, true)
                statusBarPaddingTop = 0
                statusBarPaddingLeft = 6
            }

             */
            //accompanist for system bars controller
            val systemUiController = rememberSystemUiController()
            val ts by viewModel.newMainState.collectAsState()
            //app theme
            WebAppTheme(
                systemUiController = systemUiController,
                setBackgroundGradient = { brush -> selectedBackgroundGradient = brush },
                darkTheme = darkThemeIntFromDataStore.value
                ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)

                ) {
                    Text(text = "Main SSH keys: "+viewModel.getStateInfo(), modifier = Modifier.clickable(onClick = {
                        viewModel.setMain("MAIN!!!")
                    }))
                    Text("main test from vm: ${ts.mainV}")                        //if (savedInstanceState != null) {
                       //     Text("TEST from saveinstanstat:"+savedInstanceState.getString("test"))
                       // } else { Text("not saved!") }
/*
                        NavigationComponent(
                            navHostController = navController,
                            modifierTopBar = Modifier
                                .background(MaterialTheme.colors.secondary)
                                .fillMaxWidth()
                                .padding(start = statusBarPaddingLeft.dp),
                            modifierScaffold = Modifier.padding(start = statusBarPaddingLeft.dp, top = statusBarPaddingTop.dp)
                        )




 */

                }
            }
        }
    }
}


