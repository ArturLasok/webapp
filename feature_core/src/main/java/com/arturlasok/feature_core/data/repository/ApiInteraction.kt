package com.arturlasok.feature_core.data.repository


import android.os.Build
import android.util.Log
import androidx.compose.ui.text.intl.Locale
import com.arturlasok.feature_core.BuildConfig
import com.arturlasok.feature_core.data.datasource.api.model.WebMessage
import com.arturlasok.feature_core.domain.model.Message
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.data.datasource.api.model.WebUser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class ApiInteraction(
    private val ktorClient: HttpClient
) {
    fun messageFromDomainToApi(dMessage: Message) : WebMessage {
        return WebMessage(
            _id = dMessage._did.toString(),
            wMessage_id = dMessage.dMessage_id,
            wMessage_title =dMessage.dMessage_title,
            wMessage_content = dMessage.dMessage_content,
            wMessage_author_mail = dMessage.dMessage_author_mail,
            wMessage_context =dMessage.dMessage_context,
            wMessage_added=dMessage.dMessage_added,
            wMessage_edited=dMessage.dMessage_edited,
            wMessage_user_mail=dMessage.dMessage_user_mail,
            wMessage_user_lang=dMessage.dMessage_user_lang,
            wMessage_user_country=dMessage.dMessage_user_country,
            wMessage_viewedbyuser=dMessage.dMessage_viewedbyuser,
            wMessage_sync=dMessage.dMessage_sync,
        )

    }
    fun messageFromApiToDomain(wMessage: WebMessage) : Message {
        return Message(
            _did = wMessage._id,
            dMessage_id = wMessage.wMessage_id,
            dMessage_title= wMessage.wMessage_title,
            dMessage_content = wMessage.wMessage_content,
            dMessage_author_mail = wMessage.wMessage_author_mail,
            dMessage_context = wMessage.wMessage_context,
            dMessage_added = wMessage.wMessage_added,
            dMessage_edited = wMessage.wMessage_edited,
            dMessage_user_mail = wMessage.wMessage_user_mail,
            dMessage_user_lang= wMessage.wMessage_user_lang,
            dMessage_user_country =wMessage.wMessage_user_country,
            dMessage_viewedbyuser = wMessage.wMessage_viewedbyuser,
            dMessage_sync = wMessage.wMessage_sync
        )
    }
    fun messageListFromDomainToApi(messageList: List<Message>) : List<WebMessage> {
        return messageList.map {
            messageFromDomainToApi(it)
        }
    }
    fun messageListFromApiToDomain(wMessageList: List<WebMessage>) : List<Message> {
        return wMessageList.map {
            messageFromApiToDomain(it)
        }
    }
    private val baseLink = BuildConfig.BASEAPIURL
    private val appbase = BuildConfig.APPURL

    fun getServerTime() : Flow<String> = flow {
        emit(ktorClient.get("$baseLink/servertime-plu").bodyAsText())
    }
    //Get User from ktor
    fun ktor_getUserData(
        key:String,
        mail: String
    ) : Flow<WebUser> = flow  {

        try {
            val response: HttpResponse =
                ktorClient.post("$baseLink/${appbase}_getuserdata") {
                    contentType(ContentType.Application.Json)
                    setBody(Pair(key,mail))
                }
            val json = response.body<WebUser>()
            Log.i(TAG, "KTOR get user resp: ${response.status} js $json")
            //if(response.status.value==302) { emit(true) } else { emit(false) }
            emit(json)
        }
        catch (e:java.lang.Exception) {
            Log.i(TAG, "KTOR get user data exception: ${e.message}")
            //do nothing
            emit(WebUser())
        }
    }
    // checkmobiletoken
    fun ktor_checkMobileToken(
        key:String,
        mail: String
    ) : Flow<WebUser> = flow  {

        try {

            val response: HttpResponse =
                ktorClient.post("$baseLink/${appbase}_checkmobiletoken") {
                    contentType(ContentType.Application.Json)
                    setBody(Pair(key,mail))
                }
            val json = response.body<WebUser>()
            Log.i(TAG, "CHECK MOBILE TOKEN RESPONSE IN API INTER")
            Log.i(TAG, "KTOR get user resp: ${response.status} js $json")
            //if(response.status.value==302) { emit(true) } else { emit(false) }
            emit(json)
        }
        catch (e:java.lang.Exception) {
            Log.i(TAG, "KTOR get user data exception: ${e.message}")
            //do nothing
            emit(WebUser())
        }
    }
    //Update user verification to true
    fun ktor_updateUserVerificationToTrue(
        key:String,
        mail: String
    ) : Flow<Boolean> = flow {

        try {
            val response: HttpResponse =
                ktorClient.post("$baseLink/${appbase}_updateverificationtotrue") {
                    contentType(ContentType.Application.Json)
                    setBody(Pair(key,mail))
                }
            Log.i(TAG, "KTOR update verification user response: ${response.status}")
            if(response.status.value==302) { emit(true) } else { emit(false) }
        }
        catch (e:java.lang.Exception) {
            Log.i(TAG, "KTOR update verification user exception: ${e.message}")
            //do nothing
            emit(false)
        }

    }
    //Insert and update user on login or on registration
    fun ktor_insertOrUpdateUser(
        token: String = "",
        key: String,
        mail:String,
        simCountry:String,
    ) : Flow<Boolean> = flow {
        try {


            val lang = Locale.current.region
            val model = Build.MODEL
            //ktor insert
            val response: HttpResponse =
                ktorClient.post("$baseLink/${appbase}_addorupdateuser") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        WebUser(
                        webUserKey = key,
                        webUserToken = token,
                        webUserBlocked= false,
                        webUserMail = mail,
                        webVerified = false,
                        webLastLog ="server time",
                        webReg= "server time",
                        webSimCountry = simCountry.uppercase(),
                        webUserLang = lang,
                    )
                    )
                }
            Log.i(TAG, "3.  MOBILE UPDATED USER IN Api")
            Log.i(TAG, "KTOR insert/update user response: ${response.status}")
            //201 CREATED 302 FOUND
            if(response.status.value==201 || response.status.value==302) { emit(true) }
            else { emit(false) }
        } catch(e:java.lang.Exception) {
            Log.i(TAG, "KTOR insert/update user exception: ${e.message}")
            //do nothing
            emit(false)
        }
    }
    //Insert message to ktor
    fun ktor_insertMessage(
        key: String,
        message: WebMessage
    ) : Flow<Boolean> = flow {
        try {

            //ktor insert
            val response: HttpResponse =
                ktorClient.post("$baseLink/${appbase}_addmessage") {
                    contentType(ContentType.Application.Json)
                    setBody(Pair(key,message))
                }
            Log.i(TAG, "KTOR insert message response: ${response.status}")
            //200 OK
            if(response.status.value==200) { emit(true) }
            else { emit(false) }
        } catch(e:java.lang.Exception) {
            Log.i(TAG, "KTOR insert message exception: ${e.message}")
            //do nothing
            emit(false)
        }
    }
    fun ktor_getAllMessagesFromUser(
        key:String,
        mail: String,
        listOfMessageIdNowInRoom: List<String>,
    ) : Flow<List<WebMessage>> = flow  {


        Log.i(TAG, "Room message id list before sent to ktor: ${ listOfMessageIdNowInRoom.size}")
        try {
            val response: HttpResponse =
                ktorClient.post("$baseLink/${appbase}_getallmessages") {
                    contentType(ContentType.Application.Json)
                    setBody(Triple(key,mail,  listOfMessageIdNowInRoom))
                }
            Log.i(TAG, "KTOR response body: ${response.bodyAsText()}")
            val listType = object : TypeToken<List<WebMessage>>() {}.type

            val data = Gson().fromJson<List<WebMessage>>(response.bodyAsText(),listType)

            Log.i(TAG, "KTOR get messages resp: ${response.status}")
            if(response.status.value==200) { emit(data) } else { emit(listOf()) }
        }
        catch (e:java.lang.Exception) {
            Log.i(TAG, "KTOR get messages exception: ${e.message}")
            //do nothing
            emit(listOf<WebMessage>())
        }
    }
    fun ktor_deleteOneMessage(
        key: String,
        mail: String,
        messageId: String
    ) : Flow<Boolean> = flow {
        try {

            //ktor insert
            val response: HttpResponse =
                ktorClient.post("$baseLink/${appbase}_deletemessage") {
                    contentType(ContentType.Application.Json)
                    setBody(Triple(key,mail,messageId))
                }
            Log.i(TAG, "KTOR delete one message response: ${response.status}")
            if(response.status.value==200) { emit(true) }
            else { emit(false) }
        } catch(e:java.lang.Exception) {
            Log.i(TAG, "KTOR delete on message exception: ${e.message}")
            //do nothing
            emit(false)
        }
    }
}