package com.arturlasok.feature_creator.presentation.creator_module

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.getDarkBoolean
import com.arturlasok.feature_creator.model.ModuleDataState
import com.arturlasok.feature_creator.util.getVisibleItemInfoFor
import com.arturlasok.feature_creator.util.move
import com.arturlasok.feature_creator.util.offsetEnd

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModuleContent(
    offsetX: MutableState<Float>,
    offsetY: MutableState<Float>,
    pp: MutableState<Float>,
    moduleDataState: ModuleDataState,
    dataStoreDarkTheme: Int,
    icons: List<Pair<String, ImageVector>>,
    reset: MutableState<Boolean>,
    toIndex: MutableState<Int?>,
    setToIndex: (index: Int?) -> Unit,
    deleteOneModule:(id:String) ->Unit,
    deletedMessagesList: SnapshotStateList<String>,
    setOpenModuleToId:(id:String) ->Unit
    ) {
    val endFocus = remember { mutableStateOf(false) }
    val key2 = remember { mutableStateOf(0) }
    val positionInParent = remember { mutableStateOf(0f) }
    val displacementOffset = remember { mutableStateOf(0f) }
    val moduleLazyListState = rememberLazyListState()
    val currentDraggedElementIndex = remember { mutableStateOf(-1); }
    val dragDistance = remember { mutableStateOf(0) }
    val listOffsets = remember { mutableStateListOf<Pair<String, Int>>() }
    val listOffsetsPP = remember { mutableStateListOf<Pair<String, Int>>() }
    val currentFocusedElementIndex: MutableState<Int?> = remember { mutableStateOf(null) }
    val selectedOpenModuleIndex : MutableState<Int?> = remember { mutableStateOf(null) }

    if(reset.value) {
        LaunchedEffect(key1 =reset.value , block = {
            setToIndex(null)
            //toIndex.value = null
            endFocus.value = false

        })
    }
    LaunchedEffect(key1 = moduleDataState.projectOpenModuleId.value, block = {
        if(selectedOpenModuleIndex.value!=null) {
            try {
                moduleLazyListState.animateScrollToItem(selectedOpenModuleIndex.value!!)
            }
            catch (e:Exception) {
                //nothing
            }

        }
    })
    LaunchedEffect(key1 = offsetY.value, block = {
        listOffsetsPP.clear()
        moduleLazyListState.layoutInfo.visibleItemsInfo.onEach {lazyItem->
            val valek = lazyItem.offsetEnd-lazyItem.size-moduleLazyListState.firstVisibleItemScrollOffset
            listOffsetsPP.add(
                Pair(
                    lazyItem.index.toString(),
                    valek
                )
            )
        }
    })

    LazyColumn(
        state = moduleLazyListState,
        userScrollEnabled = moduleDataState.projectOpenModuleId.value.isEmpty(),
        modifier = Modifier
            .onGloballyPositioned { layoutCoordinates ->
                positionInParent.value =
                    layoutCoordinates.parentCoordinates?.positionInRoot()?.y ?: 0f
            }
            .padding(2.dp)
            .fillMaxWidth()
            .pointerInput(key1 = true, key2 = key2) {

                this.detectDragGesturesAfterLongPress(
                    onDrag = { change, dragAmount ->
                        displacementOffset.value = change.position.y
                        if (currentFocusedElementIndex.value != null) {
                            moduleDataState.projectModulesList.move(
                                currentDraggedElementIndex.value,
                                currentFocusedElementIndex.value!!
                            )
                            currentDraggedElementIndex.value =
                                currentFocusedElementIndex.value!!

                            dragDistance.value = (moduleLazyListState.getVisibleItemInfoFor(
                                currentDraggedElementIndex.value
                            )?.offsetEnd
                                ?: 0) - 30

                        }
                        change.consume()

                    },
                    onDragStart = { offset ->

                        currentDraggedElementIndex.value =
                            moduleLazyListState.layoutInfo.visibleItemsInfo.firstOrNull { item ->
                                offset.y.toInt() in item.offset..(item.offset + item.size)
                            }?.index ?: -1
                        displacementOffset.value = offset.y

                        dragDistance.value =
                            (moduleLazyListState.getVisibleItemInfoFor(currentDraggedElementIndex.value)?.offsetEnd
                                ?: 0) - 30

                        listOffsets.clear()
                        moduleLazyListState.layoutInfo.visibleItemsInfo.onEach { listItem ->
                            listOffsets.add(
                                Pair(
                                    listItem.index.toString(),
                                    listItem.offsetEnd - 30
                                )
                            )
                        }

                    },
                    onDragCancel = {
                        currentFocusedElementIndex.value = null
                        displacementOffset.value = 0f
                        dragDistance.value = 0
                        listOffsets.clear()
                    },
                    onDragEnd = {
                        currentFocusedElementIndex.value = null
                        displacementOffset.value = 0f
                        dragDistance.value = 0
                        listOffsets.clear()
                    }

                )


            }
            .zIndex(0.9f)

    ) {
        itemsIndexed(
            items = moduleDataState.projectModulesList,
            key = { _, item -> item._id?.toString() ?: "" }
        ) { index, oneElement ->
            val paddingOnFocus = mutableStateOf(index == toIndex.value)
            val isBeingDragged = displacementOffset.value != 0f
            val backgroundColor = if (isBeingDragged && index == currentDraggedElementIndex.value) {

                MaterialTheme.colors.primary.copy(alpha = 0.5f)

            } else {
                Color.Transparent
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp, bottom = 2.dp, start = 6.dp, end = 6.dp)
                    .background(MaterialTheme.colors.primary.copy(alpha = 0.5f))
                    .height(if (paddingOnFocus.value) 24.dp else 0.dp)
                    .zIndex(0.9f)
            )

            AnimatedVisibility(
                visible = !deletedMessagesList.contains(oneElement._id.toString().substringAfter("oid=").substringBefore("}")),
                exit = shrinkVertically(
                    animationSpec = tween(
                        delayMillis = 0,
                        easing = FastOutSlowInEasing,
                        durationMillis = 500
                    )
                ),
                enter = expandVertically()
            ) {


            Column(
                modifier = Modifier
                    .border(1.dp, MaterialTheme.colors.primary.copy(alpha = 0.1f))
                    .graphicsLayer {
                        translationY =
                            if (index != currentDraggedElementIndex.value) 0f else displacementOffset.value - dragDistance.value.toFloat()
                    }
                    .zIndex(0.9f)
                    .clickable(onClick = {
                        selectedOpenModuleIndex.value = index
                        setOpenModuleToId(
                            oneElement._id
                                .toString()
                                .substringAfter("oid=")
                                .substringBefore("}")
                        )
                    })
                  ,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ModuleView(
                    dataStoreDarkTheme = getDarkBoolean(isSystemInDarkTheme(),dataStoreDarkTheme),
                    backgroundColor = backgroundColor,
                    moduleDataState = moduleDataState,
                    icons = icons,
                    oneElement = oneElement,
                    index = index,
                    deleteOneModule = { id-> deleteOneModule(id)},
                    updateOpenModuleId = {},
                    setOpenTextModuleText = {},
                    setOpenTextModuleAction = {},
                    makeTextModuleSnack = {},
                    setOpenTextModuleColorAction = {},
                    setOpenTextModuleSettings = {},
                    setOpenTextModuleLink = {}
                )

            }
            if (moduleDataState.projectModulesList.isNotEmpty()) {

                if (listOffsets.any {
                        ((displacementOffset.value) in it.second.toFloat() - 5..it.second.toFloat() + 5) && it.first.toInt() == index
                    }) {
                    currentFocusedElementIndex.value = index

                } else {

                }
            }
            LaunchedEffect(key1 = offsetY.value, block = {

                if (moduleDataState.projectModulesList.isNotEmpty()) {
                    if (listOffsetsPP.any {
                            ((offsetY.value - (positionInParent.value - pp.value) - moduleLazyListState.firstVisibleItemScrollOffset) in (it.second.toFloat() - 30f / 2)..it.second.toFloat() + 30f / 2) && it.first.toInt() == index
                            //((offsetY.value-(positionInParent.value-pp.value)-moduleLazyListState.firstVisibleItemScrollOffset) in (it.second.toFloat() - oneElement.wPageModuleSort.toFloat()/2).. it.second.toFloat() + oneElement.wPageModuleSort.toFloat()/2) && it.first.toInt() == index
                        }) {
                        Log.i(
                            TAG,
                            "Lista ToIndex:${index} //posInParent-pp: ${positionInParent.value - pp.value}"
                        )
                        if (offsetY.value > 0) {
                            setToIndex(index)

                            endFocus.value = false
                        }
                    } else if ((offsetY.value - (positionInParent.value - pp.value) - moduleLazyListState.firstVisibleItemScrollOffset > (listOffsetsPP.lastOrNull()?.second?.toFloat()
                            ?: 100000f) + 30f * 2) && index == moduleLazyListState.layoutInfo.totalItemsCount - 1
                    //?: 100000f)+oneElement.wPageModuleSort.toFloat()*2) && index == moduleLazyListState.layoutInfo.totalItemsCount-1
                    ) {
                        Log.i(TAG, "to end!")
                        endFocus.value = true
                        setToIndex(-1)
                    }
                }


            })
        }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.primary.copy(alpha = 0.5f))
                        .height(if (endFocus.value && index == moduleLazyListState.layoutInfo.totalItemsCount - 1) 24.dp else 0.dp)
                        .zIndex(0.8f)
                )
            }

    }
}