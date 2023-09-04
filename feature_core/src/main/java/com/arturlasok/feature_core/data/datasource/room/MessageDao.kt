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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMessageToRoom(messageListEntity: List<MessageEntity>) : List<Long>

    @Query("DELETE FROM MESSAGE_ROOM WHERE _id=:messageIdRoom")
    suspend fun deleteMessageFromRoomById(messageIdRoom: String) : Int
    @Query("DELETE FROM MESSAGE_ROOM")
    suspend fun deleteAllMessagesFromRoom() : Int
    @Query("SELECT * FROM MESSAGE_ROOM ORDER BY message_added_room DESC")
    suspend fun selectAllMessagesFromRoom() : List<MessageEntity>
    @Query("SELECT _id FROM MESSAGE_ROOM ORDER BY message_added_room DESC")
    suspend fun selectAllMessagesIdsFromRoom() : List<String>
    @Query("SELECT * FROM MESSAGE_ROOM WHERE _id=:messageIdRoom")
    suspend fun selectOneMessageByIdFromRoom(messageIdRoom: String) : MessageEntity

    @Query("SELECT count(*) FROM message_room WHERE message_viewedbyuser_room=0")
    fun selectCountMessagesFlowNotViewedFromRoom() : Flow<Int>

    @Query("UPDATE message_room SET message_viewedbyuser_room=:messageViewedLong WHERE message_id_room=:messageIdRoom")
    fun updateOneMessageSetViewedByUserInRoom(messageViewedLong: Long,messageIdRoom: Long) : Int






}