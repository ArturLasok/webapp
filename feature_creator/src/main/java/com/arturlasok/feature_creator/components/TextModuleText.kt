package com.arturlasok.feature_creator.components


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardHide
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_creator.R
import com.arturlasok.feature_creator.model.ColorModuleState
import com.arturlasok.feature_creator.model.ControllerType
import com.arturlasok.feature_creator.model.LinkModuleState
import com.arturlasok.feature_creator.model.LinkType
import com.arturlasok.feature_creator.model.ModuleDataState
import com.arturlasok.feature_creator.model.SettingsModuleState
import com.arturlasok.feature_creator.model.convertCssRGBAPlusComponentIdToColorModuleState
import com.arturlasok.feature_creator.model.convertCssRGBAtoComposeRGBA
import com.arturlasok.feature_creator.util.ActionTextModule
import com.arturlasok.feature_creator.util.ActionTextState
import com.arturlasok.feature_creator.util.IconsForTextModule
import com.arturlasok.feature_creator.util.TextStringAlignToComposeTextAlign
import com.arturlasok.feature_creator.util.TextStringSizeToComposeTextSize
import com.arturlasok.feature_creator.util.TextStringUnderlineToComposeTextUnderline
import com.arturlasok.feature_creator.util.TextStringWeightToComposeTextWeight
import com.arturlasok.feature_creator.util.isActionTextSelectedIcon


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun TextModuleText(
    moduleDataState: ModuleDataState,
    setOpenTextModuleText: (text: String) -> Unit,
    setOpenTextModuleAction: (action: ActionTextState) -> Unit,
    setOpenTextModuleColorAction: (colorState: ColorModuleState) -> Unit,
    setOpenTextModuleSettings:(settingsModuleState: SettingsModuleState) ->Unit,
    setOpenTextModuleLink:(linkModuleState: LinkModuleState) ->Unit,
    makeTextModuleSnack: (textRes: Int) -> Unit,
    dataStoreDarkTheme: Boolean,
    ) {
    val iconsForTextModule = IconsForTextModule().returnIcons()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val textField = remember { mutableStateOf(TextFieldValue(text = moduleDataState.projectOpenTextModule.wText)) }
    val clipboardManager = LocalClipboardManager.current
    val selectedBottomChip = remember { mutableStateOf("Colors") }
    Row() {


        Column(
            modifier = Modifier
                .zIndex(1.0f)

                .padding(start = 2.dp, end = 2.dp, top = 10.dp)
        ) {

            iconsForTextModule.onEach { toolIcon ->

                Column(
                    modifier = Modifier.zIndex(1.0f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = {
                        if (toolIcon.third is ActionTextModule.TEXT_COPY_ALL) {
                            clipboardManager.setText(AnnotatedString(moduleDataState.projectOpenTextModule.wText))

                        } else if (toolIcon.third is ActionTextModule.TEXT_CONTENT_PASTE) {
                            val clipData = clipboardManager.getText()
                            if (clipData != null) {
                                val selectionPoint = textField.value.selection.start
                                textField.value = TextFieldValue(
                                    selection = TextRange(selectionPoint),
                                    text = moduleDataState.projectOpenTextModule.wText.substring(
                                        0,
                                        selectionPoint
                                    ) + clipData.text + moduleDataState.projectOpenTextModule.wText.substring(
                                        selectionPoint
                                    )
                                )
                                setOpenTextModuleText(textField.value.text)
                            }
                        } else {
                            setOpenTextModuleAction(ActionTextState(
                                component = moduleDataState.projectOpenTextModule,
                                componentAction = toolIcon.third,
                            ))
                        }
                    }) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box() {
                                Icon(
                                    imageVector = toolIcon.second,
                                    contentDescription = UiText.StringResource(
                                        toolIcon.first,
                                        "asd"
                                    ).asString(),
                                    tint = isActionTextSelectedIcon(
                                        actionTextModule = toolIcon.third,
                                        textModule = moduleDataState.projectOpenTextModule,
                                    ).second,
                                    modifier = Modifier
                                        .width(24.dp)
                                )
                            }
                            Text(
                                UiText.StringResource(toolIcon.first, "asd").asString(),
                                style = MaterialTheme.typography.h5
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .height(2.dp)
                            .width(1.dp)
                    )
                }

            }

        }
        Column {

            Box(modifier = Modifier
                .padding(
                    top = moduleDataState.projectOpenTextModule.wTextBorderMarginTop.toInt().dp,
                    bottom = moduleDataState.projectOpenTextModule.wTextBorderMarginBottom.toInt().dp,
                    start = moduleDataState.projectOpenTextModule.wTextBorderMarginStart.toInt().dp + 2.dp,
                    end = moduleDataState.projectOpenTextModule.wTextBorderMarginEnd.toInt().dp + 12.dp,
                )
                .border(
                    1.dp, if (moduleDataState.projectOpenTextModule.wTextBorder == "yes") {
                        convertCssRGBAtoComposeRGBA(moduleDataState.projectOpenTextModule.wTextBorderColor)
                    } else {
                        Color.Transparent
                    },
                    shape = if (moduleDataState.projectOpenTextModule.wTextBackgroundRoundedRectangle == "true") {
                        MaterialTheme.shapes.large
                    } else RectangleShape
                )
                .background(
                    convertCssRGBAtoComposeRGBA(moduleDataState.projectOpenTextModule.wTextBackgroundColor),
                    shape = if (moduleDataState.projectOpenTextModule.wTextBackgroundRoundedRectangle == "true") {
                        MaterialTheme.shapes.large
                    } else RectangleShape
                )
            ) {
                TextField(
                    modifier = Modifier
                        .padding(
                            top = (moduleDataState.projectOpenTextModule.wTextMarginTop.toInt() - 10).dp,
                            bottom = (moduleDataState.projectOpenTextModule.wTextMarginBottom.toInt() - 10).dp,
                            start = (moduleDataState.projectOpenTextModule.wTextMarginStart.toInt() - 10).dp,
                            end = (moduleDataState.projectOpenTextModule.wTextMarginEnd.toInt() - 10).dp
                        )
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = if (selectedBottomChip.value.isEmpty()) 350.dp else 0.dp)
                        .minimumInteractiveComponentSize(),
                        //(if (selectedBottomChip.value.isEmpty()) 350.dp else 200.dp),
                    isError = false,
                    value = textField.value,
                    onValueChange = { newInput ->
                        if (newInput.text.length < 22500) {
                            textField.value = newInput
                            setOpenTextModuleText(newInput.text)
                        }
                    },
                    singleLine = false,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor =
                            Color.Transparent,
                        unfocusedLabelColor = MaterialTheme.colors.primary.copy(alpha = 0.7f),
                        ),
                    textStyle = TextStyle.Default.copy(
                        color = convertCssRGBAtoComposeRGBA(moduleDataState.projectOpenTextModule.wTextColor),
                        fontWeight = TextStringWeightToComposeTextWeight(
                            weight = moduleDataState.projectOpenTextModule.wTextWeight
                        ),
                        textAlign = TextStringAlignToComposeTextAlign(
                            align = moduleDataState.projectOpenTextModule.wTextAlign
                        ),
                        fontSize = TextStringSizeToComposeTextSize(
                            size = moduleDataState.projectOpenTextModule.wTextH
                        ),
                        textDecoration = TextStringUnderlineToComposeTextUnderline(
                            underline = moduleDataState.projectOpenTextModule.wTextDecoration
                        ),


                        ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.Default
                    ),
                    keyboardActions = KeyboardActions(onDone = {

                        keyboardController?.hide(); focusManager.clearFocus(true)

                    }),


                    )

            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 2.dp, end = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start)
                {

                    Chip(
                        enabled = true,
                        colors = ChipDefaults.chipColors(
                            disabledBackgroundColor = MaterialTheme.colors.background,
                            disabledContentColor = Color.LightGray,
                            backgroundColor = if (selectedBottomChip.value=="Colors") {
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

                            },
                            contentColor = if(selectedBottomChip.value=="Colors") {
                                Color.White
                            } else {
                                MaterialTheme.colors.onPrimary
                            }
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (selectedBottomChip.value=="Colors") {
                                MaterialTheme.colors.primary.copy(alpha = 0.5f)
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
                            if(selectedBottomChip.value!="Colors") {
                            selectedBottomChip.value = "Colors" }
                            else { selectedBottomChip.value="" }
                        },
                    ) {
                        Text(
                            "Colors",
                            style = MaterialTheme.typography.h5
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Chip(
                        enabled = true,
                        colors = ChipDefaults.chipColors(
                            disabledBackgroundColor = MaterialTheme.colors.background,
                            disabledContentColor = Color.LightGray,
                            backgroundColor = if (selectedBottomChip.value=="Settings") {
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

                            },
                            contentColor = if(selectedBottomChip.value=="Settings") {
                                Color.White
                            } else {
                                MaterialTheme.colors.onPrimary
                            }
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (selectedBottomChip.value=="Settings") {
                                MaterialTheme.colors.primary.copy(alpha = 0.5f)
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
                            if(selectedBottomChip.value!="Settings") {
                                selectedBottomChip.value = "Settings" }
                            else { selectedBottomChip.value="" }
                        },
                    ) {
                        Text(
                            "Settings",
                            style = MaterialTheme.typography.h5
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Chip(
                        enabled = true,
                        colors = ChipDefaults.chipColors(
                            disabledBackgroundColor = MaterialTheme.colors.background,
                            disabledContentColor = Color.LightGray,
                            backgroundColor = if (selectedBottomChip.value=="Link") {
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

                            },
                            contentColor = if(selectedBottomChip.value=="Link") {
                                Color.White
                            } else {
                                MaterialTheme.colors.onPrimary
                            }
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (selectedBottomChip.value=="Link") {
                                MaterialTheme.colors.primary.copy(alpha = 0.5f)
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
                            if(selectedBottomChip.value!="Link") {
                                selectedBottomChip.value = "Link" }
                            else { selectedBottomChip.value="" }
                        },
                    ) {
                        Text(
                            "Link",
                            style = MaterialTheme.typography.h5
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically)
                {
                    IconButton(onClick = {
                        keyboardController?.hide(); focusManager.clearFocus(true)
                    }) {
                        Icon(
                            Icons.Filled.KeyboardHide,
                            UiText.StringResource(
                                R.string.creator_textmodule_textHideKey,
                                "asd"
                            )
                                .asString(),
                            tint = MaterialTheme.colors.primary
                        )

                    }
                    IconButton(onClick = {
                    }) {
                        Icon(
                            modifier = Modifier.pointerInput(key1 = true, key2 = true, block = {
                                this.detectTapGestures(
                                    onTap = {
                                        makeTextModuleSnack(R.string.creator_textmodule_textClearOnDoubleTapInfo)
                                    },
                                    onDoubleTap = {

                                        textField.value = TextFieldValue(text = "")
                                        setOpenTextModuleText("")

                                    })
                            }),
                            imageVector = Icons.Filled.Clear,
                            contentDescription = UiText.StringResource(
                                R.string.creator_textmodule_textClear,
                                "asd"
                            ).asString(),
                            tint = Color.Red
                        )

                    }

                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
            )
            if(selectedBottomChip.value=="Link") {
                LinkComponent(
                    linkModuleState = LinkModuleState(
                        componentId = "wTextLink",
                        componentName = "wTextLink",
                        componentRes = R.string.creator_module_linkmoduleinfo,
                        componentLinkType = LinkType.TEXT_MODULE_LINK,
                        componentValue = moduleDataState.projectOpenTextModule.wTextLink
                    ),
                    projectPagesList = moduleDataState.projectPagesList,
                    dataStoreDarkTheme = dataStoreDarkTheme,
                    returnLinkState = {linkModuleState -> setOpenTextModuleLink(linkModuleState)  })
            }
            if(selectedBottomChip.value=="Settings") {
                SettingsModuleComponent(
                    settingsModuleStateList = listOf(
                        SettingsModuleState(
                        componentId = "wTextBackgroundRoundedRectangle",
                        componentName = "wTextBackgroundRoundedRectangle",
                        componentRes = R.string.creator_module_roundedborder,
                        componentControllerType = ControllerType.SWITCH,
                        controllerValue = moduleDataState.projectOpenTextModule.wTextBackgroundRoundedRectangle
                        ),
                        SettingsModuleState(
                            componentId = "Group Text Margin",
                            componentName = "Group Text Margin",
                            componentRes = R.string.creator_module_textmargingroup,
                            componentControllerType = ControllerType.GROUP,
                            controllerValue = ""
                        ),
                        SettingsModuleState(
                            componentId = "wTextMarginTop",
                            componentName = "wTextMarginTop",
                            componentRes = R.string.creator_module_textmargintop,
                            componentControllerType = ControllerType.SLIDER(10,50, 1),
                            controllerValue = moduleDataState.projectOpenTextModule.wTextMarginTop
                        ),
                        SettingsModuleState(
                            componentId = "wTextMarginBottom",
                            componentName = "wTextMarginBottom",
                            componentRes = R.string.creator_module_textmarginbottom,
                            componentControllerType = ControllerType.SLIDER(10,50, 1),
                            controllerValue = moduleDataState.projectOpenTextModule.wTextMarginBottom
                        ),
                        SettingsModuleState(
                            componentId = "wTextMarginStart",
                            componentName = "wTextMarginStart",
                            componentRes = R.string.creator_module_textmarginstart,
                            componentControllerType = ControllerType.SLIDER(10,50, 1),
                            controllerValue = moduleDataState.projectOpenTextModule.wTextMarginStart
                        ),
                        SettingsModuleState(
                            componentId = "wTextMarginEnd",
                            componentName = "wTextMarginEnd",
                            componentRes = R.string.creator_module_textmarginend,
                            componentControllerType = ControllerType.SLIDER(10,50, 1),
                            controllerValue = moduleDataState.projectOpenTextModule.wTextMarginEnd
                        ),
                        SettingsModuleState(
                            componentId = "Group Border Margin",
                            componentName = "Group Border Margin",
                            componentRes = R.string.creator_module_textbordermargingroup,
                            componentControllerType = ControllerType.GROUP,
                            controllerValue = ""
                        ),
                        SettingsModuleState(
                            componentId = "wTextBorderMarginTop",
                            componentName = "wTextBorderMarginTop",
                            componentRes = R.string.creator_module_bordermargintop,
                            componentControllerType = ControllerType.SLIDER(0,50, 1),
                            controllerValue = moduleDataState.projectOpenTextModule.wTextBorderMarginTop
                        ),
                        SettingsModuleState(
                            componentId = "wTextBorderMarginBottom",
                            componentName = "wTextBorderMarginBottom",
                            componentRes = R.string.creator_module_bordermarginbottom,
                            componentControllerType = ControllerType.SLIDER(0,50, 1),
                            controllerValue = moduleDataState.projectOpenTextModule.wTextBorderMarginBottom
                        ),
                        SettingsModuleState(
                            componentId = "wTextBorderMarginStart",
                            componentName = "wTextBorderMarginStart",
                            componentRes = R.string.creator_module_bordermarginstart,
                            componentControllerType = ControllerType.SLIDER(0,50, 1),
                            controllerValue = moduleDataState.projectOpenTextModule.wTextBorderMarginStart
                        ),
                        SettingsModuleState(
                            componentId = "wTextBorderMarginEnd",
                            componentName = "wTextBorderMarginEnd",
                            componentRes = R.string.creator_module_bordermarginend,
                            componentControllerType = ControllerType.SLIDER(0,50, 1),
                            controllerValue = moduleDataState.projectOpenTextModule.wTextBorderMarginEnd
                        )



                    ),
                    dataStoreDarkTheme = dataStoreDarkTheme,
                    returnSettingsState = {
                        setOpenTextModuleSettings(it)
                        Log.i(TAG,"settingsModuleState: ${it.toString()}")
                    }
                )

            }
            if(selectedBottomChip.value=="Colors") {
                val chipsList = listOf(
                    Pair(
                        "ModuleTextColor",
                        UiText.StringResource(R.string.creator_module_textColor, "asd").asString()
                    ),
                    Pair(
                        "ModuleTextBackgroundColor",
                        UiText.StringResource(R.string.creator_module_backgroundColor, "asd").asString()
                    ),
                    Pair(
                        "ModuleTextBorderColor",
                        UiText.StringResource(R.string.creator_module_borderColor, "asd").asString()
                    )
                )
                val selectedChip = remember {
                    mutableStateOf(
                        convertCssRGBAPlusComponentIdToColorModuleState(
                            moduleDataState.projectOpenTextModule.wTextColor,
                            "ModuleTextColor"
                        )
                    )
                }
                /*
                Text(
                    "mDS: Background${moduleDataState.projectOpenTextModule.wTextBackgroundColor} \n"+
                            "mDS: TextColor${moduleDataState.projectOpenTextModule.wTextColor} \n"+
                            "mDS: Border${moduleDataState.projectOpenTextModule.wTextBorderColor} \n"+

                            "selected chip now: ${selectedChip.value.componentId} ${selectedChip.value.r} ${selectedChip.value.g} ${selectedChip.value.b}"

                )

                 */
                ColorChooserComponent(
                    dataStoreDarkTheme = dataStoreDarkTheme,
                    returnColor = { colorModuleState ->
                        setOpenTextModuleColorAction(colorModuleState);
                        selectedChip.value = colorModuleState

                    },
                    setChip = { cms ->
                        selectedChip.value = convertCssRGBAPlusComponentIdToColorModuleState(
                            when (cms) {
                                "ModuleTextBackgroundColor" -> moduleDataState.projectOpenTextModule.wTextBackgroundColor
                                "ModuleTextColor" -> moduleDataState.projectOpenTextModule.wTextColor
                                "ModuleTextBorderColor" -> moduleDataState.projectOpenTextModule.wTextBorderColor
                                else -> moduleDataState.projectOpenTextModule.wTextBackgroundColor
                            }, cms
                        )
                    },
                    selectedChip = selectedChip,
                    chips = chipsList
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            )
        }
    }
}