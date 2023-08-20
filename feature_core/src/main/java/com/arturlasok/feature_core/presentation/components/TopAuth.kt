package com.arturlasok.feature_core.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arturlasok.feature_core.R
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.util.UiText
import com.google.firebase.auth.FirebaseAuth

@Composable
fun TopAuth(
    navigateTo: (route: String) -> Unit,
    fireAuth: FirebaseAuth,
) {
    IconButton(
        onClick = {
            if(fireAuth.currentUser!=null) {
                navigateTo(Screen.ProfileScreen.route)
            } else {
                navigateTo(Screen.AuthScreen.route)
            }

        },
        modifier = Modifier.padding(0.dp)
    ) {
        if(fireAuth.currentUser!=null) {
            UserLogoCircle(
                letter = fireAuth.currentUser!!.email?.substring(0,1) ?: "@" ,
                letterSize = 16.sp,
                color = "#FFEDE7F6",
                colorsecond = "#FFEDE7F6",
                size = 24
            )
        } else {
            Icon(
                Icons.Filled.Face, UiText.StringResource(R.string.core_user,"asd").asString(),
                tint = MaterialTheme.colors.onSurface,
            )   
        }
       

    }

}