package com.arturlasok.feature_creator.presentation.creator_module

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.feature_core.data.datasource.api.model.WebPageModule
import com.arturlasok.feature_core.data.repository.ApiInteraction
import com.arturlasok.feature_core.data.repository.RoomInteraction
import com.arturlasok.feature_core.datastore.DataStoreInteraction
import com.arturlasok.feature_core.domain.model.ModuleText
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.isOnline
import com.arturlasok.feature_creator.R
import com.arturlasok.feature_creator.model.ModuleDataState
import com.arturlasok.feature_creator.model.ProjectInteractionState
import com.arturlasok.feature_creator.util.ActionTextModule
import com.arturlasok.feature_creator.util.IconsForModule
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
    private val projectPageRoute = savedStateHandle.getStateFlow("projectPageRoute","")
    private val projectModuleToDelete = savedStateHandle.getStateFlow("moduleToDelete","")
    private val projectOpenModule = savedStateHandle.getStateFlow("openModule","")
    private val projectOpenTextModule = savedStateHandle.getStateFlow("projectOpenTextModule",ModuleText())
    private val projectOriginalTextModule = savedStateHandle.getStateFlow("projectOriginalTextModule",ModuleText())

    val deletedItems =  mutableStateListOf<String>()
    val applicationContext = application
    val iconList = IconsForModule().returnIcons()

    val moduleDataState = mutableStateOf(
        ModuleDataState(
            projectId = mutableStateOf(projectProjectId.value),
            projectPageName = mutableStateOf(projectPageName.value),
            projectSelectedPageId = mutableStateOf(projectSelectedPageId.value),
            projectGetPagesState = mutableStateOf<ProjectInteractionState>(ProjectInteractionState.Idle),
            projectPageRoute = mutableStateOf(projectPageRoute.value),

            projectGetPageModuleState = mutableStateOf<ProjectInteractionState>(ProjectInteractionState.Idle),
            projectInsertPageModuleState = mutableStateOf<ProjectInteractionState>(ProjectInteractionState.Idle),
            projectReorderPageModuleState = mutableStateOf<ProjectInteractionState>(ProjectInteractionState.Idle),
            projectDeletePageModuleState = mutableStateOf<ProjectInteractionState>(ProjectInteractionState.Idle),
            projectModuleIdToDelete = mutableStateOf(projectModuleToDelete.value),
            projectModulesList = mutableStateListOf(),
            projectOriginalModuleList = mutableStateListOf(),
            projectOpenModuleId = mutableStateOf(projectOpenModule.value),
            projectOpenTextModule = projectOpenTextModule.value,
            projectOriginalTextModule = projectOriginalTextModule.value
       )
    )
    init {
        if(projectProjectId.value.isNotEmpty()) {
            getPageFromKtor(pageId = projectSelectedPageId.value)
        }
    }
    private fun setSaveStateHandleProjectOpenTextModule() {
        savedStateHandle["projectOpenTextModule"] = moduleDataState.value.projectOpenTextModule
    }
    fun setOpenTextModuleText(text: String) {
        moduleDataState.value = moduleDataState.value.copy(projectOpenTextModule = ModuleText().copy(wText = text))
        setSaveStateHandleProjectOpenTextModule()
    }
    fun invokeOpenTextModuleAction(actionTextModule: ActionTextModule) {
        when(actionTextModule) {
            ActionTextModule.ALIGN_LEFT -> {
                setOpenTextModuleTextAlign("left")
            }
            ActionTextModule.ALIGN_RIGHT -> {
                setOpenTextModuleTextAlign("right")
            }
            ActionTextModule.ALIGN_CENTER -> {
                setOpenTextModuleTextAlign("center")
            }
            ActionTextModule.ALIGN_JUSTIFY -> {
                setOpenTextModuleTextAlign("justify")
            }
            ActionTextModule.TEXT_INCREASE -> {

            }
            ActionTextModule.TEXT_DECREASE -> {

            }
            ActionTextModule.TEXT_BOLD -> {

            }
            ActionTextModule.TEXT_UNDERLINE -> {

            }
            ActionTextModule.TEXT_COPY_ALL -> {

            }
            ActionTextModule.TEXT_CONTENT_PASTE -> {

            }
        }
    }
    fun setOpenTextModuleTextAlign(align:String) {
        moduleDataState.value = moduleDataState.value.copy(projectOpenTextModule = ModuleText().copy(wTextAlign = align))
        setSaveStateHandleProjectOpenTextModule()
    }
    fun setProjectOpenModuleId(id:String) {
        if(id!=moduleDataState.value.projectOpenModuleId.value) {
            savedStateHandle["openModule"] = id.substringAfter("oid=")
                .substringBefore("}")
            moduleDataState.value = moduleDataState.value.copy(
                projectOpenModuleId = mutableStateOf(
                    id.substringAfter("oid=")
                        .substringBefore("}")
                )
            )
        }
        else {

            savedStateHandle["openModule"] = ""
            moduleDataState.value = moduleDataState.value.copy(projectOpenModuleId = mutableStateOf(""))


        }
    }
    fun setProjectModuleIdToDelete(id: String) {
        savedStateHandle["moduleToDelete"] = id
        moduleDataState.value = moduleDataState.value.copy(projectModuleIdToDelete = mutableStateOf(id))

    }
    fun setProjectDeletePageModuleState(newState: ProjectInteractionState) {
        moduleDataState.value = moduleDataState.value.copy(projectDeletePageModuleState = mutableStateOf(newState))
        if(newState == ProjectInteractionState.Idle) {
            setProjectModuleIdToDelete("")
        }
    }
    fun setProjectReorderPageModuleState(newState: ProjectInteractionState) {
        moduleDataState.value = moduleDataState.value.copy(projectReorderPageModuleState = mutableStateOf(newState))
    }
    fun setProjectPageModuleList(pageModuleList: List<WebPageModule>) {
        moduleDataState.value = moduleDataState.value.copy(projectModulesList = pageModuleList.toMutableStateList())
    }
    fun setProjectOriginalPageModuleList(pageModuleList: List<WebPageModule>) {
        moduleDataState.value = moduleDataState.value.copy(projectOriginalModuleList = pageModuleList.toMutableStateList())
    }
    fun addModuleToList(pageModule: WebPageModule, position: Int?) {
      addPageModule(pageModule,position)
    }
    fun setProjectGetPageModuleState(newState: ProjectInteractionState) {
        moduleDataState.value = moduleDataState.value.copy(projectGetPageModuleState = mutableStateOf(newState))
    }
    fun setProjectInsertPageModuleState(newState: ProjectInteractionState) {
        moduleDataState.value = moduleDataState.value.copy(projectInsertPageModuleState = mutableStateOf(newState))
    }
    fun setProjectId(id:String) {
        savedStateHandle["projectProjectId"] = id
        moduleDataState.value = moduleDataState.value.copy(projectId = mutableStateOf(id))
    }
    fun setProjectPageName(name:String) {
        savedStateHandle["projectPageName"] = name
        moduleDataState.value = moduleDataState.value.copy(projectPageName = mutableStateOf(name))
    }
    fun setProjectPageRoute(route: String) {
        savedStateHandle["projectPageRoute"] = route
        moduleDataState.value = moduleDataState.value.copy(projectPageRoute = mutableStateOf(route))

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
    fun removeOneModuleFromModuleList(id:String) {
        moduleDataState.value.projectModulesList.removeIf {
            it._id.toString().substringAfter("oid=")
                .substringBefore("}") == id
        }
        moduleDataState.value.projectOriginalModuleList.removeIf {
            it._id.toString().substringAfter("oid=")
                .substringBefore("}") == id
        }
    }
    fun deleteModule() {
        setProjectDeletePageModuleState(ProjectInteractionState.Interact)
        apiInteraction.ktor_deleteOnePageModule(
            id = moduleDataState.value.projectModuleIdToDelete.value,
            projectId = moduleDataState.value.projectId.value,
            key = getFireAuth().currentUser?.uid ?: "",
            mail = getUserMail(),
        ).onEach { result->

            if(result) {
                setProjectOpenModuleId("")
                delay(500)
                deletedItems.add(moduleDataState.value.projectModuleIdToDelete.value)
                delay(1000)
                removeOneModuleFromModuleList(moduleDataState.value.projectModuleIdToDelete.value)
                deletedItems.remove(moduleDataState.value.projectModuleIdToDelete.value)
                setProjectDeletePageModuleState(ProjectInteractionState.OnComplete)


            }
            else {
                setProjectDeletePageModuleState(
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
    fun getPageModule(projectId: String, projectPageRoute: String) {

        setProjectGetPageModuleState(ProjectInteractionState.Interact)
        apiInteraction.ktor_getPageModuleListForPage(
            key = getFireAuth().currentUser?.uid ?: "",
            mail = getUserMail(),
            routeToken = projectPageRoute,
            projectId = projectId
        ).onEach { result->

            if(result.first) {
                //OK
                setProjectGetPageModuleState(ProjectInteractionState.OnComplete)
                setProjectPageModuleList(result.second)
                setProjectOriginalPageModuleList(result.second)

            } else {
                setProjectGetPageModuleState(
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
    fun updateSortPageModule(list: List<WebPageModule>,infoAfterUpdate:Boolean) {

        setProjectReorderPageModuleState(ProjectInteractionState.Interact)

        val rightOrder : MutableList<Pair<String,Long>> = mutableStateListOf()
        list.onEachIndexed { index,oneModule->

        rightOrder.add(Pair(oneModule._id.toString().substringAfter("oid=").substringBefore("}"),index.toLong()))

        }

        apiInteraction.ktor_sortPageModule(
            key = getFireAuth().currentUser?.uid ?: "",
            mail = getUserMail(),
            pgmList = rightOrder,
            projectId = moduleDataState.value.projectId.value.substringAfter("oid=").substringBefore("}")
        ).onEach {response->

            if(response) {
                if(infoAfterUpdate) {
                    setProjectReorderPageModuleState(ProjectInteractionState.OnComplete)
                    setProjectOriginalPageModuleList(moduleDataState.value.projectModulesList.toList())
                } else {
                    setProjectReorderPageModuleState(ProjectInteractionState.Idle)
                }

            }
            else {
                setProjectReorderPageModuleState(
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
    fun addPageModule(pageModule: WebPageModule,position: Int?) {

        setProjectInsertPageModuleState(ProjectInteractionState.Interact)

        apiInteraction.ktor_insertPageModule(
            key = getFireAuth().currentUser?.uid ?: "",
            mail = getUserMail(),
            routeToken = moduleDataState.value.projectPageRoute.value,
            moduleSort = position?.toLong() ?: pageModule.wPageModuleSort,
            type = pageModule.wPageModuleType,
            projectId = moduleDataState.value.projectId.value,
        ).onEach { result->

            if(result.first) {

                if (moduleDataState.value.projectModulesList.isNotEmpty() && (position != null) && (position > -1)) {

                    moduleDataState.value.projectModulesList
                        .add(position, pageModule.copy(_id = result.second))
                    moduleDataState.value.projectOriginalModuleList
                        .add(position, pageModule.copy(_id = result.second))

                    setProjectInsertPageModuleState(ProjectInteractionState.Idle)
                    updateSortPageModule(moduleDataState.value.projectModulesList.toList(),false)
                }
                else {
                    moduleDataState.value.projectModulesList.add(pageModule.copy(_id = result.second))
                    moduleDataState.value.projectOriginalModuleList.add(pageModule.copy(_id = result.second))

                    setProjectInsertPageModuleState(ProjectInteractionState.Idle)
                    updateSortPageModule(moduleDataState.value.projectModulesList.toList(),false)
                }

            }
            else
            {
                setProjectInsertPageModuleState(
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
                setProjectPageRoute(layout.wLayoutRouteToken)
                setProjectId(layout.wLayoutProjectId.toString())
                setProjectGetPageState(ProjectInteractionState.OnComplete)

                getPageModule(moduleDataState.value.projectId.value,moduleDataState.value.projectPageRoute.value)
            } else {
                setProjectGetPageState(
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
}