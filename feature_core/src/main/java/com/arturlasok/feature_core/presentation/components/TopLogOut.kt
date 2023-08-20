package com.arturlasok.feature_core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_core.R
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.util.UiText
import com.google.firebase.auth.FirebaseAuth

@Composable
fun TopLogOut(navigateTo: (route: String) -> Unit,firebaseAuth: FirebaseAuth) {
    val openDialog = rememberSaveable { mutableStateOf(false) }
    DefaultAlert(
        onDismiss = { openDialog.value = false },
        title = "",
        text = UiText.StringResource(R.string.core_logout_question,"asd").asString(),
        buttons = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround)
            {

                AlertButton(
                    buttonText = UiText.StringResource(R.string.core_yes,"asd").asString(),
                    textPadding = 2.dp,
                    buttonAction = {
                        openDialog.value = false;
                        firebaseAuth.signOut();
                        navigateTo(Screen.StartScreen.routeWithArgs+"?logOut=true&email=none")
                                   },
                    modifier = Modifier
                )
                AlertButton(
                    buttonText = UiText.StringResource(R.string.core_no,"asd").asString(),
                    textPadding = 2.dp,
                    buttonAction = { openDialog.value = false },
                    modifier = Modifier
                )

            }
        },
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        alertOpen = openDialog.value,
        changeAlertState = {}
    )
    //
    IconButton(
        onClick = { openDialog.value = true },
        modifier = Modifier.padding(0.dp)
    ) {
        Icon(
            Icons.Filled.Logout,
            UiText.StringResource(R.string.core_settings,"asd").asString(),
            tint = MaterialTheme.colors.onSurface,
        )

    }

}