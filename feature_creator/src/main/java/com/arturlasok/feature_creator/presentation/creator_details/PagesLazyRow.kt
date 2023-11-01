package com.arturlasok.feature_creator.presentation.creator_details

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_core.data.datasource.api.model.WebLayout
import com.arturlasok.feature_core.util.ColorType
import com.arturlasok.feature_core.util.ExtraColors
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_creator.R
import com.arturlasok.feature_creator.model.CreatorDataState

@Composable
fun PagesLazyRow(
    darkTheme:Boolean,
    creatorDataState: CreatorDataState,
    pageList: List<WebLayout>,
    selectedPageToken: String,
    setSelectedPageToken:(token:String) -> Unit,
) {
    val pagesRowState = rememberLazyListState()
    LazyRow(
        state = pagesRowState,
        modifier = Modifier.padding(bottom = 0.dp)
    ) {

        itemsIndexed(
            items = pageList,
            key = { _, item -> item._id?.toString() ?: "" }
        ) { index, onePage ->
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    shape = MaterialTheme.shapes.medium,
                    elevation = 6.dp,
                    color = if(selectedPageToken==onePage.wLayoutRouteToken) {
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
                        .fillMaxWidth(fraction = 0.90f)
                ) {
                    Column(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        IconButton(
                            onClick = {
                                setSelectedPageToken(onePage.wLayoutRouteToken)
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

                                    Icons.Filled.WebAsset,
                                    UiText.StringResource(
                                        R.string.creator_projectPage,
                                        "asd"
                                    ).asString(),
                                    tint = MaterialTheme.colors.onBackground,
                                    modifier = Modifier.width(32.dp),
                                )
                                Text(
                                    text = onePage.wLayoutPageName.uppercase()
                                        .toString(),
                                    style = MaterialTheme.typography.h6,
                                    textAlign = TextAlign.Center
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