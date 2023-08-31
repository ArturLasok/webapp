package com.arturlasok.feature_core.domain.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    @Contextual
    val _did: Any? = null,
    val dMessage_id: Long? = null,
    val dMessage_title: String = "",
    val dMessage_content: String = "",
    val dMessage_author_mail: String ="",
    val dMessage_context: String = "",
    val dMessage_added: Long = 0L,
    val dMessage_edited: Long = 0L,
    val dMessage_user_mail: String = "",
    val dMessage_user_lang:String = "",
    val dMessage_user_country:String = "",
    val dMessage_viewedbyuser:Long = 0L,
    val dMessage_sync: Long = 0L,
)
