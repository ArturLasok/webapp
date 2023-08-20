package com.arturlasok.webapp.feature_auth.presentation.auth_profile

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
        Spacer(modifier = Modifier.height(8.dp))
        fbAuth.currentUser?.let { user->
            UserShortMessage(
                modifier =  Modifier.padding(start = 20.dp, end = 20.dp),
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