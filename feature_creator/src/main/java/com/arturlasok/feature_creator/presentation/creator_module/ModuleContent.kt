package com.arturlasok.feature_creator.presentation.creator_module

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.arturlasok.feature_creator.model.ModuleDataState
import com.arturlasok.feature_creator.util.getVisibleItemInfoFor
import com.arturlasok.feature_creator.util.move
import com.arturlasok.feature_creator.util.offsetEnd

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

    ) {
    val endFocus = remember { mutableStateOf(false) }
    val key2 = remember { mutableStateOf(0) }
    //val toIndex: MutableState<Int?> = remember { mutableStateOf(null) }
    val positionInParent = remember { mutableStateOf(0f) }
    val displacementOffset = remember { mutableStateOf(0f) }
    val moduleLazyListState = rememberLazyListState()
    val currentDraggedElementIndex = remember { mutableStateOf(-1); }
    val dragDistance = remember { mutableStateOf(0) }
    val listOffsets = remember { mutableStateListOf<Pair<String, Int>>() }
    val listOffsetsPP = remember { mutableStateListOf<Pair<String, Int>>() }
    val currentFocusedElementIndex: MutableState<Int?> = remember { mutableStateOf(null) }
    /*
    Text("+X:${offsetX.value},+Y:${offsetY.value} " +
            " // focused: ${currentFocusedElementIndex.value}"+
            " // toIndex: ${toIndex.value}"+
           // " // firstscrolloffset:${}"+
            " // firstVisibleItemOffset:${moduleLazyListState.layoutInfo.visibleItemsInfo.firstOrNull()?.offset} " +
            //" // displacementOffset: ${displacementOffset.value}" +
            //" // dragDistance: ${dragDistance.value}" +
            "+pp: ${pp.value} // pp: ${positionInParent.value}" , style = MaterialTheme.typography.h5, modifier = Modifier.clickable(onClick = {
        listOffsetsPP.onEach { listItem ->
            Log.i(TAG, "[${listItem.first}] -> ${listItem.second}}")

        }
            }
            )
    )

     */
    if(reset.value) {
   LaunchedEffect(key1 =reset.value , block = {
            setToIndex(null)
           //toIndex.value = null
           endFocus.value = false

   })
    }
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
            Log.i(TAG, "ADD to offset: [${lazyItem.index}] = ${valek} offsetEnd: ${lazyItem.offsetEnd}//offset: ${lazyItem.offset} // size: ${lazyItem.size} ")
        }

    })
    LazyColumn(
        state = moduleLazyListState,
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
            }.zIndex(0.9f)

    ) {
        itemsIndexed(
            items = moduleDataState.projectModulesList,
            key = { _, item -> item._id?.toString() ?: "" }
        ) { index, oneElement ->
            //val paddingOnFocus = mutableStateOf(if(offsetY.value in ((positionInParent.value+(index*48))-pp.value)..(((positionInParent.value+(index*48))-pp.value)+40)) 24.dp else 0.dp)
            val paddingOnFocus =  mutableStateOf(index==toIndex.value)
            val isBeingDragged = displacementOffset.value != 0f
            val backgroundColor = if (isBeingDragged && index == currentDraggedElementIndex.value) {

                Color.LightGray

            } else {
                Color.Transparent
            }
           Spacer(
                modifier = Modifier
                    .fillMaxWidth().padding(top=2.dp, bottom = 2.dp, start = 6.dp, end = 6.dp)
                    .background(Color.Yellow.copy(alpha = 0.5f))
                    .height(if (paddingOnFocus.value) 24.dp else 0.dp).zIndex(0.9f)
            )
            Column(
                modifier = Modifier
                    .border(1.dp, Color.Yellow)
                    .graphicsLayer {
                        translationY =
                            if (index != currentDraggedElementIndex.value) 0f else displacementOffset.value - dragDistance.value.toFloat()
                    }.zIndex(0.9f),
                    //.padding(top = if (paddingOnFocus.value) 24.dp else 0.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor)
                        .zIndex(0.9f)
                ) {
                    Icon(
                        icons.find {
                                   it.first == oneElement.wLayoutPageName

                        }?.second ?: Icons.Filled.Info,
                        "Icon",
                        tint = MaterialTheme.colors.onBackground,
                        modifier = Modifier
                            .width(48.dp)
                            .height(oneElement.wLayoutSort.toInt().dp),
                    )
                    Text(text = "index: ${index}")
                    Text("paddingOnF: ${paddingOnFocus.value} /lista offsetów:${listOffsetsPP.size}", style = MaterialTheme.typography.h5)
                }
            }
            if(moduleDataState.projectModulesList.isNotEmpty()) {

                if (listOffsets.any {
                        ((displacementOffset.value) in it.second.toFloat() - 5..it.second.toFloat() + 5) && it.first.toInt() == index
                    }) {
                    currentFocusedElementIndex.value = index

                } else {

                }
            }
            LaunchedEffect(key1 = offsetY.value, block = {
                val paddingadd = if(paddingOnFocus.value) 24 else 0
                if(moduleDataState.projectModulesList.isNotEmpty()) {
                    if (listOffsetsPP.any {
                            ((offsetY.value-(positionInParent.value-pp.value)-moduleLazyListState.firstVisibleItemScrollOffset) in (it.second.toFloat() - oneElement.wLayoutSort.toFloat()/2).. it.second.toFloat() + oneElement.wLayoutSort.toFloat()/2) && it.first.toInt() == index
                       //+(moduleLazyListState.firstVisibleItemIndex*46)
                        }) {
                        Log.i(TAG,"Lista ToIndex:${index} //posInParent-pp: ${positionInParent.value-pp.value}")

                        //toIndex.value = index
                        setToIndex(index)
                        endFocus.value = false

                    } else if((offsetY.value - (positionInParent.value - pp.value) - moduleLazyListState.firstVisibleItemScrollOffset > (listOffsetsPP.lastOrNull()?.second?.toFloat()
                            ?: 100000f)+oneElement.wLayoutSort.toFloat()*2) && index == moduleLazyListState.layoutInfo.totalItemsCount-1
                    ) {
                       Log.i(TAG,"to end!")
                        endFocus.value = true
                        setToIndex(null)
                       //toIndex.value = null
                      //  Log.i(TAG,">>>>[ ${index} ] -> offset: ${moduleLazyListState.layoutInfo.visibleItemsInfo.getOrNull(index)?.offset}")
                    }
                }


            })
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Yellow.copy(alpha = 0.5f))
                    .height(if (endFocus.value && index == moduleLazyListState.layoutInfo.totalItemsCount - 1) 24.dp else 0.dp).zIndex(0.8f)
            )
        }
    }
}