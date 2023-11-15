package com.arturlasok.feature_creator.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WebAsset
import androidx.compose.ui.graphics.vector.ImageVector

class IconsForPages {
    private val iconList = listOf<Pair<String, ImageVector>>(
        Pair("Icons.Filled.WebAsset", Icons.Filled.WebAsset),
        Pair("Icons.Filled.Message", Icons.Filled.Message),
        Pair("Icons.Filled.Info", Icons.Filled.Info),
        Pair("Icons.Filled.Home", Icons.Filled.Home),
        Pair("Icons.Filled.LocationOn", Icons.Filled.LocationOn),
        Pair("Icons.Filled.Settings", Icons.Filled.Settings)
    )
    fun returnIcons(): List<Pair<String, ImageVector>> {
        return iconList
    }
}