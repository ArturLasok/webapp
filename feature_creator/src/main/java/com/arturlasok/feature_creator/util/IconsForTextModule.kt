package com.arturlasok.feature_creator.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatAlignJustify
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.FormatAlignRight
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.TextDecrease
import androidx.compose.material.icons.filled.TextIncrease
import androidx.compose.ui.graphics.vector.ImageVector
import com.arturlasok.feature_creator.R

class IconsForTextModule {
    private val iconList = listOf<Triple<Int, ImageVector,ActionTextModule>>(
        Triple(R.string.creator_module_left, Icons.Filled.FormatAlignLeft,ActionTextModule.ALIGN_LEFT),
        Triple(R.string.creator_module_center, Icons.Filled.FormatAlignCenter,ActionTextModule.ALIGN_CENTER),
        Triple(R.string.creator_module_right, Icons.Filled.FormatAlignRight,ActionTextModule.ALIGN_RIGHT),
        Triple(R.string.creator_module_justify, Icons.Filled.FormatAlignJustify,ActionTextModule.ALIGN_JUSTIFY),
        Triple(R.string.creator_module_size, Icons.Filled.TextIncrease,ActionTextModule.TEXT_INCREASE),
        Triple(R.string.creator_module_size, Icons.Filled.TextDecrease,ActionTextModule.TEXT_DECREASE),
        Triple(R.string.creator_module_weight, Icons.Filled.FormatBold,ActionTextModule.TEXT_BOLD),
        Triple(R.string.creator_module_underline, Icons.Filled.FormatUnderlined,ActionTextModule.TEXT_UNDERLINE),
        Triple(R.string.creator_module_copy, Icons.Filled.CopyAll,ActionTextModule.TEXT_COPY_ALL),
        Triple(R.string.creator_module_paste, Icons.Filled.ContentPaste,ActionTextModule.TEXT_CONTENT_PASTE),

    )
    fun returnIcons(): List<Triple<Int, ImageVector,ActionTextModule>> {
        return iconList
    }
}
enum class ActionTextModule {
    ALIGN_LEFT,
    ALIGN_RIGHT,
    ALIGN_CENTER,
    ALIGN_JUSTIFY,
    TEXT_INCREASE,
    TEXT_DECREASE,
    TEXT_BOLD,
    TEXT_UNDERLINE,
    TEXT_COPY_ALL,
    TEXT_CONTENT_PASTE
}