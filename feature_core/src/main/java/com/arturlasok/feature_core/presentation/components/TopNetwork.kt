package com.arturlasok.feature_core.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.SignalCellularConnectedNoInternet0Bar
import androidx.compose.material.icons.filled.SignalWifiStatusbarNull
import androidx.compose.material.icons.outlined.CloudDone
import androidx.compose.material.icons.outlined.SignalWifiStatusbarNull
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.CloudDone
import androidx.compose.material.icons.rounded.SignalWifiStatusbarNull
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_core.R
import com.arturlasok.feature_core.util.UiText

@Composable
fun TopNetwork(isNetworkAvailable: Boolean) {
    IconButton(
        onClick = { },
        modifier = Modifier.padding(0.dp)
    ) {
        if(isNetworkAvailable) {
            Icon(
                Icons.Rounded.CloudDone,
                UiText.StringResource(R.string.core_networkAvailable,"asd").asString(),
                tint = MaterialTheme.colors.onSurface,
            )
        }
        else
        {
            Icon(
                Icons.Rounded.Cloud,
                UiText.StringResource(R.string.core_networkNotAvailable,"asd").asString(),
                tint = Color.Red,
            )
        }


    }


}