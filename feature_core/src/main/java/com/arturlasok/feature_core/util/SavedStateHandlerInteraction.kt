package com.arturlasok.feature_core.util

import androidx.lifecycle.SavedStateHandle
import androidx.savedstate.SavedStateRegistry

class SavedStateHandlerInteraction() {

    lateinit var ss: SavedStateHandle

    fun setSavedState(data: SavedStateRegistry) {
        ss = SavedStateHandle().apply {
            data.getSavedStateProvider("")?.let { this.setSavedStateProvider("", it) }
        }
    }
    fun getSavedState() : SavedStateHandle {
        return ss
    }
}