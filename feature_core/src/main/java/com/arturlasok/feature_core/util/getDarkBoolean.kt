package com.arturlasok.feature_core.util

fun getDarkBoolean(isSystemInDark: Boolean,isDark: Int) : Boolean {

    return when (isDark) {
        0 -> {
            isSystemInDark
        }

        1 -> {
            false
        }

        2 -> {
            true
        }

        else -> {
            false
        }

    }
}