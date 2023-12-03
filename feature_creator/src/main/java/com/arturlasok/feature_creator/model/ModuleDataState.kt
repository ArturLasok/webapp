package com.arturlasok.feature_creator.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.arturlasok.feature_core.data.datasource.api.model.WebLayout
import com.arturlasok.feature_core.data.datasource.api.model.WebPageModule
import com.arturlasok.feature_core.domain.model.ModuleText
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class ModuleDataState(
    val projectId: MutableState<String>,

    val projectGetPagesState: MutableState<@Contextual ProjectInteractionState>,
    val projectSelectedPageId: MutableState<String>,
    val projectPageName: MutableState<String>,
    val projectPageRoute: MutableState<String>,

    val projectGetPageModuleState: MutableState<@Contextual ProjectInteractionState>,
    val projectInsertPageModuleState: MutableState<@Contextual ProjectInteractionState>,
    val projectReorderPageModuleState: MutableState<@Contextual ProjectInteractionState>,
    val projectDeletePageModuleState: MutableState<@Contextual ProjectInteractionState>,
    val projectModuleIdToDelete: MutableState<String>,
    val projectOpenModuleId: MutableState<String>,
    @Contextual
    val projectPagesList : SnapshotStateList<WebLayout>,
    @Contextual
    val projectModulesList: SnapshotStateList<WebPageModule>,
    @Contextual
    val projectOriginalModuleList: SnapshotStateList<WebPageModule>,

    @Contextual
    val projectOpenTextModule: ModuleText,
    @Contextual
    val projectOriginalTextModule: ModuleText,
    )
