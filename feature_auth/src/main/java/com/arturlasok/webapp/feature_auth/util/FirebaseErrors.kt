package com.arturlasok.webapp.feature_auth.util

import com.arturlasok.feature_auth.R


fun fireBaseErrors(errorString: String) : Int {

    return when {

        errorString.contains("internal error") -> R.string.auth_fberror_internal

        errorString.contains("no user record") -> R.string.auth_fberror_nouser

        errorString.contains("network error") -> R.string.auth_fberror_nonetwork

        errorString.contains("password is invalid") -> R.string.auth_fberror_invalidpass

        errorString.contains("unusual activity") -> R.string.auth_fberror_blocked

        errorString.contains("already in use") -> R.string.auth_fberror_inuse

        errorString.contains("blocked") -> R.string.auth_fberror_blocked

        else -> R.string.auth_somethingWrong


    }

}