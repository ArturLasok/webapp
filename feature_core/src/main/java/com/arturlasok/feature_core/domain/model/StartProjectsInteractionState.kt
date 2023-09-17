package com.arturlasok.feature_core.domain.model


sealed class StartProjectsInteractionState {
    object Idle :StartProjectsInteractionState()

    object Interact: StartProjectsInteractionState()

    object OnComplete :StartProjectsInteractionState()

    object Empty : StartProjectsInteractionState()
    class IsSuccessful(val message: String? = null) : StartProjectsInteractionState()

    object IsCanceled : StartProjectsInteractionState()

    class Error(val message: String = "" ):StartProjectsInteractionState()

}
