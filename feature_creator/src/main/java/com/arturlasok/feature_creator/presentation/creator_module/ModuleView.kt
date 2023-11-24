package com.arturlasok.feature_creator.presentation.creator_module

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.arturlasok.feature_core.data.datasource.api.model.WebPageModule
import com.arturlasok.feature_core.util.ColorType
import com.arturlasok.feature_core.util.ExtraColors
import com.arturlasok.feature_creator.components.TextModuleComponent
import com.arturlasok.feature_creator.model.ModuleDataState
import com.arturlasok.feature_creator.util.ActionTextModule

@Composable
fun ModuleView(
    deleteOneModule: (id: String) -> Unit,
    moduleDataState: ModuleDataState,
    icons: List<Pair<String, ImageVector>>,
    oneElement: WebPageModule,
    index: Int,
    backgroundColor: Color,
    dataStoreDarkTheme: Boolean,
    updateOpenModuleId:(id: String) -> Unit,
    setOpenTextModuleText:(text:String) -> Unit,
    setOpenTextModuleAction:(action: ActionTextModule) -> Unit,
) {

    ModuleMenu(
        enabled = moduleDataState.projectOpenModuleId.value.contentEquals(oneElement._id.toString().substringAfter("oid=")
            .substringBefore("}")),
        barColor = ExtraColors(type = ColorType.DESIGNMENUMODULE, darktheme = dataStoreDarkTheme).copy(alpha = 0.3f),
        moduleDataState = moduleDataState,
        icons = icons,
        oneElement = oneElement,
        index = index,
        deleteOneModule = { id-> deleteOneModule(id)},
        updateOpenModuleId = { id-> updateOpenModuleId(id)}
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .zIndex(0.9f)
            .defaultMinSize(minHeight = 60.dp)
    ) {
        Text("Module", style = MaterialTheme.typography.h5)
        when(oneElement.wPageModuleType) {
            "Icons.Filled.Message" -> {
                TextModuleComponent(
                    updateOpenModuleId={id -> updateOpenModuleId(id) },
                    moduleDataState =moduleDataState,
                    setOpenTextModuleText = { text ->  setOpenTextModuleText(text) },
                    editMode = moduleDataState.projectOpenModuleId.value.isNotEmpty(),
                    setOpenTextModuleAction = {action -> setOpenTextModuleAction(action)  }

                )
            }
            else -> {
                Text("Unknown component")
            }
        }
    }

}