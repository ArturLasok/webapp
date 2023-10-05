package com.arturlasok.feature_creator.presentation.creator_details

import android.app.Application
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.feature_core.data.repository.ApiInteraction
import com.arturlasok.feature_core.data.repository.RoomInteraction
import com.arturlasok.feature_core.datastore.DataStoreInteraction
import com.arturlasok.feature_core.domain.model.ModuleType
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.isOnline
import com.arturlasok.feature_creator.R
import com.arturlasok.feature_creator.model.CreatorDataState
import com.arturlasok.feature_creator.model.ProjectInteractionState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val dataStoreInteraction: DataStoreInteraction,
    private val isOnline: isOnline,
    private val fireAuth: FirebaseAuth,
    private val apiInteraction: ApiInteraction,
    private val roomInteraction: RoomInteraction,
    ) : ViewModel() {

    private val projectId = savedStateHandle.getStateFlow("projectId","")
    private val selectedPageToken = savedStateHandle.getStateFlow("selectedPageToken","")


    val creatorDataState = mutableStateOf(
        CreatorDataState(
            projectId = mutableStateOf(projectId.value),
            projectPagesList = mutableStateListOf(),
            projectGetPagesState = mutableStateOf(ProjectInteractionState.Idle),
            projectSelectedPageToken = mutableStateOf("")
        )
    )


    //name,icon,type
    private val listOfWebModules = mutableStateListOf<Triple<Int,Int,ModuleType>>()
    init {
        listOfWebModules.add(Triple(R.string.creator_module_text,R.drawable.appbutton,ModuleType.TEXT))
        listOfWebModules.add(Triple(R.string.creator_module_menu,R.drawable.smile,ModuleType.MENU))
        listOfWebModules.add(Triple(R.string.creator_module_img,R.drawable.appbutton,ModuleType.IMG))
        listOfWebModules.add(Triple(R.string.creator_module_link,R.drawable.appbutton,ModuleType.LINK))
        listOfWebModules.add(Triple(R.string.creator_module_spacer,R.drawable.appbutton,ModuleType.SPACER))
        listOfWebModules.add(Triple(R.string.creator_module_toplogo,R.drawable.appbutton,ModuleType.TOPLOGO))
        listOfWebModules.add(Triple(R.string.creator_module_news,R.drawable.appbutton,ModuleType.NEWS))

       openProjectIdToDataState()

    }


    val applicationContext = application
    fun screenRefresh() {
        openProjectIdToDataState()
    }
    fun setSelectedPageToken(token:String) {
        creatorDataState.value = creatorDataState.value.copy(projectSelectedPageToken = mutableStateOf(token))
    }
    fun getListOfProjectPages() {
        setGetPagesState(ProjectInteractionState.Interact)
        apiInteraction.ktor_getAllWebLayoutsFromProject(
            key = getFireAuth().currentUser?.uid ?: "",
            mail = getUserMail(),
            projectId = creatorDataState.value.projectId.value
        ).onEach { layoutList ->
            delay(3000)
            if(layoutList.first) {
                creatorDataState.value.projectPagesList.addAll(layoutList.second.filter {
                    it.wLayoutPageName.isNotEmpty() && it.wLayoutRouteToken.isNotEmpty()
                })
                setGetPagesState(ProjectInteractionState.Idle)
            } else {
                setGetPagesState(ProjectInteractionState.Error(message =UiText.StringResource(
                    R.string.creator_nonetwork,
                    "asd"
                ).asString(applicationContext)))
            }

        }.launchIn(viewModelScope)

    }
    fun openProjectIdToDataState() {
        getOpenProjectIdFromDataStore().take(1).onEach { projectId ->
            creatorDataState.value.projectId.value = projectId
            savedStateHandle["projectId"] = projectId
            getListOfProjectPages()
        }.launchIn(viewModelScope)
    }
    fun setGetPagesState(newState: ProjectInteractionState) {
        creatorDataState.value = creatorDataState.value.copy(projectGetPagesState = mutableStateOf(newState))
    }
    fun getListOfWebModules(): SnapshotStateList<Triple<Int, Int, ModuleType>> {
        return listOfWebModules
    }
    fun getOpenProjectIdFromDataStore() : Flow<String> {
        return dataStoreInteraction.getOpenProjectId()
    }
    fun darkFromStore() : Flow<Int> {
        return dataStoreInteraction.getDarkThemeInt()
    }
    fun haveNetwork() : Boolean {
        return isOnline.isNetworkAvailable.value
    }
    fun getFireAuth() : FirebaseAuth {
        return fireAuth
    }
    private fun getUserMail() : String {
        return getFireAuth().currentUser?.email ?: "empty"
    }
}