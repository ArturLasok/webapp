package com.arturlasok.feature_creator.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_creator.model.ColorModuleState
import com.arturlasok.feature_creator.model.LinkModuleState
import com.arturlasok.feature_creator.model.ModuleDataState
import com.arturlasok.feature_creator.model.SettingsModuleState
import com.arturlasok.feature_creator.model.convertCssRGBAtoComposeRGBA
import com.arturlasok.feature_creator.util.ActionTextModule
import com.arturlasok.feature_creator.util.ActionTextState
import com.arturlasok.feature_creator.util.TextStringAlignToComposeTextAlign
import com.arturlasok.feature_creator.util.TextStringSizeToComposeTextSize
import com.arturlasok.feature_creator.util.TextStringUnderlineToComposeTextUnderline
import com.arturlasok.feature_creator.util.TextStringWeightToComposeTextWeight


@Composable
fun TextModuleComponent(
    updateOpenModuleId: (id: String) -> Unit,
    moduleDataState: ModuleDataState,
    setOpenTextModuleText: (text: String) -> Unit,
    setOpenTextModuleAction: (action: ActionTextState) -> Unit,
    setOpenTextModuleColorAction: (colorModuleState:ColorModuleState) -> Unit,
    setOpenTextModuleSettings:(settingsModuleState: SettingsModuleState) ->Unit,
    setOpenTextModuleLink:(linkModuleState: LinkModuleState) ->Unit,
    makeTextModuleSnack: (textRes: Int) -> Unit,
    editMode: Boolean,
    dataStoreDarkTheme: Boolean,
) {

    if(editMode) {
        BackHandler {
            updateOpenModuleId("")
        }
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

            TextModuleText(
                moduleDataState = moduleDataState,
                setOpenTextModuleText = { text -> setOpenTextModuleText(text) },
                setOpenTextModuleAction = { action -> setOpenTextModuleAction(action) },
                setOpenTextModuleColorAction = { colorState ->  setOpenTextModuleColorAction(colorState) },
                setOpenTextModuleSettings = { settingsModuleState -> setOpenTextModuleSettings(settingsModuleState) },
                setOpenTextModuleLink = { linkModuleState -> setOpenTextModuleLink(linkModuleState)  },
                dataStoreDarkTheme = dataStoreDarkTheme,
                makeTextModuleSnack = {textRes -> makeTextModuleSnack(textRes)  }

            )
        }

    }
    else {
        Box(modifier = Modifier
            .padding(
                top=moduleDataState.projectOpenTextModule.wTextBorderMarginTop.toInt().dp,
                bottom = moduleDataState.projectOpenTextModule.wTextBorderMarginBottom.toInt().dp,
                start = moduleDataState.projectOpenTextModule.wTextBorderMarginStart.toInt().dp,
                end = moduleDataState.projectOpenTextModule.wTextBorderMarginEnd.toInt().dp,
            )
            .border(1.dp, if (moduleDataState.projectOpenTextModule.wTextBorder == "yes") {
                convertCssRGBAtoComposeRGBA(moduleDataState.projectOpenTextModule.wTextBorderColor)
            } else {
                Color.Transparent
            },
                shape = if (moduleDataState.projectOpenTextModule.wTextBackgroundRoundedRectangle == "true") {
                    MaterialTheme.shapes.large
                } else RectangleShape
            )
            .background(convertCssRGBAtoComposeRGBA(moduleDataState.projectOpenTextModule.wTextBackgroundColor)
                , shape =  if (moduleDataState.projectOpenTextModule.wTextBackgroundRoundedRectangle == "true") {
                    MaterialTheme.shapes.large
                } else RectangleShape
            )
        ) {
            Text(
                text = moduleDataState.projectOpenTextModule.wText,
                modifier = Modifier.fillMaxWidth().padding(
                    top = (moduleDataState.projectOpenTextModule.wTextMarginTop.toInt()).dp,
                    bottom = (moduleDataState.projectOpenTextModule.wTextMarginBottom.toInt()).dp,
                    start = (moduleDataState.projectOpenTextModule.wTextMarginStart.toInt()).dp,
                    end = (moduleDataState.projectOpenTextModule.wTextMarginEnd.toInt()).dp
                ),
                color = convertCssRGBAtoComposeRGBA(moduleDataState.projectOpenTextModule.wTextColor),
                fontWeight = TextStringWeightToComposeTextWeight(
                    weight = moduleDataState.projectOpenTextModule.wTextWeight),
                textAlign = TextStringAlignToComposeTextAlign(
                    align = moduleDataState.projectOpenTextModule.wTextAlign),
                fontSize = TextStringSizeToComposeTextSize(
                    size = moduleDataState.projectOpenTextModule.wTextH),
                textDecoration = TextStringUnderlineToComposeTextUnderline(
                    underline = moduleDataState.projectOpenTextModule.wTextDecoration)
            )
        }
        /*
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .border(if(moduleDataState.projectOpenTextModule.wTextBorder=="yes")
            { 1.dp } else { 0.dp },
                if(moduleDataState.projectOpenTextModule.wTextBorder=="yes") {
                convertCssRGBAtoComposeRGBA(moduleDataState.projectOpenTextModule.wTextBorderColor)
                } else { Color.Transparent }

                ,MaterialTheme.shapes.medium)
            .background(convertCssRGBAtoComposeRGBA(moduleDataState.projectOpenTextModule.wTextBackgroundColor),MaterialTheme.shapes.medium)
        ) {
            Text(
                text = moduleDataState.projectOpenTextModule.wText,
                modifier = Modifier.fillMaxWidth().padding(0.dp),
                color = convertCssRGBAtoComposeRGBA(moduleDataState.projectOpenTextModule.wTextColor),

                fontWeight = TextStringWeightToComposeTextWeight(
                    weight = moduleDataState.projectOpenTextModule.wTextWeight),
                textAlign = TextStringAlignToComposeTextAlign(
                    align = moduleDataState.projectOpenTextModule.wTextAlign),
                fontSize = TextStringSizeToComposeTextSize(
                    size = moduleDataState.projectOpenTextModule.wTextH),
                textDecoration = TextStringUnderlineToComposeTextUnderline(
                    underline = moduleDataState.projectOpenTextModule.wTextDecoration)
            )
        }

         */
    }
}
