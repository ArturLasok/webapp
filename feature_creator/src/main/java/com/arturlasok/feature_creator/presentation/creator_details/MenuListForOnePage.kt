package com.arturlasok.feature_creator.presentation.creator_details

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_core.data.datasource.api.model.WebLayout
import com.arturlasok.feature_core.data.datasource.api.model.WebMenu

@Composable
fun MenuListForOnePage(
    projectPageMenuList: SnapshotStateList<WebMenu>,
    projectPageList: SnapshotStateList<WebLayout>,
    navigateToPageByToken: (token: String) -> Unit
) {
    fun tokenToRouteName(token:String) :String {
        return projectPageList.find {
            it.wLayoutRouteToken == token
        }?.wLayoutPageName ?: "Home"
    }

    projectPageMenuList.onEachIndexed {index, oneMenu ->
        Column(modifier = Modifier.border(1.dp, Color.Green).padding(3.dp).clickable(onClick = { navigateToPageByToken(oneMenu.wMenuRoute) })) {
            Text(text="${index}. ${oneMenu.wMenuColor}", style = MaterialTheme.typography.h5, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text="Place ${oneMenu.wMenuPlace}", style = MaterialTheme.typography.h5, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text="Route to ${
                
                tokenToRouteName(oneMenu.wMenuRoute)
            
            }", style = MaterialTheme.typography.h5, maxLines = 1, overflow = TextOverflow.Ellipsis)

        }

    }

}