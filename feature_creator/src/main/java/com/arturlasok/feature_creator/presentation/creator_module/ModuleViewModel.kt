package com.arturlasok.feature_creator.presentation.creator_module

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.feature_core.data.datasource.api.model.WebLayout
import com.arturlasok.feature_core.data.repository.ApiInteraction
import com.arturlasok.feature_core.data.repository.RoomInteraction
import com.arturlasok.feature_core.datastore.DataStoreInteraction
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.isOnline
import com.arturlasok.feature_creator.R
import com.arturlasok.feature_creator.model.ModuleDataState
import com.arturlasok.feature_creator.model.ProjectInteractionState
import com.arturlasok.feature_creator.util.IconsForModule
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
@HiltViewModel
class ModuleViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val dataStoreInteraction: DataStoreInteraction,
    private val isOnline: isOnline,
    private val fireAuth: FirebaseAuth,
    private val apiInteraction: ApiInteraction,
    private val roomInteraction: RoomInteraction,
) : ViewModel() {

    private val projectPageName = savedStateHandle.getStateFlow("projectPageName","")
    private val projectProjectId = savedStateHandle.getStateFlow("projectProjectId","")
    private val projectSelectedPageId = savedStateHandle.getStateFlow("projectSelectedPageId","")

    val applicationContext = application
    val iconList = IconsForModule().returnIcons()
    val moduleDataState = mutableStateOf(
        ModuleDataState(
            projectId = mutableStateOf(projectProjectId.value),
            projectPageName = mutableStateOf(projectPageName.value),
            projectSelectedPageId = mutableStateOf(projectSelectedPageId.value),
            projectGetPagesState = mutableStateOf<ProjectInteractionState>(ProjectInteractionState.Idle),
            projectModulesList = mutableStateListOf()
       )
    )
    init {
        if(projectProjectId.value.isNotEmpty()) {
            getPageFromKtor(pageId = projectSelectedPageId.value)
        }
    }
    fun addModuleToList(layout: WebLayout, position: Int?) {
        if (moduleDataState.value.projectModulesList.isNotEmpty() && position!=null) {
            moduleDataState.value.projectModulesList
                .add(position ?: moduleDataState.value.projectModulesList.lastIndex, layout)

        }
        else {
            moduleDataState.value.projectModulesList.add(layout)
        }
    }
    fun setProjectId(id:String) {
        savedStateHandle["projectProjectId"] = id
        moduleDataState.value = moduleDataState.value.copy(projectId = mutableStateOf(id))
    }
    fun setProjectPageName(name:String) {
        savedStateHandle["projectPageName"] = name
        moduleDataState.value = moduleDataState.value.copy(projectPageName = mutableStateOf(name))
    }
    fun setProjectSelectedPageId(token:String) {
        savedStateHandle["projectSelectedPageId"] = token
        moduleDataState.value = moduleDataState.value.copy(projectSelectedPageId = mutableStateOf(token))
    }
    fun setProjectGetPageState(newState:ProjectInteractionState) {
        moduleDataState.value = moduleDataState.value.copy(projectGetPagesState = mutableStateOf(newState))
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
    fun getPageFromKtor(pageId: String) {

        setProjectGetPageState(ProjectInteractionState.Interact)
        apiInteraction.ktor_getOneWebLayouts(
            key = getFireAuth().currentUser?.uid ?: "",
            mail = getUserMail(),
            id = pageId
        ).onEach { layout ->
            if (layout.wLayoutPageName.isNotEmpty()) {

                setProjectPageName(layout.wLayoutPageName)
                setProjectSelectedPageId(pageId)
                setProjectId(layout.wLayoutProjectId.toString())
                //setEditPageRouteToken(layout.wLayoutRouteToken)
                setProjectGetPageState(ProjectInteractionState.OnComplete)
            } else {
                setProjectGetPageState(
                    ProjectInteractionState.Error(
                        message = UiText.StringResource(
                            R.string.creator_editmodule_api_error,
                            "asd"
                        ).asString(applicationContext)
                    )
                )
            }


        }.launchIn(viewModelScope)
    }
}