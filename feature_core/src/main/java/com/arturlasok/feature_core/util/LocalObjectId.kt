package com.arturlasok.feature_core.util

import android.util.Log
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonObject
import org.bson.types.ObjectId

object LocalObjectIdSerializer : KSerializer<ObjectId> {
    //override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("MongoObjectId",PrimitiveKind.STRING)
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    override val descriptor: SerialDescriptor = buildSerialDescriptor("oid",SerialKind.CONTEXTUAL)
    override fun serialize(encoder: Encoder, value: ObjectId) {
        Log.i(TAG,"ENCODER ! ${encoder.toString()} ")
        encoder.encodeString(value.toString().substringAfter("oid=").substringBefore("}"))
    }

    override fun deserialize(decoder: Decoder): ObjectId {
        Log.i(TAG,"DECODER ! ${(decoder as JsonDecoder).decodeJsonElement().jsonObject.get("\$oid").toString()} ")
        return ObjectId((decoder as JsonDecoder).decodeJsonElement().jsonObject.get("\$oid").toString())
    }
}
