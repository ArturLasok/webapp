package com.arturlasok.feature_creator.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WebAsset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_core.data.datasource.api.model.WebLayout
import com.arturlasok.feature_core.util.ColorType
import com.arturlasok.feature_core.util.ExtraColors
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_creator.R
import com.arturlasok.feature_creator.model.CreatorDataState

@Composable
fun IconsLazyRow(
    darkTheme:Boolean,
    iconsList:List<Pair<String,ImageVector>>,
    selectedIconName: String,
    setSelectedIconName:(name:String) -> Unit,
) {
    val selectedIndex = remember {
        mutableStateOf(iconsList.indexOfFirst {
            it.first == selectedIconName
        })
    }
    val pagesRowState = rememberLazyListState()
    LaunchedEffect(key1 = selectedIndex.value, block = {
        try {

            pagesRowState.animateScrollToItem(selectedIndex.value)
        }
        catch(_:Exception) {

        }

    })
    LazyRow(
        state = pagesRowState,
        modifier = Modifier.padding(bottom = 0.dp)
    ) {

        itemsIndexed(
            items = iconsList,
            key = { _, item -> item.first }
        ) { index, oneIcon ->
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    shape = MaterialTheme.shapes.medium,
                    elevation = 6.dp,
                    color = if(selectedIconName==oneIcon.first) {
                        ExtraColors(
                            type = ColorType.DESIGNONE,
                            darktheme = darkTheme
                        )
                    } else { MaterialTheme.colors.background },
                    modifier = Modifier
                        .padding(
                            start = 12.dp,
                            end = 0.dp,
                            bottom = 6.dp
                        )
                        .padding(top = 0.dp)
                        .height(60.dp)
                        .width(60.dp)
                ) {
                    Column(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        IconButton(
                            onClick = {
                                setSelectedIconName(oneIcon.first)
                                selectedIndex.value = index

                            },
                            modifier = Modifier
                                .padding(2.dp)
                                .fillMaxSize()

                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    oneIcon.second,
                                    oneIcon.first,
                                    tint = MaterialTheme.colors.onBackground,
                                    modifier = Modifier.width(48.dp),
                                )
                            }
                        }

                    }
                }
                Spacer(
                    modifier = Modifier
                        .height(2.dp)
                        .width(10.dp)
                )
            }
        }
    }
}