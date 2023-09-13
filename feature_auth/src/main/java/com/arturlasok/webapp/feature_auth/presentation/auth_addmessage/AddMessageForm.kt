package com.arturlasok.webapp.feature_auth.presentation.auth_addmessage

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardHide
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_auth.R
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.getDarkBoolean
import com.arturlasok.webapp.feature_auth.model.NewMessageDataState
import com.arturlasok.webapp.feature_auth.model.ProfileInteractionState
import com.arturlasok.webapp.feature_auth.presentation.components.UserMessageButton
import com.arturlasok.webapp.feature_auth.presentation.components.UserMessageType
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddMessageForm(
    setNewMessage:(message:String) ->Unit,
    setNewMessageTopic: (messageTopic: String) -> Unit,
    setNewMessageContext:(mContext:String)->Unit,
    sendClick:() -> Unit,
    darkTheme: Int,
    fbAuth: FirebaseAuth,
    newMessageDataState: NewMessageDataState
) {

    val isDark: Boolean = getDarkBoolean(isSystemInDark = isSystemInDarkTheme(), darkTheme)
    val textField = remember { mutableStateOf(TextFieldValue(text = newMessageDataState.newMessage)) }
    val topicField = remember { mutableStateOf(TextFieldValue(text = newMessageDataState.newMessageTopic)) }
    val toField = remember { mutableStateOf(TextFieldValue(text = newMessageDataState.newMessageTo)) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = newMessageDataState, block = {
        if(newMessageDataState.newMessageContext.isNotEmpty()) {
            topicField.value = TextFieldValue(text = newMessageDataState.newMessageTopic)
            toField.value = TextFieldValue(text = newMessageDataState.newMessageTo)
        }
        if(newMessageDataState.newMessageSendInteractionState.value == ProfileInteractionState.OnComplete) {
            Log.i(TAG, "-> CLEAR <---------------------")
            topicField.value = TextFieldValue(text = "")
            textField.value = TextFieldValue(text = "")
            toField.value = TextFieldValue(text = "")
        }
    } )
    AnimatedVisibility(
        visible = newMessageDataState.newMessageSendInteractionState.value == ProfileInteractionState.OnComplete,
        exit = fadeOut(
            animationSpec = tween(delayMillis = 0,easing = FastOutSlowInEasing, durationMillis = 1000)
        ),
        enter = fadeIn(
            animationSpec = tween(delayMillis = 0,easing = FastOutSlowInEasing, durationMillis = 1000)
        )
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            elevation = 20.dp,
            color = MaterialTheme.colors.background,
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
                .fillMaxWidth()
                .padding(top = 0.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth()
                )
                Text(UiText.StringResource(R.string.auth_messageSent, "asd")
                    .asString(),style = MaterialTheme.typography.h3)
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth()
                )
            }
        }
    }

    AnimatedVisibility(
        visible = newMessageDataState.newMessageSendInteractionState.value != ProfileInteractionState.OnComplete,
        exit = fadeOut(
            animationSpec = tween(delayMillis = 0,easing = FastOutSlowInEasing, durationMillis = 1000)
        ),
        enter = fadeIn(
            animationSpec = tween(delayMillis = 0,easing = FastOutSlowInEasing, durationMillis = 1000)
        )
    ) {

        Surface(
            shape = MaterialTheme.shapes.medium,
            elevation = 20.dp,
            color = MaterialTheme.colors.background,
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
                .fillMaxWidth()
                .padding(top = 0.dp)
        ) {

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier
                    .height(15.dp)
                    .fillMaxWidth())
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start)
                {
                    //To filed
                    OutlinedTextField(
                        enabled = false,
                        modifier = Modifier
                            .padding(start = 24.dp, end = 12.dp)
                            .fillMaxWidth(0.6f),
                        isError = false,
                        label = {
                            Text(UiText.StringResource(R.string.auth_to,"asd").asString())
                        },
                        value = toField.value,
                        onValueChange = { newInput ->
                           toField.value =  newInput
                        },
                        shape = MaterialTheme.shapes.large,
                        singleLine = true,
                        maxLines = 1,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = if(false) {  Color.Green } else { MaterialTheme.colors.primary.copy(alpha = 0.7f) },
                            unfocusedLabelColor =   MaterialTheme.colors.primary.copy(alpha = 0.7f) ,
                            focusedBorderColor = if(true) { Color.Green } else { MaterialTheme.colors.primary },
                        ),
                        textStyle = TextStyle.Default.copy(fontWeight = FontWeight.Bold),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Ascii,
                            imeAction = ImeAction.Default
                        ),


                    )
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 24.dp), horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Center) {
                        UserMessageButton(
                            buttonEnabled = newMessageDataState.newMessageSendInteractionState.value == ProfileInteractionState.Idle,
                            buttonVisible = true,
                            messageType = UserMessageType.NORMAL,
                            buttonText = UiText.StringResource(R.string.auth_send,"asd").asString(),
                            textPadding = 1.dp,
                            buttonAction = { sendClick() },
                            modifier = Modifier.padding(top =6.dp)
                        )
                    }

                }
                Spacer(modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth())
                //Topic field
                OutlinedTextField(
                    enabled = newMessageDataState.newMessageContext.isEmpty(),
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp)
                        .fillMaxWidth(),
                    isError = false,
                    label = {
                        Text(UiText.StringResource(R.string.auth_message_topic,"asd").asString())
                    },
                    value = topicField.value,
                    onValueChange = { newInput ->
                        if(newInput.text.length<120) {
                        topicField.value =  newInput
                        setNewMessageTopic(newInput.text) }
                    },
                    shape = MaterialTheme.shapes.large,
                    singleLine = true,
                    maxLines = 1,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = if(false) {  Color.Green } else { MaterialTheme.colors.primary.copy(alpha = 0.7f) },
                        unfocusedLabelColor =   MaterialTheme.colors.primary.copy(alpha = 0.7f) ,
                        focusedBorderColor = if(false) { Color.Green } else { MaterialTheme.colors.primary },
                    ),
                    textStyle = TextStyle.Default.copy(fontWeight = FontWeight.Bold),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {

                        keyboardController?.hide(); focusManager.clearFocus(true)

                    }),
                    trailingIcon = {
                        Row {
                            if(newMessageDataState.newMessageContext.isEmpty()) {
                                IconButton(onClick = {
                                    topicField.value = TextFieldValue(text = "")
                                    //isValidEmail.value = false
                                    setNewMessageTopic("")
                                }) {
                                    Icon(
                                        Icons.Filled.Clear,
                                        UiText.StringResource(R.string.auth_clear, "asd")
                                            .asString(),
                                        tint = Color.Red
                                    )

                                }
                            }
                            IconButton(onClick = {
                                keyboardController?.hide(); focusManager.clearFocus(true)
                            }) {
                                Icon(
                                    Icons.Filled.KeyboardHide, UiText.StringResource(R.string.auth_hideKeybord,"asd").asString(),
                                    tint = Color.Gray
                                )

                            }
                        }
                    }

                )
                Spacer(modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth())
                OutlinedTextField(
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp)
                        .fillMaxWidth()
                        .height(200.dp),
                    isError = false,
                    label = {

                        Text(UiText.StringResource(R.string.auth_message_content,"asd").asString())
                    },
                    value = textField.value,
                    onValueChange = { newInput ->
                        if(newInput.text.length<2500) {
                            textField.value = newInput
                            //isValidEmail.value = isValidEmailString(textField.value.text)
                            setNewMessage(newInput.text)
                        }
                                    },
                    shape = MaterialTheme.shapes.large,
                    singleLine = false,
                    maxLines = 20,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = if(false) {  Color.Green } else { MaterialTheme.colors.primary.copy(alpha = 0.7f) },
                        unfocusedLabelColor =   MaterialTheme.colors.primary.copy(alpha = 0.7f) ,
                        focusedBorderColor = if(false) { Color.Green } else { MaterialTheme.colors.primary },
                    ),
                    textStyle = TextStyle.Default.copy(fontWeight = FontWeight.Medium),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.Default
                    ),
                    keyboardActions = KeyboardActions(onDone = {

                        keyboardController?.hide(); focusManager.clearFocus(true)

                    }),
                    trailingIcon = {
                        Column(verticalArrangement = Arrangement.Top) {
                            IconButton(onClick = {
                            keyboardController?.hide(); focusManager.clearFocus(true)
                        }) {
                            Icon(
                                Icons.Filled.KeyboardHide, UiText.StringResource(R.string.auth_hideKeybord,"asd").asString(),
                                tint = Color.Gray
                            )

                        }
                            Spacer(modifier = Modifier.height(20.dp))
                            IconButton(onClick = {
                                textField.value = TextFieldValue(text = "")
                                //isValidEmail.value = false
                               setNewMessage("")
                            }) {
                                Icon(
                                    Icons.Filled.Clear, UiText.StringResource(R.string.auth_clear,"asd").asString(),
                                    tint = Color.Red
                                )

                            }


                        }

                    }

                )
                Spacer(modifier = Modifier
                    .height(15.dp)
                    .fillMaxWidth())
            }
        }

    }

}