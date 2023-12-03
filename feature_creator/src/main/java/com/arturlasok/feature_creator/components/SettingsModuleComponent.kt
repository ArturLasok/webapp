package com.arturlasok.feature_creator.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_creator.model.ControllerType
import com.arturlasok.feature_creator.model.SettingsModuleState

@Composable
fun SettingsModuleComponent(
    settingsModuleStateList: List<SettingsModuleState>,
    dataStoreDarkTheme:Boolean,
    returnSettingsState:(settingState: SettingsModuleState)->Unit,
) {
    Surface(
        modifier = Modifier.padding(start = 2.dp, end = 12.dp),
        elevation = 3.dp,
        shape = MaterialTheme.shapes.medium,
        color = if(dataStoreDarkTheme) {
            MaterialTheme.colors.background} else { Color(0xFFEEEEEE)
        },

        ) {
        Column(modifier = Modifier
            .padding(start = 10.dp)
            .fillMaxWidth()) {
            settingsModuleStateList.onEach { settingsElement->

                    when (settingsElement.componentControllerType) {
                        is ControllerType.SWITCH -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = UiText.StringResource(
                                        settingsElement.componentRes,
                                        "asd"
                                    ).asString(),
                                    style = MaterialTheme.typography.h4,
                                    color = MaterialTheme.colors.primary
                                )
                                //Spacer(modifier = Modifier.width(60.dp))
                                Switch(
                                    checked = settingsElement.controllerValue.toBoolean(),
                                    onCheckedChange = {
                                        returnSettingsState(
                                            settingsElement.copy(controllerValue = it.toString())
                                        )
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedTrackColor = MaterialTheme.colors.surface,
                                        uncheckedTrackColor =
                                        if (dataStoreDarkTheme) {
                                            Color.Gray
                                        } else {
                                            Color.DarkGray
                                        }

                                    )
                                )
                            }
                        }
                        is ControllerType.SLIDER -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.width(50.dp)) {
                                    Text(text=UiText.StringResource(
                                        settingsElement.componentRes, "asd").asString(),
                                        style = MaterialTheme.typography.h4, color = MaterialTheme.colors.primary)
                                    Text(text=settingsElement.controllerValue.toFloat().toString(),
                                        style = MaterialTheme.typography.h4, color = MaterialTheme.colors.primary)
                                }

                                Spacer(modifier = Modifier.width(0.dp))
                                Slider(
                                    value = settingsElement.controllerValue.toFloat(),
                                    onValueChange = {
                                        returnSettingsState(settingsElement.copy(controllerValue = it.toInt().toString()))

                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    valueRange = settingsElement.componentControllerType.min.toFloat() ..settingsElement.componentControllerType.max.toFloat(),
                                    //steps = settingsElement.componentControllerType.step,
                                    onValueChangeFinished = { },
                                    colors = SliderDefaults.colors(),
                                )
                            }

                        }
                        is ControllerType.GROUP -> {
                            Text(
                                text = UiText.StringResource(
                                    settingsElement.componentRes,
                                    "asd"
                                ).asString(),
                                style = MaterialTheme.typography.h4,
                                color = MaterialTheme.colors.primary
                            )
                        }
                    }
            }
        }
    }
}