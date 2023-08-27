package com.arturlasok.feature_core.domain.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    @Contextual
    val _did: Any? = null,
    val dMessage_id_room: Long? = null,
    val dMessage_title_room: String = "",
    val dMessage_content_room: String = "",
    val dMessage_author_mail_room: String ="",
    val dMessage_context_room: String = "",
    val dMessage_added_room: Long = 0L,
    val dMessage_edited_room: Long = 0L,
    val dMessage_user_mail_room: String = "",
    val dMessage_user_lang_room:String = "",
    val dMessage_user_country_room:String = "",
    val dMessage_viewedbyuser_room:Long = 0L,
    val dMessage_sync_room: Long = 0L,
)
