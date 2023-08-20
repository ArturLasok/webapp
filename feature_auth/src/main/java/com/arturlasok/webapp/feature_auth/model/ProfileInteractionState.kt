package com.arturlasok.webapp.feature_auth.model

sealed class ProfileInteractionState {

    object Idle : ProfileInteractionState()

    class Interact(val action:()->Unit) :ProfileInteractionState()

    object OnComplete :ProfileInteractionState()

    class IsSuccessful(val message:String = "", val action:()->Unit) : ProfileInteractionState()

    class IsCanceled(val message:String = "") : ProfileInteractionState()

    class Error(val message:String = "", val action:()->Unit) : ProfileInteractionState()


}