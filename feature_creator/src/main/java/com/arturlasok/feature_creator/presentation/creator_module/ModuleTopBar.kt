package com.arturlasok.feature_creator.presentation.creator_module

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.arturlasok.feature_core.util.ColorType
import com.arturlasok.feature_core.util.ExtraColors
import com.arturlasok.feature_core.util.getDarkBoolean
import com.arturlasok.feature_creator.model.ModuleDataState
import kotlin.math.roundToInt

@Composable
fun ModuleTopBar(
    moduleDataState: ModuleDataState,
    dataStoreDarkTheme: Int,
    icons: List<Pair<String, ImageVector>>,
    addThisElement: (name: String) -> Unit,
    pp: MutableState<Float>,
    offsetX: MutableState<Float>,
    offsetY: MutableState<Float>,
    setResetValue: (value: Boolean) -> Unit,
    reset: MutableState<Boolean>,
    selectedTool: MutableState<String>,
    setSelectedTool: (toolName: String) -> Unit,
) {

    //Text("reset: ${reset.value} // offset X: ${offsetX.value} / offset Y: ${offsetY.value}",modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End, style = MaterialTheme.typography.h5)


    Column(modifier = Modifier.height(56.dp).fillMaxWidth().background(
        ExtraColors(
            type = ColorType.DESIGNONE,
            darktheme = getDarkBoolean(
                isSystemInDarkTheme(),
                dataStoreDarkTheme
            )
        )


    )) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            elevation = 6.dp,
            color = MaterialTheme.colors.background
            ,
            modifier = Modifier.padding(2.dp)
              //  .padding(top = 16.dp)
                .height(50.dp)
                .fillMaxWidth()
                .zIndex(1.0f)
        ) {

        }
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            //.border(1.dp, Color.Cyan)
            .padding(6.dp).padding(start = 8.dp,end = 8.dp)
    ) {
        Text(
            text = "Tools".uppercase(),
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.clickable(onClick = {
                moduleDataState.projectModulesList.clear()
            }),
            text = "PAGE: "+moduleDataState.projectPageName.value.uppercase(),
            style = MaterialTheme.typography.h5
        )
    }
    Column(
        modifier = Modifier
            //.border(1.dp, Color.Red)
            .fillMaxSize()
            .horizontalScroll(rememberScrollState())
            .padding(6.dp, top = 20.dp)
            .zIndex(0.9f),
        verticalArrangement = Arrangement.Top,

        ) {

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.zIndex(1.0f)
        ) {
            icons.onEach {toolIcon->
/*
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    elevation = 6.dp,
                    color = MaterialTheme.colors.background
                    ,
                    modifier = Modifier

 */
                        /*
                        .offset {
                            if (toolIcon.first == "Icons.Filled.Home") {
                                IntOffset(
                                    offsetX.value.roundToInt(),
                                    offsetY.value.roundToInt()
                                )
                            } else {
                                IntOffset(0, 0)
                            }
                        }
                        .pointerInput(key1 = reset.value) {
                            this.detectDragGestures(
                                onDragStart = { offset ->

                                },
                                onDrag = { change: PointerInputChange, dragAmount: Offset ->
                                    change.consume()
                                    if (toolIcon.first == "Icons.Filled.Home") {
                                        offsetX.value += dragAmount.x
                                        offsetY.value += dragAmount.y
                                    }

                                },
                                onDragEnd = {
                                    //reset.value = true
                                    setResetValue(true)
                                    addThisElement(toolIcon.first)
                                },
                                onDragCancel = {

                                }

                            )

                        }

                         */
                /*
                        .padding(
                            start = 3.dp,
                            end = 0.dp,
                            bottom = 3.dp
                        )
                        .padding(top = 3.dp)
                        .height(40.dp)
                        .width(40.dp)
                        .zIndex(1.0f)
                ) {

                 */
                    Column(
                        modifier = Modifier.zIndex(1.0f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = toolIcon.second,
                            contentDescription = toolIcon.first,
                            tint = MaterialTheme.colors.onBackground,
                            modifier = Modifier
                                .width(32.dp)
                                .onGloballyPositioned { layoutCoordinates ->
                                    pp.value =
                                        layoutCoordinates.parentCoordinates?.positionInRoot()?.y
                                            ?: 0f
                                }
                                .zIndex(1.0f)

                                .offset {
                                    if(toolIcon.first==selectedTool.value) {
                                        IntOffset(
                                            offsetX.value.roundToInt(),
                                            offsetY.value.roundToInt()
                                        )
                                    } else {
                                        IntOffset(0,0)
                                    }
                                }
                                .pointerInput(key1 = reset.value) {
                                    this.detectDragGestures(
                                        onDragStart = { offset ->
setSelectedTool(toolIcon.first)
                                        },
                                        onDrag = { change: PointerInputChange, dragAmount: Offset ->
                                            change.consume()
                                            if(toolIcon.first==selectedTool.value) {
                                                offsetX.value += dragAmount.x
                                                offsetY.value += dragAmount.y
                                            }

                                        },
                                        onDragEnd = {
                                            //reset.value = true
                                            addThisElement(toolIcon.first)
                                            setResetValue(true)
                                            setSelectedTool("")
                                        },
                                        onDragCancel = {
                                            setResetValue(true)
                                            setSelectedTool("")
                                        }

                                    )

                                },


                        )
                        Spacer(
                            modifier = Modifier
                                .height(2.dp)
                                .width(1.dp)
                        )
                    }
              //  }
            }

        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(6.dp))

        /*
        ModuleToolsLazyRow(
            darkTheme = getDarkBoolean(isSystemInDarkTheme(),dataStoreDarkTheme),
            iconsList = icons,
            selectedIconName = "addPageViewModel.newPageDataState.value.newPageIconName",
            setSelectedIconName = {  },
            addThisElement = { name ->  addThisElement(name) }
        )

         */
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(3.dp))

    }
    if(reset.value) {
        LaunchedEffect(key1 = true, block = {
            //reset.value = false
            setResetValue(false)
            offsetX.value = 0f
            offsetY.value = 0f
        })
    }

}