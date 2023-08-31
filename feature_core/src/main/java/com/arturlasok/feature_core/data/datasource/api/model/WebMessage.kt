package com.arturlasok.feature_core.data.datasource.api.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class WebMessage(
    @Contextual
    val _id:Any? = null,
    val wMessage_id: Long? = null,
    val wMessage_title: String = "",
    val wMessage_content: String = "",
    val wMessage_author_mail: String ="",
    val wMessage_context: String = "",
    val wMessage_added: Long = 0L,
    val wMessage_edited: Long = 0L,
    val wMessage_user_mail: String = "",
    val wMessage_user_lang:String = "",
    val wMessage_user_country:String = "",
    val wMessage_viewedbyuser:Long = 0L,
    val wMessage_sync: Long = 0L,
)
