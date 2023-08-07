package com.arturlasok.feature_core.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DefaultSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss:() -> Unit
){
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar =  { data ->
            Snackbar(
                backgroundColor = if(data.message.substringBefore(">>>")=="error") { MaterialTheme.colors.error } else
                {MaterialTheme.colors.primary},
                modifier = Modifier.padding(start =4.dp,end = 4.dp,top = 4.dp, bottom = 4.dp),
                content = {
                    Text(
                        text = data.message.substringAfter(">>>"),
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.onError,
                        maxLines = 5
                    )
                },
                action = {
                    data.actionLabel?.let { actionLabel ->
                        Button(
                            onClick = { onDismiss() },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.White
                            )

                        ) {

                            Text(
                                text = actionLabel,
                                style = MaterialTheme.typography.h4,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                elevation = 10.dp
            )
        },
        modifier = modifier
    )
}