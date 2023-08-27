package com.arturlasok.webapp.feature_auth.data.repository


import android.os.Build
import android.util.Log
import androidx.compose.ui.text.intl.Locale
import com.arturlasok.feature_core.BuildConfig
import com.arturlasok.feature_core.domain.model.Message
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.webapp.feature_auth.data.datasource.api.model.WebUser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class ApiInteraction(
    private val ktorClient: HttpClient
) {

    private val baseLink = BuildConfig.BASEAPIURL
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
                ktorClient.post("$baseLink/web_getuserdata") {
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
    //Update user verification to true
    fun ktor_updateUserVerificationToTrue(
        key:String,
        mail: String
    ) : Flow<Boolean> = flow {

        try {
            val response: HttpResponse =
                ktorClient.post("$baseLink/web_updateverificationtotrue") {
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
        key: String,
        mail:String,
        simCountry:String,
    ) : Flow<Boolean> = flow {
        try {
            val lang = Locale.current.region
            val model = Build.MODEL
            //ktor insert
            val response: HttpResponse =
                ktorClient.post("$baseLink/web_addorupdateuser") {
                    contentType(ContentType.Application.Json)
                    setBody(WebUser(
                        webUserKey = key,
                        webUserToken = "empty",
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
            Log.i(TAG, "KTOR insert/update user response: ${response.status}")
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
        message: Message
    ) : Flow<Boolean> = flow {
        try {

            //ktor insert
            val response: HttpResponse =
                ktorClient.post("$baseLink/web_addmessage") {
                    contentType(ContentType.Application.Json)
                    setBody(Pair(key,message))
                }
            Log.i(TAG, "KTOR insert message response: ${response.status}")
            if(response.status.value==201) { emit(true) }
            else { emit(false) }
        } catch(e:java.lang.Exception) {
            Log.i(TAG, "KTOR insert message exception: ${e.message}")
            //do nothing
            emit(false)
        }
    }
}