package com.arturlasok.feature_creator.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.arturlasok.feature_core.data.datasource.api.model.WebLayout
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class CreatorDataState(
    val projectId: MutableState<String>,
    @Contextual
    val projectPagesList : SnapshotStateList<WebLayout>,
    val projectGetPagesState: MutableState<@Contextual ProjectInteractionState>,
    val projectSelectedPageToken: MutableState<String>,


)
