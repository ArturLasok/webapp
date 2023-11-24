package com.arturlasok.feature_creator.presentation.creator_module

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.arturlasok.feature_core.data.datasource.api.model.WebPageModule
import com.arturlasok.feature_creator.model.ModuleDataState
import com.arturlasok.feature_creator.model.ProjectInteractionState

@Composable
fun ModuleMenu(
    enabled: Boolean,
    barColor: Color,
    deleteOneModule: (id: String) -> Unit,
    moduleDataState: ModuleDataState,
    icons: List<Pair<String, ImageVector>>,
    oneElement: WebPageModule,
    index: Int,
    updateOpenModuleId:(id: String) -> Unit,
) {
    if(enabled) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(barColor)
                .zIndex(0.9f)
                .height(40.dp)
                //.clickable(onClick = {updateOpenModuleId("")})

        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        icons.find {
                            it.first == oneElement.wPageModuleType

                        }?.second ?: Icons.Filled.Info,
                        "Icon",
                        tint = MaterialTheme.colors.onBackground,
                        modifier = Modifier
                            .width(48.dp),
                    )
                    Text(
                        text = "Index:$index Sort:${oneElement.wPageModuleSort} id:${
                            oneElement._id.toString().substringAfter("oid=")
                                .substringBefore("}")
                        }", style = MaterialTheme.typography.h5
                    )
                }
            }

            IconButton(
                onClick = {
                    deleteOneModule(
                        oneElement._id.toString().substringAfter("oid=")
                            .substringBefore("}")
                    )
                },
                enabled = moduleDataState.projectDeletePageModuleState.value == ProjectInteractionState.Idle,
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
                        tint = if (moduleDataState.projectDeletePageModuleState.value == ProjectInteractionState.Idle) {
                            MaterialTheme.colors.error
                        } else {
                            Color.DarkGray
                        },
                        modifier = Modifier.width(32.dp),
                    )
                }
            }
        }
    }
}