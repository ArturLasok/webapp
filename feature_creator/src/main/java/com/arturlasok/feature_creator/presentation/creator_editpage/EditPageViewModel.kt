package com.arturlasok.feature_creator.presentation.creator_editpage

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.feature_core.data.repository.ApiInteraction
import com.arturlasok.feature_core.data.repository.RoomInteraction
import com.arturlasok.feature_core.datastore.DataStoreInteraction
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.isOnline
import com.arturlasok.feature_creator.R
import com.arturlasok.feature_creator.model.EditPageDataState
import com.arturlasok.feature_creator.model.ProjectInteractionState
import com.arturlasok.feature_creator.util.IconsForPages
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class EditPageViewModel@Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val dataStoreInteraction: DataStoreInteraction,
    private val isOnline: isOnline,
    private val fireAuth: FirebaseAuth,
    private val apiInteraction: ApiInteraction,
    private val roomInteraction: RoomInteraction,
) : ViewModel() {

    private val editPageId = savedStateHandle.getStateFlow("editPageId","")
    private val editPageName = savedStateHandle.getStateFlow("editPageName","")
    private val editPageProjectId = savedStateHandle.getStateFlow("editProjectId","")
    private val editPageRouteToken = savedStateHandle.getStateFlow("editPageRouteToken","")
    private val editPageIconName = savedStateHandle.getStateFlow("editPageIconName","Icons.Filled.WebAsset")

    val iconList = IconsForPages().returnIcons()

    val editPageDataState = mutableStateOf(
        EditPageDataState(
            editPageName = mutableStateOf(editPageName.value),
            editPageId = editPageId.value,
            editPageProjectId = editPageProjectId.value,
            editPageIconName = editPageIconName.value,
            editPageRouteToken = editPageRouteToken.value,
            editPageInteractionState = mutableStateOf<ProjectInteractionState>(ProjectInteractionState.Idle),
            editPageSaveInteractionState = mutableStateOf<ProjectInteractionState>(ProjectInteractionState.Idle),
            editPageDeleteInteractionState = mutableStateOf<ProjectInteractionState>(ProjectInteractionState.Idle)
        )
    )

    val applicationContext = application
    fun setEditPageIconName(newName: String) {
        savedStateHandle["editPageIconName"] = newName
        editPageDataState.value = editPageDataState.value.copy(editPageIconName= newName)
    }
    fun setEditPageDeleteState(newState: ProjectInteractionState) {
        editPageDataState.value = editPageDataState.value.copy(editPageDeleteInteractionState = mutableStateOf(newState))
    }
    fun setEditPageInteractionState(newState: ProjectInteractionState) {
        editPageDataState.value = editPageDataState.value.copy(editPageInteractionState = mutableStateOf(newState))
    }
    fun setEditPageSaveInteractionState(newState: ProjectInteractionState) {
        editPageDataState.value = editPageDataState.value.copy(editPageSaveInteractionState = mutableStateOf(newState))
    }
    fun setEditPageName(newName: String)  {
        savedStateHandle["editPageName "] = newName
        editPageDataState.value = editPageDataState.value.copy(editPageName = mutableStateOf(newName))
    }
    fun setEditPageId(id:String) {
        savedStateHandle["editPageId"] = id
        editPageDataState.value = editPageDataState.value.copy(editPageId = id)
    }
    fun setEditProjectId(id:String) {
        savedStateHandle["editProjectId"] = id
        editPageDataState.value = editPageDataState.value.copy(editPageProjectId =id)
    }
    fun setEditPageRouteToken(token:String) {
        savedStateHandle["editPageRouteToken"] = token
        editPageDataState.value = editPageDataState.value.copy(editPageRouteToken = token)
    }
    fun deletePage() {
        //todo delete all connected modules
        setEditPageDeleteState(ProjectInteractionState.Checking)
        apiInteraction.ktor_deleteOneLayout(
            layId = editPageDataState.value.editPageId,
            projectId = editPageDataState.value.editPageProjectId,
            key = getFireAuth().currentUser?.uid ?: "",
            routeToken = editPageDataState.value.editPageRouteToken,
            mail = getUserMail(),
        ).onEach {response->

            if(response) {
            setEditPageDeleteState(ProjectInteractionState.OnComplete) }
            else {
                //delete error
                setEditPageDeleteState(ProjectInteractionState.Error(message = UiText.StringResource(
                    R.string.creator_editpage_api_error,
                    "asd"
                ).asString(applicationContext)))
            }


        }.launchIn(viewModelScope)


    }
    fun updatePage() {

        if(editPageDataState.value.editPageName.value.length in (1..22) && editPageDataState.value.editPageProjectId.isNotEmpty()) {
            setEditPageSaveInteractionState(ProjectInteractionState.Interact)
            apiInteraction.ktor_updatePage(
                pageName = editPageDataState.value.editPageName.value,
                pageIconName = editPageDataState.value.editPageIconName,
                pageId = editPageDataState.value.editPageId,
                projectId = editPageDataState.value.editPageProjectId,
                key = getFireAuth().currentUser?.uid ?: "",
                mail = getUserMail(),
            ).onEach {response->

                if(response) {
                    setEditPageSaveInteractionState(ProjectInteractionState.OnComplete)
                } else {
                    //addError
                   setEditPageSaveInteractionState(ProjectInteractionState.Error(message = UiText.StringResource(
                        R.string.creator_editpage_api_error,
                        "asd"
                    ).asString(applicationContext)))
                }


            }.launchIn(viewModelScope)
        }
            else {
                //formError
                setEditPageSaveInteractionState(ProjectInteractionState.Error(message = UiText.StringResource(
                    R.string.creator_addPageFormError,
                    "asd"
                ).asString(applicationContext)))
            }

    }
    fun getPageFromKtor(pageId: String) {

            setEditPageInteractionState(ProjectInteractionState.Interact)
            apiInteraction.ktor_getOneWebLayouts(
                key = getFireAuth().currentUser?.uid ?: "",
                mail = getUserMail(),
                id = pageId
            ).onEach { layout ->

                if (layout.wLayoutPageName.isNotEmpty()) {

                    setEditPageName(layout.wLayoutPageName)
                    setEditPageId(pageId)
                    setEditProjectId(layout.wLayoutProjectId.toString())
                    setEditPageRouteToken(layout.wLayoutRouteToken)
                    setEditPageIconName(layout.wLayoutModuleType)
                    setEditPageInteractionState(ProjectInteractionState.OnComplete)
                } else {
                    setEditPageInteractionState(
                        ProjectInteractionState.Error(
                            message = UiText.StringResource(
                                R.string.creator_editpage_api_error,
                                "asd"
                            ).asString(applicationContext)
                        )
                    )
                }


            }.launchIn(viewModelScope)
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