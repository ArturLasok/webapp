package com.arturlasok.feature_creator.presentation.creator_details

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturlasok.feature_core.data.datasource.api.model.WebMenu
import com.arturlasok.feature_core.data.repository.ApiInteraction
import com.arturlasok.feature_core.data.repository.RoomInteraction
import com.arturlasok.feature_core.datastore.DataStoreInteraction
import com.arturlasok.feature_core.domain.model.ModuleType
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.isOnline
import com.arturlasok.feature_creator.R
import com.arturlasok.feature_creator.model.CreatorDataState
import com.arturlasok.feature_creator.model.ProjectInteractionState
import com.arturlasok.feature_creator.model.ProjectMenuViewState
import com.arturlasok.feature_creator.util.IconsForPages
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
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
    private val selectedPageToken = savedStateHandle.getStateFlow("selectedPageToken","Home")
    private val selectedMenuToken = savedStateHandle.getStateFlow("selectedMenuToken","Home")

    val iconList = IconsForPages().returnIcons()

    val creatorDataState = mutableStateOf(
        CreatorDataState(
            projectId = mutableStateOf(projectId.value),
            projectPagesList = mutableStateListOf(),
            projectGetPagesState = mutableStateOf(ProjectInteractionState.Idle),
            projectSelectedPageToken = mutableStateOf(selectedPageToken.value),

            projectGetAllModulesState = mutableStateOf(ProjectInteractionState.Idle),
            projectModulesListPromPage = mutableStateListOf(),

            projectMenuLoadingState = mutableStateOf(ProjectInteractionState.Idle),
            projectPageMenuList = mutableStateListOf(),
            projectPageOriginalSortedList = mutableStateListOf(),

            projectSelectedMenuToken = mutableStateOf(selectedMenuToken.value),
            projectDeleteMenuState = mutableStateOf(ProjectInteractionState.Idle),
            projectInsertMenuState = mutableStateOf(ProjectInteractionState.Idle),
            projectReorderMenuState = mutableStateOf(ProjectInteractionState.Idle),
            projectMenuViewState = mutableStateOf(ProjectMenuViewState.Short)

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
        savedStateHandle["selectedPageToken"] = token
        setSelectedMenuToken(creatorDataState.value.projectSelectedPageToken.value)
        //Load all modules from this page
        getAllPageModules()
        getListOfMenusForPlace()
    }
    fun setSelectedMenuToken(token: String) {
        creatorDataState.value = creatorDataState.value.copy(projectSelectedMenuToken = mutableStateOf(token))
        savedStateHandle["selectedMenuToken"] = token
    }
    fun menuViewStateToFraction(state: ProjectMenuViewState) : Float {
        return when(state) {
            is ProjectMenuViewState.Short -> { 0.2f }
            is ProjectMenuViewState.Close -> { 0.0f }
            is ProjectMenuViewState.Open -> { 0.7f }
        }
    }
    fun getAllPageModules() {
        setGetAllModuleState(ProjectInteractionState.Interact)

        viewModelScope.launch {
            //delay(2000)
            setGetAllModuleState(ProjectInteractionState.Idle)
        }


    }
    fun deleteOneMenu(webMenu: WebMenu) {

        setDeleteMenuState(ProjectInteractionState.Interact)
        apiInteraction.ktor_deleteOneMenu(
            menu = webMenu,
            key = getFireAuth().currentUser?.uid ?: "",
            mail = getUserMail(),
        ).onEach { response ->
            if (response) {

                setDeleteMenuState(ProjectInteractionState.OnComplete)
                setSelectedPageToken(creatorDataState.value.projectSelectedPageToken.value)
            } else {

                //delError
                setDeleteMenuState(
                    ProjectInteractionState.Error(
                        message = UiText.StringResource(
                            R.string.creator_deleteMenuApiError,
                            "asd"
                        ).asString(applicationContext)
                    )
                )
            }
        }.launchIn(viewModelScope)

    }
    fun saveNewMenuOrder(menuListForPage: List<WebMenu>) {
        val menuReadyToSave: MutableList<WebMenu> = mutableListOf()

        menuListForPage.onEachIndexed { index,oneMenu ->

            menuReadyToSave.add(oneMenu.copy(_id = ObjectId(oneMenu._id.toString().substringAfter("oid=").substringBefore("}")),wMenuSort = index.toLong(),
                wMenuProjectId = ObjectId(oneMenu.wMenuProjectId.toString().substringAfter("oid=").substringBefore("}"))
            ))
        }
        menuReadyToSave.onEach { dane->
            Log.i(TAG,dane._id.toString())
        }



        setReorderMenuState(ProjectInteractionState.Interact)
        apiInteraction.ktor_updateMenuReorder(
            key = getFireAuth().currentUser?.uid ?: "",
            mail = getUserMail(),
            menuList = menuReadyToSave.toList()
        ).onEach { response->
            if(response) {
                setReorderMenuState(ProjectInteractionState.OnComplete)
                setSelectedPageToken(creatorDataState.value.projectSelectedPageToken.value)
            }
            else {
                setReorderMenuState(ProjectInteractionState.Error(
                    message = UiText.StringResource(
                        R.string.creator_reorderMenuError,
                        "asd"
                    ).asString(applicationContext)
                ))
            }

        }.launchIn(viewModelScope)

    }
    fun addRouteToThisMenu(routeToken:String, placeToken:String) {

        setInsertMenuState(ProjectInteractionState.Interact)
        apiInteraction.ktor_insertNewMenu(
            key = getFireAuth().currentUser?.uid ?: "",
            mail = getUserMail(),
            menuPlace = placeToken,
            menuRoute =routeToken,
            menuColor = "MenuColor",
            menuTextColor = "TextColor",
            menuIconTint = "TintIconColor",
            projectId = creatorDataState.value.projectId.value,
        ).onEach { response ->

            if (response) {

                setInsertMenuState(ProjectInteractionState.OnComplete)
                setSelectedPageToken(creatorDataState.value.projectSelectedPageToken.value)
            } else {

                //addError
                setInsertMenuState(
                    ProjectInteractionState.Error(
                        message = UiText.StringResource(
                            R.string.creator_addMenuApiError,
                            "asd"
                        ).asString(applicationContext)
                    )
                )
              }

            }.launchIn(viewModelScope)
    }
    fun insertMenu(autoLink:Boolean) {
        var error = false
        var isCompleted = false
        var finish = 0
        setInsertMenuState(ProjectInteractionState.Interact)
        Log.i(TAG,"+++add menu HOME")
        apiInteraction.ktor_insertNewMenu(
            key = getFireAuth().currentUser?.uid ?: "",
            mail = getUserMail(),
            menuPlace = creatorDataState.value.projectSelectedPageToken.value,
            menuRoute = creatorDataState.value.projectId.value,
            menuColor = "MenuColor",
            menuTextColor = "TextColor",
            menuIconTint = "TintIconColor",
            projectId = creatorDataState.value.projectId.value,
        ).onEach { response ->

            if(response) {
                Log.i(TAG,"+++add menu HOME")
            } else {
                error = true
                //addError
                setInsertMenuState(ProjectInteractionState.Error(message = UiText.StringResource(
                    R.string.creator_addMenuApiError,
                    "asd"
                ).asString(applicationContext)))
            }


        }.launchIn(viewModelScope).invokeOnCompletion {
            val otherSize = creatorDataState.value.projectPagesList.filter {
                it.wLayoutRouteToken != creatorDataState.value.projectSelectedPageToken.value
                        && it.wLayoutRouteToken != creatorDataState.value.projectId.value
            }.size


            if(autoLink && !error && otherSize>0) {

                setInsertMenuState(ProjectInteractionState.Interact)
                //autolink
                creatorDataState.value.projectPagesList.filter {
                    it.wLayoutRouteToken != creatorDataState.value.projectSelectedPageToken.value
                            && it.wLayoutRouteToken != creatorDataState.value.projectId.value
                }.onEach {

                    Log.i(TAG,"+++add menu ${it.wLayoutRouteToken}")
                    apiInteraction.ktor_insertNewMenu(
                        key = getFireAuth().currentUser?.uid ?: "",
                        mail = getUserMail(),
                        menuPlace = creatorDataState.value.projectSelectedPageToken.value,
                        menuRoute = it.wLayoutRouteToken,
                        menuColor = "MenuColor",
                        menuTextColor = "TextColor",
                        menuIconTint = "TintIconColor",
                        projectId = creatorDataState.value.projectId.value,
                    ).onEach { response ->

                        if(response) {
                            finish++

                            if(finish==otherSize) {
                                Log.i(TAG,">>>COMPLETE<<<<")
                                setInsertMenuState(ProjectInteractionState.OnComplete)
                                setSelectedPageToken(creatorDataState.value.projectSelectedPageToken.value)
                            }
                        } else {
                            //addError
                            setInsertMenuState(ProjectInteractionState.Error(message = UiText.StringResource(
                                R.string.creator_addMenuApiError,
                                "asd"
                            ).asString(applicationContext)))
                        }


                    }.launchIn(viewModelScope)

                }

            } else  {
                if(error) {
                    //add error
                    setInsertMenuState(ProjectInteractionState.Error(message = UiText.StringResource(
                        R.string.creator_addMenuApiError,
                        "asd"
                    ).asString(applicationContext)))
                } else {
                    setInsertMenuState(ProjectInteractionState.OnComplete)
                    setSelectedPageToken(creatorDataState.value.projectSelectedPageToken.value)
                }
            }

        }



    }
    fun getListOfMenusForPlace() {
        creatorDataState.value.projectPageMenuList.clear()
        setGetMenuLoadingState(ProjectInteractionState.Interact)
        apiInteraction.ktor_getMenusByPlace(
            key = getFireAuth().currentUser?.uid ?: "",
            mail = getUserMail(),
            menuPlace = creatorDataState.value.projectSelectedMenuToken.value,
            projectId = creatorDataState.value.projectId.value
        ).onEach { menuList ->

            if(menuList.first) {
                creatorDataState.value.projectPageMenuList.clear()
                creatorDataState.value.projectPageMenuList.addAll(menuList.second)
                creatorDataState.value.projectPageOriginalSortedList.clear()
                creatorDataState.value.projectPageOriginalSortedList.addAll(menuList.second)
                setGetMenuLoadingState(ProjectInteractionState.Idle)
            }
            else {
                setGetMenuLoadingState(ProjectInteractionState.Error(message =UiText.StringResource(
                    R.string.creator_nonetwork,
                    "asd"
                ).asString(applicationContext)))
            }


            Log.i(TAG,">>> get menu for place ${menuList.second.size}")
        }.launchIn(viewModelScope)
    }
    fun getListOfProjectPages() {
        setGetPagesState(ProjectInteractionState.Interact)
        apiInteraction.ktor_getAllWebLayoutsFromProject(
            key = getFireAuth().currentUser?.uid ?: "",
            mail = getUserMail(),
            projectId = creatorDataState.value.projectId.value
        ).onEach { layoutList ->

            if(layoutList.first) {
                creatorDataState.value.projectPagesList.clear()
                creatorDataState.value.projectPagesList.addAll(layoutList.second.filter {
                    it.wLayoutPageName.isNotEmpty() && it.wLayoutRouteToken.isNotEmpty()
                })

                setSelectedPageToken(creatorDataState.value.projectPagesList.first().wLayoutRouteToken)
                setGetPagesState(ProjectInteractionState.Idle)
                getListOfMenusForPlace()
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
    fun setMenuViewState(newState: ProjectMenuViewState) {
        when(creatorDataState.value.projectMenuViewState.value) {
            /*
            is ProjectMenuViewState.Close -> {
                creatorDataState.value = creatorDataState.value.copy(projectMenuViewState = mutableStateOf(ProjectMenuViewState.Short))
            }

             */
            is ProjectMenuViewState.Short -> {
                creatorDataState.value = creatorDataState.value.copy(projectMenuViewState = mutableStateOf(ProjectMenuViewState.Open))
            }
            is ProjectMenuViewState.Open -> {
                creatorDataState.value = creatorDataState.value.copy(projectMenuViewState = mutableStateOf(ProjectMenuViewState.Short))
            }
            else -> {}
        }

    }
    fun setReorderMenuState(newState: ProjectInteractionState) {
        creatorDataState.value = creatorDataState.value.copy(projectReorderMenuState = mutableStateOf(newState))
    }
    fun setInsertMenuState(newState: ProjectInteractionState) {
        creatorDataState.value = creatorDataState.value.copy(projectInsertMenuState = mutableStateOf(newState))
    }
    fun setDeleteMenuState(newState: ProjectInteractionState) {
        creatorDataState.value = creatorDataState.value.copy(projectDeleteMenuState = mutableStateOf(newState))
    }
    fun setGetPagesState(newState: ProjectInteractionState) {
        creatorDataState.value = creatorDataState.value.copy(projectGetPagesState = mutableStateOf(newState))
    }
    fun setGetAllModuleState(newState: ProjectInteractionState) {
        creatorDataState.value = creatorDataState.value.copy(projectGetAllModulesState = mutableStateOf(newState))
    }
    fun setGetMenuLoadingState(newState: ProjectInteractionState) {
        creatorDataState.value =creatorDataState.value.copy(projectMenuLoadingState = mutableStateOf(newState))
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