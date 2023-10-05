package com.arturlasok.feature_creator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DefaultTabRow(currentTabPosition: Int, setCurrentTabPosition:(pos: Int) ->Unit, daneTab: List<String>) {

    val indicator2 = @Composable { tabPositions: List<TabPosition> ->
        DetailsIndicator(
            tabPositions = tabPositions,
            selectedTabIndex = currentTabPosition
        )
    }
    TabRow(

        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth(),

        indicator = indicator2,

        selectedTabIndex = currentTabPosition
    ) {

        val daneDoTab = daneTab

        daneDoTab.forEachIndexed { index, element ->


            Tab(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .height(40.dp),

                text = { Text(text = element, color = if(currentTabPosition == index) { MaterialTheme.colors.surface } else { Color.Gray}) },
                onClick = {
                   setCurrentTabPosition(
                        index
                    )
                },
                selected = currentTabPosition == index,
                unselectedContentColor = Color.LightGray,
                selectedContentColor = MaterialTheme.colors.primaryVariant,


                )

        }

    }
}