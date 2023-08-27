package com.arturlasok.webapp.feature_auth.data.repository

import android.util.Log
import com.arturlasok.feature_core.data.datasource.room.MessageDao
import com.arturlasok.feature_core.data.datasource.room.model.MessageEntity
import com.arturlasok.feature_core.domain.model.Message
import com.arturlasok.feature_core.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class RoomInteraction(val messageDao: MessageDao) {

    fun messageFromDomainToEntity(dMessage: Message) : MessageEntity {
        return MessageEntity(
            _id = dMessage._did.toString(),
            message_id_room = dMessage.dMessage_id_room,
            message_title_room =dMessage.dMessage_title_room,
            message_content_room =dMessage.dMessage_content_room,
            message_author_mail_room = dMessage.dMessage_author_mail_room,
            message_context_room=dMessage.dMessage_context_room,
            message_added_room=dMessage.dMessage_added_room,
            message_edited_room=dMessage.dMessage_edited_room,
            message_user_mail_room=dMessage.dMessage_user_mail_room,
            message_user_lang_room=dMessage.dMessage_user_lang_room,
            message_user_country_room=dMessage.dMessage_user_country_room,
            message_viewedbyuser_room=dMessage.dMessage_viewedbyuser_room,
            message_sync_room=dMessage.dMessage_sync_room,
        )

    }
    fun messageFromEntityToDomain(messageEntity: MessageEntity) : Message {
        return Message(
            _did = messageEntity._id,
            dMessage_id_room = messageEntity.message_id_room,
            dMessage_title_room = messageEntity.message_title_room,
            dMessage_content_room = messageEntity.message_content_room,
            dMessage_author_mail_room = messageEntity.message_author_mail_room,
            dMessage_context_room = messageEntity.message_context_room,
            dMessage_added_room = messageEntity.message_added_room,
            dMessage_edited_room = messageEntity.message_edited_room,
            dMessage_user_mail_room = messageEntity.message_user_mail_room,
            dMessage_user_lang_room = messageEntity.message_user_lang_room,
            dMessage_user_country_room = messageEntity.message_user_country_room,
            dMessage_viewedbyuser_room = messageEntity.message_viewedbyuser_room,
            dMessage_sync_room = messageEntity.message_sync_room
        )
    }
    fun messageListFromDomainToEntity(messageList: List<Message>) : List<MessageEntity> {
        return messageList.map {
            messageFromDomainToEntity(it)
        }
    }
    fun messageListFromEntityToDomain(messageEntityList: List<MessageEntity>) : List<Message> {
        return messageEntityList.map {
            messageFromEntityToDomain(it)
        }
    }
    fun getAllMessagesFromRoom() : Flow<List<Message>> = flow {
        try {
            emit(messageListFromEntityToDomain(messageDao.selectAllMessagesFromRoom()))
        }
        catch (e:Exception) {
            Log.i(TAG, "Room get all messages exception: ${e.message}")
            //do nothing
            emit(listOf())
        }
    }
    fun insertMessageToRoom(message: Message) : Flow<Boolean> = flow {
        try {
            val insert = messageDao.insertMessageToRoom(messageFromDomainToEntity(message))
            if(insert>0) { emit(true) } else { emit(false) }
        }
        catch (e:Exception) {
            Log.i(TAG, "Room insert message exception: ${e.message}")
            //do nothing
            emit(false)
        }
    }
    fun deleteMessageFromRoom(message: Message) : Flow<Boolean> = flow {
        try {
            val delete = messageDao.deleteMessageFromRoomById(message.dMessage_id_room ?: -1)
            if(delete>0) { emit(true) } else { emit(false) }
        }
        catch (e:Exception) {
            Log.i(TAG, "Room delete message exception: ${e.message}")
            //do nothing
            emit(false)
        }
    }


}