package com.arturlasok.webapp.ui.mobileTokenVerification

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_core.R
import com.arturlasok.feature_core.presentation.components.AlertButton
import com.arturlasok.feature_core.presentation.components.DefaultAlert
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.webapp.model.UserMobileCheckState

@Composable
fun MobileTokenVerification(
    mobileCheckState: UserMobileCheckState,
    changeStateMobileCheckState:(newState: UserMobileCheckState) -> Unit) {
    when(mobileCheckState) {

        is UserMobileCheckState.Same -> {
           //do nothing
        }
        is UserMobileCheckState.SameButBlocked -> {
            DefaultAlert(
                onDismiss = { },
                title = "",
                text = UiText.StringResource(R.string.core_accountBlocked, "asd")
                    .asString(),
                buttons = {  },
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                alertOpen = true,
                changeAlertState = {}
            )
        }
        is UserMobileCheckState.NotTheSame -> {
            DefaultAlert(
                onDismiss = {  },
                title = "",
                text = UiText.StringResource(R.string.core_oteherDeviceInfo, "asd").asString(),
                buttons = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround)
                    {

                        AlertButton(
                            buttonText = UiText.StringResource(R.string.core_otherDeviceStay, "asd")
                                .asString(),
                            textPadding = 2.dp,
                            buttonAction = {
                                changeStateMobileCheckState(UserMobileCheckState.Stay)
                            },
                            modifier = Modifier
                        )
                        AlertButton(
                            buttonText =  UiText.StringResource(
                                R.string.core_otherDeviceLogOUt,
                                "asd"
                            ).asString(),
                            textPadding = 2.dp,
                            buttonAction = {
                                changeStateMobileCheckState(UserMobileCheckState.LogOut)
                            },
                            modifier = Modifier
                        )
                    }
                          },
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                alertOpen = true,
                changeAlertState = { }
            )
        }
        is UserMobileCheckState.NotTheSameAndBlocked -> {
            DefaultAlert(
                onDismiss = { },
                title = "",
                text = UiText.StringResource(R.string.core_accountBlocked, "asd")
                    .asString(),
                buttons = {  },
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                alertOpen = true,
                changeAlertState = {}
            )
        }
        else -> {

        }
    }

}