package com.arturlasok.feature_creator.presentation.creator_details

import androidx.compose.foundation.background
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
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.WebAsset
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_core.data.datasource.api.model.WebLayout
import com.arturlasok.feature_core.data.datasource.api.model.WebMenu
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_creator.model.ProjectMenuViewState
import com.arturlasok.feature_creator.util.getVisibleItemInfoFor
import com.arturlasok.feature_creator.util.move
import com.arturlasok.feature_creator.util.offsetEnd
import kotlinx.serialization.Contextual

@Composable
fun MenuListForOnePage(
    projectPageMenuList: SnapshotStateList<WebMenu>,
    projectPageList: SnapshotStateList<WebLayout>,
    org:String,
    enabled: Boolean,
    deleteOneMenu: (webMenu: WebMenu) -> Unit,
    iconList: List<Pair<String, ImageVector>>,
    creatorDataState: MutableState<@Contextual ProjectMenuViewState>,
    navigateToPageByToken: (token: String) -> Unit,

    ) {

    val youHaveDifferentOrder = projectPageMenuList.joinToString("-") { it._id.toString() } ==org

    val key2 = remember { mutableStateOf(0) }

    val displacementOffset = remember { mutableStateOf(0f) }
    val menuColumnState = rememberLazyListState()
    val currentDraggedElementIndex = remember { mutableStateOf(-1); }
    val dragDistance = remember { mutableStateOf(0) }

    val listOffsets = remember { mutableStateListOf<Pair<String,Int>>() }

    val currentFocusedElementIndex : MutableState<Int?> = remember { mutableStateOf(null) }


    fun tokenToRouteName(token:String) :String {
        return projectPageList.find {
            it.wLayoutRouteToken == token
        }?.wLayoutPageName ?: "Home"
    }
    fun tokenToRouteIcon(token: String) : ImageVector {

        val iconString = projectPageList.find {
            it.wLayoutRouteToken == token
        }?.wLayoutModuleType

        return iconList.find {
            it.first == iconString
        }?.second ?: Icons.Filled.WebAsset
    }

    Spacer(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, end = 8.dp)
        .background(Color.LightGray)
        .height(1.dp))
    when (creatorDataState.value) {
        is ProjectMenuViewState.Open -> {
            if(projectPageMenuList.size>1) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 3.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        color = MaterialTheme.colors.error,
                        text = UiText.StringResource(
                            com.arturlasok.feature_creator.R.string.creator_dragdropmenuinfo,
                            "asd"
                        ).asString().uppercase(),
                        style = MaterialTheme.typography.h6
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                )
            }
        }

        else -> {}
    }
    LazyColumn(
        state = menuColumnState,
        modifier = Modifier
            .padding(bottom = 0.dp)
            .fillMaxWidth()
            .pointerInput(key1 = enabled, key2 = key2) {
                if (enabled) {
                    //button do zapisu stanu
                    this.detectDragGesturesAfterLongPress(
                        onDrag = { change, dragAmount ->
                            displacementOffset.value = change.position.y
                            if (currentFocusedElementIndex.value != null) {
                                projectPageMenuList.move(
                                    currentDraggedElementIndex.value,
                                    currentFocusedElementIndex.value!!
                                )
                                currentDraggedElementIndex.value =
                                    currentFocusedElementIndex.value!!

                                dragDistance.value = (menuColumnState.getVisibleItemInfoFor(
                                    currentDraggedElementIndex.value
                                )?.offsetEnd
                                    ?: 0) - 30

                                //dragDistance.value = menuColumnState.layoutInfo.visibleItemsInfo[currentDraggedElementIndex.value].offsetEnd - 30
                            }
                            change.consume()

                        },
                        onDragStart = { offset ->

                            currentDraggedElementIndex.value =
                                menuColumnState.layoutInfo.visibleItemsInfo.firstOrNull { item ->
                                    offset.y.toInt() in item.offset..(item.offset + item.size)
                                }?.index ?: -1
                            displacementOffset.value = offset.y
                            //TODO out of bounds possible
                            dragDistance.value =
                                (menuColumnState.getVisibleItemInfoFor(currentDraggedElementIndex.value)?.offsetEnd
                                    ?: 0) - 30
                            //dragDistance.value = menuColumnState.layoutInfo.visibleItemsInfo[currentDraggedElementIndex.value].offsetEnd - 30
                            listOffsets.clear()
                            menuColumnState.layoutInfo.visibleItemsInfo.onEach { listItem ->
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
            }
    ) {

        itemsIndexed(
            items = projectPageMenuList,
            key = { _, item -> item._id?.toString() ?: "" }
        ) {index, oneMenuElement ->
            val isBeingDragged = displacementOffset.value != 0f
            val backgroundColor = if (isBeingDragged && index==currentDraggedElementIndex.value) {
                
                Color.LightGray
                
            } else {
                Color.Transparent
            }
            
            Column(

                modifier = Modifier.graphicsLayer { translationY = if(index!=currentDraggedElementIndex.value) 0f else displacementOffset.value-dragDistance.value.toFloat() },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor)
                    ) {
                    IconButton(
                        onClick = {
                            when (creatorDataState.value) {
                                is ProjectMenuViewState.Short -> {
                                    navigateToPageByToken(oneMenuElement.wMenuRoute)
                                }

                                else -> {}
                            }
                            },
                        modifier = Modifier
                            .padding(2.dp)
                            .weight(0.9f, true)


                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            when (creatorDataState.value) {
                                is ProjectMenuViewState.Short -> {
                                    Icon(
                                        tokenToRouteIcon(oneMenuElement.wMenuRoute),
                                        "Icon",
                                        tint = MaterialTheme.colors.onBackground,
                                        modifier = Modifier.width(32.dp),
                                    )
                                    Text(
                                        text =
                                        tokenToRouteName(oneMenuElement.wMenuRoute).uppercase(),
                                        style = MaterialTheme.typography.h6,
                                        maxLines = 3,
                                        overflow = TextOverflow.Ellipsis,
                                        textAlign = TextAlign.Center
                                    )
                                }

                                else -> {
                                    //ProjectMenuViewState.Open
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            tokenToRouteIcon(oneMenuElement.wMenuRoute),
                                            "Icon",
                                            tint = MaterialTheme.colors.onBackground,
                                            modifier = Modifier.width(48.dp),
                                        )
                                        Spacer(modifier = Modifier.width(1.dp))
                                        Text(
                                            text =
                                            tokenToRouteName(oneMenuElement.wMenuRoute).uppercase(),
                                            style = MaterialTheme.typography.h5,
                                            maxLines = 1,
                                            fontWeight = FontWeight.Bold,
                                            overflow = TextOverflow.Ellipsis,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                    //Delete
                    if(youHaveDifferentOrder) {
                        when (creatorDataState.value) {
                            is ProjectMenuViewState.Open -> {

                                IconButton(
                                    onClick = {
                                        deleteOneMenu(oneMenuElement)
                                    },
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .width(32.dp)
                                ) {
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.End
                                    ) {

                                        Icon(
                                            Icons.Filled.Delete,
                                            "Icon",
                                            tint = MaterialTheme.colors.error,
                                            modifier = Modifier.width(32.dp),
                                        )
                                    }
                                }
                            }

                            else -> {
                                //Short
                            }
                        }
                    }
                }

                if(projectPageMenuList.isNotEmpty()) {

                        if (listOffsets.any {
                                ((displacementOffset.value) in it.second.toFloat() - 5..it.second.toFloat() + 5) && it.first.toInt() == index
                            }) {
                          currentFocusedElementIndex.value = index

                        } else {

                        }
                }
            }
        }
    }
}