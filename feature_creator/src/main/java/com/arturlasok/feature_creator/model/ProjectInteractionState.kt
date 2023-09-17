package com.arturlasok.feature_creator.model


sealed class ProjectInteractionState {
    object Idle :ProjectInteractionState()

    object Interact: ProjectInteractionState()

    object OnComplete :ProjectInteractionState()

    class IsSuccessful(val message: String? = null) : ProjectInteractionState()

    object IsCanceled : ProjectInteractionState()

    class Error(val message: String = "" ):ProjectInteractionState()

}
