package com.arturlasok.feature_creator.model

data class LinkModuleState(
    var componentId:String = "",
    val componentName: String = "",
    val componentRes: Int,
    val componentLinkType: LinkType,
    val componentValue:String,
)

sealed class LinkType {
    object TEXT_MODULE_LINK : LinkType()
}