package com.arturlasok.webapp.feature_auth.presentation.auth_profile

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_auth.R
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.webapp.feature_auth.presentation.components.UserMessageButton
import com.arturlasok.webapp.feature_auth.presentation.components.UserMessageType
import com.arturlasok.webapp.feature_auth.presentation.components.UserShortMessage
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileVerificationMessage(
    checkVerification:() ->Unit,
    sendVerificationMail:() ->Unit,
    fbAuth: FirebaseAuth,
    profileIsVerified: Boolean,
    verificationMailButtonEnabled: MutableState<Boolean>,
    verificationMailButtonVisible: MutableState<Boolean>,
    verificationCheckButtonEnabled: Boolean,
) {


    if(fbAuth.currentUser?.isEmailVerified == false && !profileIsVerified) {
        Spacer(modifier = Modifier.height(8.dp))
        fbAuth.currentUser?.let { user->
            UserShortMessage(
                modifier =  Modifier.padding(start = 20.dp, end = 20.dp),
                messageFromToAvailable = false,
                messageType = UserMessageType.IMPORTANT,
                messageFrom = "ADMIN",
                messageTo = user,
                messageText = UiText.StringResource(R.string.auth_afterreg_notregistered, "").asString()
            ) {
                //buttons
                UserMessageButton(
                    buttonEnabled = verificationMailButtonEnabled.value,
                    buttonVisible = verificationMailButtonVisible.value,
                    messageType =  UserMessageType.IMPORTANT,
                    buttonText =  UiText.StringResource(R.string.auth_sendVerMail, "").asString(),
                    textPadding = 0.dp,
                    buttonAction = { sendVerificationMail() },
                    modifier = Modifier.padding(start = 12.dp, bottom = 6.dp, end = 6.dp)
                )
                UserMessageButton(
                    buttonEnabled = verificationCheckButtonEnabled,
                    buttonVisible = true,
                    messageType =  UserMessageType.IMPORTANT,
                    buttonText =  UiText.StringResource(R.string.auth_verify, "").asString(),
                    textPadding = 0.dp,
                    buttonAction = { checkVerification() },
                    modifier = Modifier.padding(start = 12.dp, bottom = 6.dp, end = 6.dp)
                )

            }
        }
    }
}