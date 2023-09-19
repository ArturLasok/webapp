package com.arturlasok.webapp.feature_auth.presentation.auth_profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_core.R
import com.arturlasok.feature_core.data.datasource.api.model.WebProject
import com.arturlasok.feature_core.domain.model.StartProjectsInteractionState
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.start_screen.OneProjectButton
import com.arturlasok.feature_core.util.UiText
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileProjectsView(
    navigateTo: (route: String) -> Unit,
    navigateUp: () -> Unit,
    allProjects: Pair<MutableState<List<WebProject>>, MutableState<StartProjectsInteractionState>>,
    haveNetwork :Boolean

) {
    when(allProjects.second.value) {

        is StartProjectsInteractionState.OnComplete -> {
            FlowRow(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                allProjects.first.value.onEach { project ->

                    OneProjectButton(
                        navigateTo = { route -> navigateTo(route) },
                        navigateUp = { navigateUp() },
                        project = project,
                    )
                }
                //add next project
                OneProjectButton(
                    navigateTo = { route -> navigateTo(route) },
                    navigateUp = { navigateUp() },
                    project = WebProject(),
                )
            }

        }
        is StartProjectsInteractionState.Interact -> {

            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally)
            {
                LinearProgressIndicator(color = MaterialTheme.colors.primary, modifier = Modifier.height(2.dp))
            }


        }
        is StartProjectsInteractionState.Error -> {

            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally)
            {
                val anim = remember { mutableStateOf(0) }
                LaunchedEffect(key1 = true) {
                    delay(100)
                    anim.value = 1
                }
                AnimatedVisibility(
                    visible = anim.value == 1,
                    exit = fadeOut(
                        animationSpec = tween(delayMillis = 500)
                    ),
                    enter = fadeIn(
                        animationSpec = tween(
                            delayMillis = 500,
                            easing = FastOutSlowInEasing,
                            durationMillis = 1000
                        )
                    )
                ) {
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        Text(
                            modifier = Modifier.clickable(onClick = {
                                navigateTo(Screen.StartScreen.route)
                            }),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h2,
                            color = MaterialTheme.colors.onSurface,
                            text = if (haveNetwork) {
                                (allProjects.second.value as StartProjectsInteractionState.Error).message
                            } else {
                                UiText.StringResource(R.string.core_nonetwork, "asd")
                                    .asString()
                            },

                            )
                        IconButton(
                            onClick = { navigateTo(Screen.ProfileScreen.route) },
                            modifier = Modifier
                                .padding(0.dp)
                                .width(64.dp)
                        ) {
                            Icon(
                                Icons.Filled.Refresh,
                                UiText.StringResource(R.string.core_refresh, "asd")
                                    .asString(),
                                tint = MaterialTheme.colors.onSurface,
                            )

                        }
                        Text(
                            color = MaterialTheme.colors.onSurface,
                            text = UiText.StringResource(
                                R.string.core_refresh,
                                "asd"
                            ).asString().uppercase(),
                            style = MaterialTheme.typography.h2,
                            modifier = Modifier.clickable(onClick = {
                                navigateTo(Screen.StartScreen.route)
                            })
                        )
                    }

                }
            }
        }
        is StartProjectsInteractionState.Empty ->
        {
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable(onClick = {
                        navigateTo(Screen.AddProjectScreen.route)
                    })
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
                    contentDescription = "Logo",

                    )
                Text("Create your own website now!", style = MaterialTheme.typography.h1)
            }


        }
        else -> {
            //nothing
        }

    }
}