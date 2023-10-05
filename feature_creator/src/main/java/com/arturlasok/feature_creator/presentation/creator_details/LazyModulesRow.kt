package com.arturlasok.feature_creator.presentation.creator_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_core.domain.model.ModuleType
import com.arturlasok.feature_core.util.UiText

@Composable
fun LazyModulesRow(modulesList: SnapshotStateList<Triple<Int, Int, ModuleType>>) {

    LazyRow(state = rememberLazyListState()) {
        itemsIndexed(modulesList) {index, item ->

            Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(4.dp))
                Image(
                    bitmap = ImageBitmap.imageResource(
                        id = item.second
                    ),
                    modifier = Modifier
                        .size(
                            48.dp,
                            48.dp
                        )
                        .padding(bottom = 2.dp)
                        .alpha(0.9f),
                    contentDescription = UiText.StringResource(item.first, "asd").asString(),

                    )
                Text(
                    text = UiText.StringResource(item.first, "asd").asString(),
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

        }
    }


}