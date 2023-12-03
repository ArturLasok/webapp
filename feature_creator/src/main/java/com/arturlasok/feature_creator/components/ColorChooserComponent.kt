package com.arturlasok.feature_creator.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Brightness1
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_creator.model.ColorModuleState
import com.arturlasok.feature_creator.model.iconsDataForColorPalette

@OptIn(ExperimentalLayoutApi::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun ColorChooserComponent(
    dataStoreDarkTheme: Boolean,
    returnColor:(colorModuleSate: ColorModuleState) ->Unit,
    setChip:(cms: String) ->Unit,
    selectedChip: MutableState<ColorModuleState>,
    chips: List<Pair<String, String>>,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        modifier = Modifier.padding(start = 2.dp, end = 12.dp),
        elevation = 3.dp,
        shape = MaterialTheme.shapes.medium,
        color = if(dataStoreDarkTheme) {
            MaterialTheme.colors.background} else { Color(0xFFEEEEEE)
        },

        ) {
        Column(modifier = Modifier.padding(6.dp)) {
            FlowRow(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                    chips.onEach {oneChip->


                        val chipColor = if (selectedChip.value.componentId == oneChip.first) {
                            if(dataStoreDarkTheme) {
                                MaterialTheme.colors.primary
                            }else{
                                MaterialTheme.colors.primary
                            }

                        } else {
                            if(dataStoreDarkTheme) {
                                Color.DarkGray
                            }
                            else{
                                Color.LightGray
                            }

                        }
                        Chip(
                            enabled = true,
                            colors = ChipDefaults.chipColors(
                                disabledBackgroundColor = MaterialTheme.colors.background,
                                disabledContentColor = Color.LightGray,
                                backgroundColor = chipColor,
                                contentColor = if(selectedChip.value.componentId == oneChip.first) {
                                    Color.White
                                } else {
                                    MaterialTheme.colors.onPrimary
                                }
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (selectedChip.value.componentId.isEmpty()) {
                                    MaterialTheme.colors.error
                                } else {
                                    if(dataStoreDarkTheme){
                                        Color.DarkGray
                                    }
                                    else
                                    {
                                        Color.LightGray
                                    }

                                }
                            ),
                            onClick = {
                                keyboardController?.hide(); focusManager.clearFocus(true)
                                setChip(oneChip.first)
                            },
                        ) {
                            Text(
                                oneChip.second,
                                style = MaterialTheme.typography.h5
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                    }

            }
            //R
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.width(40.dp)) {
                    Text(text="RED", style = MaterialTheme.typography.h4, color = MaterialTheme.colors.primary)
                    Text(text=selectedChip.value.r.toString(), style = MaterialTheme.typography.h4, color = MaterialTheme.colors.primary)
                }

                Spacer(modifier = Modifier.width(8.dp))
                Slider(
                    value = selectedChip.value.r.toFloat(),
                    onValueChange = {
                        returnColor(selectedChip.value.copy(r = it.toInt()))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    valueRange = 0f..255f,
                    //steps = 255,
                    onValueChangeFinished = { },

                    colors = SliderDefaults.colors(),
                )
            }
                
        
            //G
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.width(40.dp)) {
                    Text(
                        text = "GREEN",
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.primary
                    )
                    Text(text=selectedChip.value.g.toString(), style = MaterialTheme.typography.h4, color = MaterialTheme.colors.primary)
                }

                Spacer(modifier = Modifier.width(8.dp))
                Slider(
                    value = selectedChip.value.g.toFloat(),
                    onValueChange = {
                        returnColor(selectedChip.value.copy(g = it.toInt()))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    valueRange = 0f..255f,
                    //steps = 255,
                    onValueChangeFinished = { },
                    colors = SliderDefaults.colors(),
                )
            }
            //B
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.width(40.dp)) {
                    Text(
                        text = "BLUE",
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.primary
                    )
                    Text(text=selectedChip.value.b.toString(), style = MaterialTheme.typography.h4, color = MaterialTheme.colors.primary)
                }

                Spacer(modifier = Modifier.width(8.dp))
                Slider(
                    value = selectedChip.value.b.toFloat(),
                    onValueChange = {
                        returnColor(selectedChip.value.copy(b = it.toInt()))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    valueRange = 0f..255f,
                    //steps = 255,
                    onValueChangeFinished = { },
                    colors = SliderDefaults.colors(),
                )
            }
            //A
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.width(40.dp)) {
                    Text(
                        text = "ALPHA",
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.primary
                    )
                    Text(
                        text=selectedChip.value.alpha.toString(),
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.primary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Slider(
                    value = selectedChip.value.alpha.toFloat(),
                    onValueChange = {
                        returnColor(selectedChip.value.copy(alpha = it.toInt()))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    valueRange = 0f..255f,
                    //steps = 255,
                    onValueChangeFinished = { },
                    colors = SliderDefaults.colors(),
                )
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                Icon(
                    modifier = Modifier.clickable(onClick = {
                        returnColor(
                            ColorModuleState(
                                0,
                                0,
                                0,
                                0,
                                selectedChip.value.componentId
                            )
                        )
                    }),
                    imageVector = Icons.Filled.Block,
                    contentDescription ="TRANSPARENT",
                    tint = Color.DarkGray
                )
                Spacer(modifier = Modifier.width(10.dp))
                iconsDataForColorPalette().onEach {icons->
                    Icon(
                        modifier = Modifier.clickable(onClick = {
                            returnColor(
                            ColorModuleState(
                                icons.r,
                                icons.g,
                                icons.b,
                                icons.alpha,
                                selectedChip.value.componentId
                            )
                            )
                        }),
                        imageVector = Icons.Filled.Brightness1,
                        contentDescription =icons.componentId, tint = Color(icons.r,icons.g,icons.b,icons.alpha)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }

            }
        }

    }
}