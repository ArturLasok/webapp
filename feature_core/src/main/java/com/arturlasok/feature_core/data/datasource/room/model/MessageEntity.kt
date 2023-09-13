package com.arturlasok.feature_core.data.datasource.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message_room")
data class MessageEntity(

    @PrimaryKey
    @ColumnInfo(name = "_id")
    val _id: String = "",

    //@PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "message_id_room")
    val message_id_room: Long?,

    @ColumnInfo(name = "message_title_room")
    val message_title_room: String,

    @ColumnInfo(name = "message_content_room")
    val message_content_room: String,

    @ColumnInfo(name = "message_author_mail_room")
    val message_author_mail_room: String,

    @ColumnInfo(name = "message_cotext_room")
    val message_context_room: String,

    @ColumnInfo(name = "message_added_room")
    val message_added_room: Long,

    @ColumnInfo(name = "message_edited_room")
    val message_edited_room: Long,

    @ColumnInfo(name = "message_user_mail_room")
    val message_user_mail_room: String,

    @ColumnInfo(name = "message_user_lang_room")
    val message_user_lang_room:String,

    @ColumnInfo(name = "message_user_country_room")
    val message_user_country_room:String,

    @ColumnInfo(name = "message_viewedbyuser_room")
    val message_viewedbyuser_room:Long,

    @ColumnInfo(name = "message_sync_room")
    val message_sync_room: Long,


)
