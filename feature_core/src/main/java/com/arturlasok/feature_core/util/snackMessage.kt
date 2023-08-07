package com.arturlasok.feature_core.util

import androidx.compose.material.ScaffoldState
import kotlinx.coroutines.launch

fun snackMessage(
    snackType: SnackType,
    message: String,
    actionLabel: String,
    snackbarController: SnackbarController,
    scaffoldState: ScaffoldState
) {
    snackbarController.getScope().launch {


        launch {

            snackbarController.showSnackbar(
                scaffoldState = scaffoldState,
                message = if(snackType==SnackType.ERROR) {
                    "error>>>$message"
                } else { "normal>>>$message" },
                actionLabel = actionLabel
            )

        }
    }

}
enum class SnackType {
    ERROR,NORMAL
}