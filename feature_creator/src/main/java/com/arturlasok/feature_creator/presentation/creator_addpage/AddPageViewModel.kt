package com.arturlasok.feature_creator.presentation.creator_addpage

import android.app.Application
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WebAsset
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.feature_core.data.repository.ApiInteraction
import com.arturlasok.feature_core.data.repository.RoomInteraction
import com.arturlasok.feature_core.datastore.DataStoreInteraction
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.isOnline
import com.arturlasok.feature_creator.R
import com.arturlasok.feature_creator.model.NewPageDataState
import com.arturlasok.feature_creator.model.ProjectInteractionState
import com.arturlasok.feature_creator.util.IconsForPages
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val dataStoreInteraction: DataStoreInteraction,
    private val isOnline: isOnline,
    private val fireAuth: FirebaseAuth,
    private val apiInteraction: ApiInteraction,
    private val roomInteraction: RoomInteraction,
) : ViewModel() {

   private val newPageName = savedStateHandle.getStateFlow("newPageName","")
   private val newPageProjectId = mutableStateOf("")
   private val newPageIconName = savedStateHandle.getStateFlow("newPageIconName","Icons.Filled.WebAsset")

   val iconList = IconsForPages().returnIcons()

    val newPageDataState = mutableStateOf(
        NewPageDataState(
            newPageName = newPageName.value,
            newPageProjectId = newPageProjectId.value,
            newPageIconName = newPageIconName.value,
            newPageInsertState = mutableStateOf<ProjectInteractionState>(ProjectInteractionState.Idle)
        )
    )

    init {
        setNewPageProjectId()

    }
    val applicationContext = application


    fun setNewPageProjectId() {

            getOpenProjectIdFromDataStore().take(1).onEach {
                Log.i(TAG,"take project id: $it")
                newPageProjectId.value = it
                setNewPageProjectIdState(it)

            }.launchIn(viewModelScope)


    }
    fun setNewPageIconName(newName: String) {
        savedStateHandle["newPageIconName"] = newName
        newPageDataState.value = newPageDataState.value.copy(newPageIconName= newName)
    }
    fun setNewPageProjectIdState(projectId:String) {
        newPageDataState.value = newPageDataState.value.copy(newPageProjectId = projectId)
    }
    fun setNewPageName(name:String) {
        savedStateHandle["newPageName"] = name
        newPageDataState.value = newPageDataState.value.copy(newPageName=name)
    }
    fun getOpenProjectIdFromDataStore() : Flow<String> {
        return dataStoreInteraction.getOpenProjectId()
    }
    fun setNewPageInsertState(newState: ProjectInteractionState) {
        newPageDataState.value = newPageDataState.value.copy(newPageInsertState = mutableStateOf(newState))
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
    fun insertPage() {
        if(newPageDataState.value.newPageName.length in (1..22) && newPageDataState.value.newPageProjectId.isNotEmpty()) {
            apiInteraction.ktor_insertNewPage(
                pageName = newPageDataState.value.newPageName,
                pageIconName = newPageDataState.value.newPageIconName,
                projectId = newPageDataState.value.newPageProjectId ,
                key = getFireAuth().currentUser?.uid ?: "",
                mail = getUserMail()
            ).onEach { response->

                if(response) {
                    setNewPageInsertState(ProjectInteractionState.OnComplete)
                } else {
                    //addError
                    setNewPageInsertState(ProjectInteractionState.Error(message = UiText.StringResource(
                        R.string.creator_addPageApiError,
                        "asd"
                    ).asString(applicationContext)))
                }



            }.launchIn(viewModelScope)
        }
        else {
            //formError
            setNewPageInsertState(ProjectInteractionState.Error(message = UiText.StringResource(
                R.string.creator_addPageFormError,
                "asd"
            ).asString(applicationContext)))
        }
    }
}