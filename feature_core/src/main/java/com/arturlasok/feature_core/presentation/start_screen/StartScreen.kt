package com.arturlasok.feature_core.presentation.start_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.feature_core.R
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.components.TopAuth
import com.arturlasok.feature_core.presentation.components.TopBack
import com.arturlasok.feature_core.presentation.components.TopNetwork

import com.arturlasok.feature_core.presentation.components.TopSettings

@Composable
fun StartScreen(
    startViewModel: StartViewModel = hiltViewModel(),
    navigateTo: (route: String) -> Unit,
    navigateUp:()->Unit,
    navScreenLabel: String = "",
    modifierTopBar: Modifier,
    modifierScaffold: Modifier
) {

    val dataStoreDarkTheme = startViewModel.darkFromStore().collectAsState(initial = 0)
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost =  { scaffoldState.snackbarHostState },
        topBar = {
            //Back button //Top menu
            Row(modifierTopBar,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween)
            {

               //Front
                Row{
                    TopBack(isHome = true, isSecondScreen = false,isInDualMode = false,routeLabel = navScreenLabel, onBack = { navigateUp() })
                    { navigateTo(Screen.StartScreen.route) }
                }

                //End
                Row {
                    TopAuth(navigateTo = { route -> navigateTo(route) }, fireAuth = startViewModel.getFireAuth(), notViewed = startViewModel.numberNotViewedMessages().collectAsState(initial = 0).value )
                    TopSettings( navigateTo = { route-> navigateTo(route)})
                    TopNetwork(isNetworkAvailable = startViewModel.haveNetwork())
                }


            }
        },
        bottomBar ={
            Text("Bottom MENU")
        }
    ) { paddingValues ->
        //content
        Box(modifier = modifierScaffold
            .padding(paddingValues)
            .fillMaxSize()) {


           Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
               modifier = Modifier
               .padding(0.dp)
               .fillMaxSize()) {

                   Image(
                       bitmap = ImageBitmap.imageResource(
                           id = R.drawable.website1
                       ),
                       modifier = Modifier
                           .size(
                               256.dp,
                               256.dp
                           )
                           .padding(bottom = 2.dp)
                           .alpha(0.9f),
                       contentDescription = "Zablokowane",

                   )
               /*

                */
                Text("Create You Web Site!", style = MaterialTheme.typography.h1)
           }


        }

    }


}