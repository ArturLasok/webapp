package com.arturlasok.feature_creator.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arturlasok.feature_creator.model.ModuleDataState
import com.arturlasok.feature_creator.util.ActionTextModule
import com.arturlasok.feature_creator.util.TextStringAlignToComposeTextAlign


@Composable
fun TextModuleComponent(
    updateOpenModuleId: (id: String) -> Unit,
    moduleDataState: ModuleDataState,
    setOpenTextModuleText:(text:String) -> Unit,
    setOpenTextModuleAction:(action: ActionTextModule) -> Unit,
    editMode: Boolean,
) {
    if(editMode) {
        BackHandler {
            updateOpenModuleId("")
        }
        Text(moduleDataState.projectOpenTextModule.wText,
            textAlign = TextStringAlignToComposeTextAlign(align = moduleDataState.projectOpenTextModule.wTextAlign),
            modifier= Modifier.fillMaxWidth().clickable(onClick = { setOpenTextModuleText(System.currentTimeMillis().toString())})
        )
        TextModuleText(
            moduleDataState=moduleDataState,
            setOpenTextModuleText = { text ->  setOpenTextModuleText(text) },
            setOpenTextModuleAction = { action -> setOpenTextModuleAction(action)  }
        )

    }
    else {
        Text(moduleDataState.projectOpenTextModule.wText,
            modifier= Modifier)


    }

}
