package com.arturlasok.feature_core.data.repository


import android.os.Build
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import com.arturlasok.feature_core.BuildConfig
import com.arturlasok.feature_core.data.datasource.api.model.WebDomains
import com.arturlasok.feature_core.data.datasource.api.model.WebLayout
import com.arturlasok.feature_core.data.datasource.api.model.WebMenu
import com.arturlasok.feature_core.data.datasource.api.model.WebMessage
import com.arturlasok.feature_core.data.datasource.api.model.WebProject
import com.arturlasok.feature_core.data.datasource.api.model.WebUser
import com.arturlasok.feature_core.domain.model.Message
import com.arturlasok.feature_core.util.TAG
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import org.bson.codecs.kotlinx.BsonEncoder
import org.bson.types.ObjectId


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

    //get domains from ktor
    fun ktor_getDomainsData(host:String) : Flow<Triple<List<WebDomains>,List<WebDomains>,String>> = flow  {

        try {
            Log.i(TAG, "KTOR CHIP HOST: ${host}")
            val response: HttpResponse =
                ktorClient.post("$baseLink/${appbase}_getdomains") {
                    contentType(ContentType.Text.Plain)
                    setBody(host)
                }

            val listType = object : TypeToken<List<List<WebDomains>>>() {}.type

            val data = Gson().fromJson<List<List<WebDomains>>>(response.bodyAsText(),listType)

            Log.i(TAG, "KTOR get domains resp: ${response.status} bat: ${response.bodyAsText()} ${data[0].size} // ${data[1].size} // ${data[2].size} // ${data[2][0].wDomain_address}")
            emit(Triple(data[0],data[1],data[2][0].wDomain_address))
        }
        catch (e:java.lang.Exception) {
            Log.i(TAG, "KTOR get domains data exception: ${e.message}")
            //do nothing
            emit(Triple(listOf(), listOf(),"FALSE!"))
        }
    }
    //Get Projects from ktor
    fun ktor_getUserProjects(
        key:String,
        mail: String
    ) : Flow<List<WebProject>> = flow  {
        Log.i(TAG, "KTOR get user project mail: $mail , key: $key")
        try {
            val response: HttpResponse =
                ktorClient.post("$baseLink/${appbase}_getuserprojects") {
                    contentType(ContentType.Application.Json)
                    setBody(Pair(mail,key))
                }
            val listType = object : TypeToken<List<WebProject>>() {}.type

            val json = Gson().fromJson<List<WebProject>>(response.bodyAsText(),listType)

            Log.i(TAG, "KTOR get projects resp: ${response.status} js ${json.size}")
            //if(response.status.value==302) { emit(true) } else { emit(false) }
            emit(json)
        }
        catch (e:java.lang.Exception) {
            Log.i(TAG, "KTOR get user projects exception: ${e.message}")
            //do nothing
            emit(listOf(WebProject(wProject_sync = -1L)))
        }
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
    //
    fun ktor_updateOpenProjects(
        mail: String,
        key:String,
        tempToken:String,
    ) : Flow<Boolean> = flow {

        try {
            val response: HttpResponse =
                ktorClient.post("$baseLink/${appbase}_updateopenprojects") {
                    contentType(ContentType.Application.Json)
                    setBody(Triple(mail,key,tempToken))
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
    //Get all project layouts
    fun ktor_getAllWebLayoutsFromProject(
        key:String,
        mail: String,
        projectId:String,
    ) : Flow<Pair<Boolean,List<WebLayout>>> = flow  {
        try {
            val response: HttpResponse =
                ktorClient.post("$baseLink/${appbase}_getallprojectlayouts") {
                    contentType(ContentType.Application.Json)
                    setBody(Triple(key, mail, projectId))
                }
            Log.i(TAG, "KTOR layout response body: ${response.bodyAsText()}")

            val listType = object : TypeToken<List<WebLayout>>() {}.type
            val data = Gson().fromJson<List<WebLayout>>(response.bodyAsText(),listType)
            //val data = Json.decodeFromString<List<WebLayout>>(response.bodyAsText())
            //val data =  response.body<List<WebLayout>>()

            Log.i(TAG, "KTOR get layouts resp: ${response.status}")
            Log.i(TAG, "KTOR get layouts resp data after take: ${data}")
            if(response.status.value==200) { emit(Pair(true,data)) } else { emit(Pair(false,listOf())) }
        }
        catch (e:java.lang.Exception) {
            Log.i(TAG, "KTOR get layouts exception: ${e.message} ${e.localizedMessage} ${e.cause}")
            //do nothing
            emit(Pair(false,listOf<WebLayout>()))
        }
    }
    //Get one project layouts
    fun ktor_getOneWebLayouts(
        key:String,
        mail: String,
        id:String,
    ) : Flow<WebLayout> = flow  {
        try {
            val response: HttpResponse =
                ktorClient.post("$baseLink/${appbase}_getonelayout") {
                    contentType(ContentType.Application.Json)
                    setBody(Triple(key, mail, id.substringAfter("oid=").substringBefore("}")))
                }
            Log.i(TAG, "KTOR one layout response body: ${response.bodyAsText()}")

            val listType = object : TypeToken<WebLayout>() {}.type
            val data = Gson().fromJson<WebLayout>(response.bodyAsText(),listType)
            //val data = Json.decodeFromString<List<WebLayout>>(response.bodyAsText())
            //val data =  response.body<List<WebLayout>>()

            Log.i(TAG, "KTOR get one layout resp: ${response.status}")
            Log.i(TAG, "KTOR get one layout resp data after take: ${data}")
            if(response.status.value==200) { emit(data) } else { emit(WebLayout()) }
        }
        catch (e:java.lang.Exception) {
            Log.i(TAG, "KTOR get layouts exception: ${e.message} ${e.localizedMessage} ${e.cause}")
            //do nothing
            emit(WebLayout())
        }
    }
    //Update Page Name
    fun ktor_deleteOneLayout(
        layId: String,
        projectId: String,
        key: String,
        mail:String,
    ) : Flow<Boolean> = flow {
        try {
            //ktor update
            val response: HttpResponse =
                ktorClient.post("$baseLink/${appbase}_deletelayout") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        Triple(
                            mail,
                            key,
                            WebLayout(
                                _id = ObjectId(layId.substringAfter("oid=").substringBefore("}")),
                                wLayoutProjectId = ObjectId(projectId.substringAfter("oid=").substringBefore("}")),
                                wLayoutRouteToken ="token",
                                wLayoutPageName = "todelete",
                                wLayoutModule = null,
                                wLayoutModuleType = "",
                                wLayoutPosition = null,
                                wLayoutSort = 0L
                            )
                        )
                    )
                }
            Log.i(TAG, "KTOR delete layout: ${response.status}")
            //201 CREATED
            if(response.status.value==201) { emit(true) }
            else { emit(false) }
        } catch(e:java.lang.Exception) {
            Log.i(TAG, "KTOR delete layout exception: ${e.message}")
            //do nothing
            emit(false)
        }
    }
    //Update Page Name
    fun ktor_updatePage(
        pageName : String,
        pageId: String,
        projectId: String,
        key: String,
        mail:String,
    ) : Flow<Boolean> = flow {
        try {
            //ktor update
            val response: HttpResponse =
                ktorClient.post("$baseLink/${appbase}_updatepage") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        Triple(
                            mail,
                            key,
                            WebLayout(
                                _id = ObjectId(pageId.substringAfter("oid=").substringBefore("}")),
                                wLayoutProjectId = ObjectId(projectId.substringAfter("oid=").substringBefore("}")),
                                wLayoutRouteToken ="token",
                                wLayoutPageName = pageName,
                                wLayoutModule = null,
                                wLayoutModuleType = "",
                                wLayoutPosition = null,
                                wLayoutSort = 0L
                            )
                        )
                    )
                }
            Log.i(TAG, "KTOR update page: ${response.status}")
            //201 CREATED
            if(response.status.value==201) { emit(true) }
            else { emit(false) }
        } catch(e:java.lang.Exception) {
            Log.i(TAG, "KTOR update page exception: ${e.message}")
            //do nothing
            emit(false)
        }
    }
    //Get menus by place getmenusbyplace
    fun ktor_getMenusByPlace(
        key: String,
        mail:String,
        menuPlace:String,
        projectId: String,
    ) : Flow<Pair<Boolean,List<WebMenu>>> = flow {
        try {
            val response: HttpResponse =
                ktorClient.post("$baseLink/${appbase}_getmenusbyplace") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        Triple(
                            key,
                            mail,
                            WebMenu(
                                wMenuProjectId = ObjectId(projectId),
                                wMenuPlace = menuPlace,
                                wMenuRoute ="",
                                wMenuSort = 0L,
                                wMenuColor = "",
                                wMenuTextColor = "",
                                wMenuIconTint = "",
                            )
                        )
                    )
                }
            val listType = object : TypeToken<List<WebMenu>>() {}.type
            val data = Gson().fromJson<List<WebMenu>>(response.bodyAsText(),listType)
            Log.i(TAG, "KTOR get menus: ${response.status}")
            //200 OK
            if(response.status.value==200) { emit(Pair(true, data)) }
            else { emit(Pair(false, listOf())) }
        } catch(e:java.lang.Exception) {
            Log.i(TAG, "KTOR get menus exception: ${e.message}")
            //do nothing
            emit(Pair(false,listOf()))
        }

    }
    //Insert new menu to ktor
    fun ktor_insertNewMenu(
        key: String,
        mail:String,
        menuPlace:String,
        menuRoute:String,
        menuSort: Long = System.currentTimeMillis(),
        menuColor: String,
        menuTextColor:String,
        menuIconTint:String,
        projectId: String,

    ) : Flow<Boolean> = flow {
        try {
            val unixTime = System.currentTimeMillis() / 1000L
            val allowedChars = ('A'..'Z') + ('a'..'s') + ('0'..'9')
            val genToken: () -> String = fun(): String {
                return (1..16)
                    .map { allowedChars.random() }
                    .joinToString("")
            }
            val token = unixTime.toString() + "tok" + genToken.invoke()
            //ktor insert
            val response: HttpResponse =
                ktorClient.post("$baseLink/${appbase}_addnewmenu") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        Triple(
                            key,
                            mail,
                            WebMenu(
                                wMenuProjectId = ObjectId(projectId),
                                wMenuPlace = menuPlace,
                                wMenuRoute = menuRoute,
                                wMenuSort = menuSort,
                                wMenuColor = menuColor,
                                wMenuTextColor = menuTextColor,
                                wMenuIconTint = menuIconTint,
                            )
                        )
                    )
                }
            Log.i(TAG, "KTOR insert menu: ${response.status}")
            //201 CREATED
            if(response.status.value==201) { emit(true) }
            else { emit(false) }
        } catch(e:java.lang.Exception) {
            Log.i(TAG, "KTOR insert menu exception: ${e.message}")
            //do nothing
            emit(false)
        }
    }
    //Insert new Page to ktor
    fun ktor_insertNewPage(
        pageName : String,
        projectId: String,
        key: String,
        mail:String,
    ) : Flow<Boolean> = flow {
        try {
            val unixTime = System.currentTimeMillis() / 1000L
            val allowedChars = ('A'..'Z') + ('a'..'s') + ('0'..'9')
            val genToken: () -> String = fun(): String {
                return (1..16)
                    .map { allowedChars.random() }
                    .joinToString("")
            }
            val token = unixTime.toString() + "tok" + genToken.invoke()
            //ktor insert
            val response: HttpResponse =
                ktorClient.post("$baseLink/${appbase}_addnewpage") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        Triple(
                            mail,
                            key,
                        WebLayout(
                              //_id = ObjectId(""),
                              wLayoutProjectId = ObjectId(projectId),
                              wLayoutRouteToken = token,
                              wLayoutPageName = pageName,
                              wLayoutModule = null,
                              wLayoutModuleType = "",
                              wLayoutPosition = null,
                              wLayoutSort = 0L
                        )
                        )
                    )
                }
            Log.i(TAG, "KTOR insert page: ${response.status}")
            //201 CREATED
            if(response.status.value==201) { emit(true) }
            else { emit(false) }
        } catch(e:java.lang.Exception) {
            Log.i(TAG, "KTOR insert page exception: ${e.message}")
            //do nothing
            emit(false)
        }
    }
    //Insert project to ktor
    fun ktor_insertProject(
        projectName : String,
        projectAddress: String,
        projectDomain:String,
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
                ktorClient.post("$baseLink/${appbase}_addwebproject") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        WebProject(
                            wProject_title = projectName,
                            wProject_address = "$projectAddress.$projectDomain",
                            wProject_mail = mail,
                            wProject_token = key,
                            wProject_enabled = true,
                            wProject_lang = lang,
                            wProject_country = simCountry.uppercase(),
                            wProject_added = System.currentTimeMillis(),
                        )
                    )
                }
            Log.i(TAG, "KTOR insert project: ${response.status}")
            //201 CREATED
            if(response.status.value==201) { emit(true) }
            else { emit(false) }
        } catch(e:java.lang.Exception) {
            Log.i(TAG, "KTOR insert/update user exception: ${e.message}")
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