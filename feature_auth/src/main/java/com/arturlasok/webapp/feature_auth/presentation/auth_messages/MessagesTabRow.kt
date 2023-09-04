package com.arturlasok.webapp.feature_auth.presentation.auth_messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arturlasok.webapp.feature_auth.presentation.components.CustomIndicator

@Composable
fun MessagesTabRow(currentTabPosition: Int, setCurrentTabPosition:(pos: Int) ->Unit, daneTab: List<String> = listOf("IN BOX","OUT BOX")) {
    val indicator2 = @Composable { tabPositions: List<TabPosition> ->
        CustomIndicator(
            tabPositions = tabPositions,
            selectedTabIndex = currentTabPosition
        )
    }
    TabRow(

        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth(),

        indicator = indicator2,

        selectedTabIndex = currentTabPosition
    ) {

        val daneDoTab = daneTab

        daneDoTab.forEachIndexed { index, element ->


            Tab(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .height(50.dp),

                text = { Text(text = element, color = MaterialTheme.colors.surface) },
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