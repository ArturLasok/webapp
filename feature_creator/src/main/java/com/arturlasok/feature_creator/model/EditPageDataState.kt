package com.arturlasok.feature_creator.model

import androidx.compose.runtime.MutableState

data class EditPageDataState(
    val editPageName: MutableState<String>,
    val editPageIconName: String = "Icons.Filled.WebAsset",
    val editPageId: String,
    val editPageProjectId: String,
    val editPageRouteToken:String,
    val editPageInteractionState: MutableState<ProjectInteractionState>,
    val editPageSaveInteractionState: MutableState<ProjectInteractionState>,
    val editPageDeleteInteractionState: MutableState<ProjectInteractionState>,
)
