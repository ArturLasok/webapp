package com.arturlasok.webapp.util

import android.util.Log
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bson.types.ObjectId

object LocalObjectIdSerializer : KSerializer<ObjectId> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("MongoObjectId",PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ObjectId) {
        Log.i(TAG,"ENCODER ! ${encoder.toString()} ")
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): ObjectId {
        Log.i(TAG,"DECODER ! ${decoder.toString()} ")
        return ObjectId(decoder.decodeString())
    }
}