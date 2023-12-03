package com.arturlasok.feature_creator.util


import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BorderOuter
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.arturlasok.feature_core.domain.model.ModuleText
import com.arturlasok.feature_creator.R

class IconsForTextModule {
    private val iconList = listOf<Triple<Int, ImageVector,ActionTextModule>>(
        Triple(R.string.creator_module_left, Icons.Filled.FormatAlignLeft,ActionTextModule.ALIGN_LEFT()),
        Triple(R.string.creator_module_center, Icons.Filled.FormatAlignCenter,ActionTextModule.ALIGN_CENTER()),
        Triple(R.string.creator_module_right, Icons.Filled.FormatAlignRight,ActionTextModule.ALIGN_RIGHT()),
        Triple(R.string.creator_module_justify, Icons.Filled.FormatAlignJustify,ActionTextModule.ALIGN_JUSTIFY()),
        Triple(R.string.creator_module_size, Icons.Filled.TextIncrease,ActionTextModule.TEXT_INCREASE()),
        Triple(R.string.creator_module_size, Icons.Filled.TextDecrease,ActionTextModule.TEXT_DECREASE()),
        Triple(R.string.creator_module_weight, Icons.Filled.FormatBold,ActionTextModule.TEXT_BOLD()),
        Triple(R.string.creator_module_underline, Icons.Filled.FormatUnderlined,ActionTextModule.TEXT_UNDERLINE()),
        Triple(R.string.creator_module_border, Icons.Filled.BorderOuter,ActionTextModule.TEXT_BORDER()),
        Triple(R.string.creator_module_copy, Icons.Filled.CopyAll,ActionTextModule.TEXT_COPY_ALL()),
        Triple(R.string.creator_module_paste, Icons.Filled.ContentPaste,ActionTextModule.TEXT_CONTENT_PASTE()),


    )
    fun returnIcons(): List<Triple<Int, ImageVector,ActionTextModule>> {
        return iconList
    }

}
data class ActionTextState(
    val component:Any,
    val componentAction: ActionTextModule
)
sealed class ActionTextModule() {
    class ALIGN_LEFT(val valueToSet:String= "left") : ActionTextModule()
    class ALIGN_RIGHT(val valueToSet:String= "right") : ActionTextModule()
    class ALIGN_CENTER(val valueToSet:String= "center"): ActionTextModule()
    class ALIGN_JUSTIFY(val valueToSet:String= "justify"): ActionTextModule()
    class TEXT_INCREASE: ActionTextModule()
    class TEXT_DECREASE: ActionTextModule()
    class TEXT_BOLD: ActionTextModule()
    class TEXT_UNDERLINE: ActionTextModule()
    class TEXT_COPY_ALL: ActionTextModule()
    class TEXT_CONTENT_PASTE: ActionTextModule()
    class TEXT_BORDER: ActionTextModule()
}
data class UniversalModule(
    val textAlign :String ="",
    val textHigh: String ="",
    val textWeight: String="",
    val textDecoration:String="",
    val textBorder:String="",

)
@Composable
fun isActionTextSelectedIcon(actionTextModule: ActionTextModule,textModule: Any) : Triple<Boolean, Color,FontWeight> {


    val currentValue = when(textModule)  {
        is ModuleText -> {
            UniversalModule(
                textAlign = textModule.wTextAlign,
                textHigh =  textModule.wTextH,
                textWeight = textModule.wTextWeight,
                textDecoration = textModule.wTextDecoration,
                textBorder = textModule.wTextBorder
            )
        }

        else -> {
            UniversalModule()
        }
    }

    return when(actionTextModule) {
        is ActionTextModule.ALIGN_LEFT -> {
            if(currentValue.textAlign=="left") {
                Triple(true,MaterialTheme.colors.primary,FontWeight.Bold)
            } else { Triple(false, Color.LightGray,FontWeight.Normal) }
        }
        is ActionTextModule.ALIGN_RIGHT -> {
            if(currentValue.textAlign=="right") {
                Triple(true,MaterialTheme.colors.primary,FontWeight.Bold)
            } else { Triple(false, Color.LightGray,FontWeight.Normal) }
        }
        is ActionTextModule.ALIGN_CENTER -> {
            if(currentValue.textAlign=="center") {
                Triple(true,MaterialTheme.colors.primary,FontWeight.Bold)
            } else { Triple(false, Color.LightGray,FontWeight.Normal) }
        }
        is ActionTextModule.ALIGN_JUSTIFY -> {
            if(currentValue.textAlign=="justify") {
                Triple(true,MaterialTheme.colors.primary,FontWeight.Bold)
            } else { Triple(false, Color.LightGray,FontWeight.Normal) }
        }
        is ActionTextModule.TEXT_INCREASE -> {
            if(currentValue.textHigh!="h6") {
                Triple(true,MaterialTheme.colors.primary,FontWeight.Bold)
            } else { Triple(false, Color.LightGray,FontWeight.Normal) }
        }
        is ActionTextModule.TEXT_DECREASE -> {
            if(currentValue.textHigh!="h1") {
                Triple(true,MaterialTheme.colors.primary,FontWeight.Bold)
            } else { Triple(false, Color.LightGray,FontWeight.Normal) }
        }
        is ActionTextModule.TEXT_BOLD -> {
            if(currentValue.textWeight=="bold") {
                Triple(true,MaterialTheme.colors.primary,FontWeight.Bold)
            } else { Triple(false, Color.LightGray,FontWeight.Normal) }
        }
        is ActionTextModule.TEXT_UNDERLINE-> {
            if(currentValue.textDecoration=="Underline") {
                Triple(true,MaterialTheme.colors.primary,FontWeight.Bold)
            } else { Triple(false, Color.LightGray,FontWeight.Normal) }
        }
        is ActionTextModule.TEXT_BORDER -> {
            if(currentValue.textBorder=="yes") {
                Triple(true,MaterialTheme.colors.primary,FontWeight.Bold)
            } else { Triple(false, Color.LightGray,FontWeight.Normal) }
        }
        else -> {
            Triple(false, MaterialTheme.colors.primary,FontWeight.Normal)
        }
    }

}
