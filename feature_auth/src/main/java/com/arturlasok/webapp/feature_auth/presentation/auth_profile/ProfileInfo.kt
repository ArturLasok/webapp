package com.arturlasok.webapp.feature_auth.presentation.auth_profile

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arturlasok.feature_core.presentation.components.UserLogoCircle
import com.arturlasok.webapp.feature_auth.model.ProfileDataState
import com.arturlasok.webapp.feature_auth.model.ProfileInteractionState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun ProfileInfo(
    darkTheme: Int,
    fbAuth: FirebaseAuth,
    profileDataState: ProfileDataState,
) {
    val isDark: Boolean = when (darkTheme) {
        0 -> {
            isSystemInDarkTheme()
        }

        1 -> {
            false
        }

        2 -> {
            true
        }

        else -> {
            false
        }
    }
    Surface(
        shape = MaterialTheme.shapes.medium,
        elevation = 20.dp,
        color = MaterialTheme.colors.background,
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
            .fillMaxWidth()
            .padding(top = 0.dp)
    ) {
        val anim = remember { mutableStateOf(0) }
        LaunchedEffect(key1 = true){
            delay(100)
            anim.value = 1
        }
        AnimatedVisibility(
            visible = profileDataState.profileInfoInteractionState.value == ProfileInteractionState.OnComplete && anim.value==1,
            exit = fadeOut(
                animationSpec = tween(delayMillis = 1000)
            ),
            enter = fadeIn(
                animationSpec = tween(delayMillis = 0,easing = FastOutSlowInEasing, durationMillis = 1000)
            )
        ) {

            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(
                        Modifier
                            .height(20.dp)
                            .fillMaxWidth()
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Spacer(
                            Modifier.width(20.dp)
                        )
                        UserLogoCircle(
                            letter = profileDataState.profileMail.first().toString(),
                            letterSize = ((LocalConfiguration.current.screenWidthDp / 5) - (LocalConfiguration.current.screenWidthDp / 7)).sp,
                            color = if (isDark) {
                                MaterialTheme.colors.primaryVariant
                            } else {
                                MaterialTheme.colors.onSurface
                            },
                            colorsecond = if (isDark) {
                                MaterialTheme.colors.primaryVariant
                            } else {
                                MaterialTheme.colors.onSurface
                            },
                            size = LocalConfiguration.current.screenWidthDp / 9
                        )
                        Spacer(
                            Modifier
                                .height(15.dp)
                                .width(15.dp)
                        )

                        Column {
                            Text(
                                text = profileDataState.profileMail,
                                style = MaterialTheme.typography.h3,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "\nCountry: ${profileDataState.profileCountry}",
                                style = MaterialTheme.typography.h5
                            )
                            Text(
                                text = "Language: ${profileDataState.profileLang}",
                                style = MaterialTheme.typography.h5
                            )
                        }


                    }
                    Spacer(
                        Modifier
                            .height(15.dp)
                            .fillMaxWidth()
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(20.dp))

                    UserLogoCircle(
                        letter = profileDataState.profileMail.first().toString(),
                        letterSize = ((LocalConfiguration.current.screenWidthDp / 3) - (LocalConfiguration.current.screenWidthDp / 7)).sp,
                        color = if (isDark) {
                            MaterialTheme.colors.primaryVariant
                        } else {
                            MaterialTheme.colors.onSurface
                        },
                        colorsecond = if (isDark) {
                            MaterialTheme.colors.primaryVariant
                        } else {
                            MaterialTheme.colors.onSurface
                        },
                        size = LocalConfiguration.current.screenWidthDp / 3
                    )
                    Spacer(Modifier.height(15.dp))
                    Text(
                        text = profileDataState.profileMail,
                        style = MaterialTheme.typography.h3,
                        fontWeight = FontWeight.Bold
                    )
                    Row {
                        Text(
                            text = "Country: ${profileDataState.profileCountry}",
                            style = MaterialTheme.typography.h5
                        )
                        Text(text = "  ")
                        Text(
                            text = "Language: ${profileDataState.profileLang}",
                            style = MaterialTheme.typography.h5
                        )
                    }


                    Spacer(Modifier.height(15.dp))

                }
            }
        }


    AnimatedVisibility(
        visible = profileDataState.profileInfoInteractionState.value == ProfileInteractionState.Idle,

        enter = fadeIn(
            animationSpec = tween(delayMillis = 500)
        )
    ) {


        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(20.dp))
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                strokeWidth = 1.dp,
                //backgroundColor = Color.DarkGray,
                modifier = Modifier.width((40.dp))
            )
            Spacer(Modifier.height(20.dp))
        }
    }

    }
}