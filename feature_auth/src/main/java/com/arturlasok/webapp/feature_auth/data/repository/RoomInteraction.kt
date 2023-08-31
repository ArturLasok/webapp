package com.arturlasok.webapp.feature_auth.data.repository

import android.util.Log
import com.arturlasok.feature_core.data.datasource.api.model.WebMessage
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
            message_id_room = dMessage.dMessage_id,
            message_title_room =dMessage.dMessage_title,
            message_content_room =dMessage.dMessage_content,
            message_author_mail_room = dMessage.dMessage_author_mail,
            message_context_room=dMessage.dMessage_context,
            message_added_room=dMessage.dMessage_added,
            message_edited_room=dMessage.dMessage_edited,
            message_user_mail_room=dMessage.dMessage_user_mail,
            message_user_lang_room=dMessage.dMessage_user_lang,
            message_user_country_room=dMessage.dMessage_user_country,
            message_viewedbyuser_room=dMessage.dMessage_viewedbyuser,
            message_sync_room=dMessage.dMessage_sync,
        )

    }
    fun messageFromEntityToDomain(messageEntity: MessageEntity) : Message {
        return Message(
            _did = messageEntity._id,
            dMessage_id = messageEntity.message_id_room,
            dMessage_title= messageEntity.message_title_room,
            dMessage_content = messageEntity.message_content_room,
            dMessage_author_mail = messageEntity.message_author_mail_room,
            dMessage_context = messageEntity.message_context_room,
            dMessage_added = messageEntity.message_added_room,
            dMessage_edited = messageEntity.message_edited_room,
            dMessage_user_mail = messageEntity.message_user_mail_room,
            dMessage_user_lang = messageEntity.message_user_lang_room,
            dMessage_user_country = messageEntity.message_user_country_room,
            dMessage_viewedbyuser = messageEntity.message_viewedbyuser_room,
            dMessage_sync = messageEntity.message_sync_room
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
    //fun getAllMessagesFromRoom() : Flow<List<MessageEntity>> = messageDao.selectAllMessagesFromRoom()
    fun getAllMessagesIdsFromRoom() : Flow<List<String>> = flow {
        try {
            emit(messageDao.selectAllMessagesIdsFromRoom())
        }
        catch (e:Exception) {
            Log.i(TAG, "Room get all messages exception: ${e.message}")
            //do nothing
            emit(listOf())
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
    fun getOneMessagesFromRoom(messageId:String) : Flow<Message> = flow {
        try {
            emit(messageFromEntityToDomain(messageDao.selectOneMessageByIdFromRoom(messageId)))
        }
        catch (e:Exception) {
            Log.i(TAG, "Room get all messages exception: ${e.message}")
            //do nothing
            emit(Message())
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
    fun insertAllMessageToRoom(messages: List<Message>) : Flow<Boolean> = flow {
        try {
            val insert = messageDao.insertAllMessageToRoom(messageListFromDomainToEntity(messages))
            Log.i(TAG, "Room insert size: ${insert.size}")
            if(insert.size>0) { emit(true) } else { emit(false) }
        }
        catch (e:Exception) {
            Log.i(TAG, "Room insert all messages exception: ${e.message}")
            //do nothing
            emit(false)
        }
    }
    fun deleteMessageFromRoom(message: Message) : Flow<Boolean> = flow {
        try {
            val delete = messageDao.deleteMessageFromRoomById(message.dMessage_id ?: -1)
            if(delete>0) { emit(true) } else { emit(false) }
        }
        catch (e:Exception) {
            Log.i(TAG, "Room delete message exception: ${e.message}")
            //do nothing
            emit(false)
        }
    }
    fun deleteAllMessagesFromRoom() : Flow<Boolean> = flow {
        try {
            val delete = messageDao.deleteAllMessagesFromRoom()
            if(delete>0) { emit(true) } else { emit(false) }
        }
        catch (e:Exception) {
            Log.i(TAG, "Room delete message exception: ${e.message}")
            //do nothing
            emit(false)
        }
    }
    fun updateOneMessageSetViewedByUserRoom(message: Message, viewedLong: Long) : Flow<Boolean> = flow {
        try {
            val update = messageDao.updateOneMessageSetViewedByUserInRoom(viewedLong,message.dMessage_id ?: -1)
            if(update>0) { emit(true) } else { emit(false) }
        }
        catch (e:Exception) {
            Log.i(TAG, "Room update viewed by user message exception: ${e.message}")
            //do nothing
            emit(false)
        }
    }


}