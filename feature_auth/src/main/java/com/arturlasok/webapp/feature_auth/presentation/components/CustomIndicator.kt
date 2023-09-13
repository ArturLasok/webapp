package com.arturlasok.webapp.feature_auth.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomIndicator(
    tabPositions: List<TabPosition>, selectedTabIndex: Int

) {
    Surface(

        color = MaterialTheme.colors.primary,
        modifier = Modifier

            .tabIndicatorOffset(tabPositions[selectedTabIndex])
            .width(tabPositions[selectedTabIndex].width)
            .padding(3.dp)
            .height(3.dp)
    ) {

    }
}