package com.arturlasok.feature_core.data.datasource.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arturlasok.feature_core.data.datasource.room.model.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessageToRoom(messageEntity: MessageEntity) : Long

    @Query("DELETE FROM MESSAGE_ROOM WHERE message_id_room=:messageIdRoom")
    suspend fun deleteMessageFromRoomById(messageIdRoom: Long) : Int

    @Query("SELECT * FROM MESSAGE_ROOM ORDER BY message_id_room DESC")
    suspend fun selectAllMessagesFromRoom() : List<MessageEntity>

    @Query("SELECT * FROM MESSAGE_ROOM WHERE message_id_room=:messageIdRoom")
    suspend fun selectOneMessageByIdFromRoom(messageIdRoom: Long) : MessageEntity

    @Query("SELECT count(*) FROM message_room WHERE message_viewedbyuser_room=0")
    fun selectCountMessagesFlowNotViewedFromRoom() : Flow<Int>

    @Query("UPDATE message_room SET message_viewedbyuser_room=:messageViewedLong WHERE message_id_room=:messageIdRoom")
    fun updateOneMessageSetViewedByUserInRoom(messageViewedLong: Long,messageIdRoom: Long)






}