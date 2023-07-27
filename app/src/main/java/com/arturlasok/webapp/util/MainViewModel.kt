package com.arturlasok.webapp.util

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    ) : ViewModel()
{
    private val maine = savedStateHandle.getStateFlow("main","")
    private val daine = savedStateHandle.getStateFlow("dain","")
    //new task state
    val newMainState = combine(maine,daine) {
            mainVal, dain ->
        NewMainState(
            mainV = mainVal
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),NewMainState())
    fun setMain(test: String) {
        //savedStateHandlerInteraction.getSavedState()["authLogin"] = login
        savedStateHandle["main"] = test
    }
    fun getMain() : String {
        return savedStateHandle["main"] ?: "nic"
    }
    fun getStateInfo(): String {
        return savedStateHandle.keys().joinToString {
            it
        } + " \n" +
                "hashcodes: "+savedStateHandle.hashCode()+ " "+savedStateHandle.savedStateProvider().hashCode()
    }
}