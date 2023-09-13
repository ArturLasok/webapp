package com.arturlasok.webapp.model

sealed class UserMobileCheckState {
    object Idle: UserMobileCheckState()

    object Same: UserMobileCheckState()
    object NotTheSame : UserMobileCheckState()

    object SameButBlocked : UserMobileCheckState()

    object NotTheSameAndBlocked : UserMobileCheckState()

    object LogOut: UserMobileCheckState()

    object Stay: UserMobileCheckState()

    object Block :UserMobileCheckState()
}
