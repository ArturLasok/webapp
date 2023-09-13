package com.arturlasok.webapp.feature_auth.presentation.auth_profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_auth.R
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.webapp.feature_auth.presentation.components.UserMessageType
import com.arturlasok.webapp.feature_auth.presentation.components.UserShortMessage
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileFirstLoginMessage(
    profileFirstLogin: Boolean,
    fbAuth: FirebaseAuth
) {
    if(profileFirstLogin) {
        Surface(shape = MaterialTheme.shapes.medium, elevation = 20.dp, color = MaterialTheme.colors.background, modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 16.dp).padding(top = 0.dp)) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Spacer(modifier = Modifier.height(8.dp))
                fbAuth.currentUser?.let { user->
                    UserShortMessage(
                        modifier =  Modifier.padding(start = 0.dp, end = 0.dp),
                        messageFromToAvailable = false,
                        messageType = UserMessageType.NORMAL,
                        messageFrom = "ADMIN",
                        messageTo = user,
                        messageText = UiText.StringResource(R.string.auth_afterreg_info, "").asString()
                    ) {
                        //buttons

                    }
                }
            }
        }

    }


}