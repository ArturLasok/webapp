package com.arturlasok.feature_creator.model

data class SettingsModuleState(
    var componentId:String = "",
    val componentName :String = "",
    val componentRes: Int,
    val componentControllerType: ControllerType,
    val controllerValue:String,
)
sealed class ControllerType() {
    class SLIDER(val min:Int,val max:Int, val step:Int) : ControllerType()
    object SWITCH : ControllerType()

    object GROUP : ControllerType()
}