package com.arturlasok.webapp.feature_auth.presentation.auth_messages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.SubdirectoryArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arturlasok.feature_core.R
import com.arturlasok.feature_core.domain.model.Message
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.presentation.components.DeleteIcon
import com.arturlasok.feature_core.presentation.components.ReplyIcon
import com.arturlasok.feature_core.presentation.components.UserLogoCircle
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.getDarkBoolean
import com.arturlasok.webapp.feature_auth.util.DateTimeType
import com.arturlasok.webapp.feature_auth.util.TimeMilisecondsTo
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.concurrent.TimeUnit

@Composable
fun MessageList(
    darkTheme: Int,
    fbAuth: FirebaseAuth,
    messagesList: List<Message>,
    deleteAction:(messageId: String) ->Unit,
    reAction:(messageId:String)-> Unit,
    navigateTo: (route: String) -> Unit,
) {
    val isDark: Boolean = getDarkBoolean(isSystemInDark = isSystemInDarkTheme(), darkTheme)
    val messagesColumnState = rememberLazyListState()

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                LazyColumn(state= messagesColumnState) {

                    itemsIndexed(
                        items = messagesList,
                        key = { _,item -> item._did.toString() }
                    ) { index, oneMessage ->

                        val thisFontWeight = remember { if(oneMessage.dMessage_sync < 0 && oneMessage.dMessage_author_mail != (fbAuth.currentUser?.email
                                ?: "unknown")
                        ) FontWeight.Bold else FontWeight.Normal}

                        Column(modifier = Modifier
                            .clickable(onClick = {
                                navigateTo(Screen.OneMessageScreen.routeWithArgs + "?mesId=${oneMessage._did.toString()}")
                            })
                            .fillMaxWidth()){
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth()
                            ){
                                Column(Modifier.weight(0.6f)) {
                                    Row() {
                                        Column(Modifier.padding(3.dp,end= 5.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                            UserLogoCircle(
                                                letter =oneMessage.dMessage_author_mail.substring(0,1) ?: "@" ,
                                                letterSize = 24.sp,
                                                color = MaterialTheme.colors.onSurface,
                                                colorsecond = MaterialTheme.colors.onSurface,
                                                size = 36
                                            )
                                        }
                                        Column {

                                            Text(text=oneMessage.dMessage_author_mail+" | "+TimeMilisecondsTo(DateTimeType.DATE_MEDIUM,oneMessage.dMessage_added)+" "+TimeMilisecondsTo(DateTimeType.TIME_H_AND_MIN,oneMessage.dMessage_added),
                                                maxLines = 1,
                                                style = MaterialTheme.typography.h5,
                                                overflow = TextOverflow.Ellipsis,
                                                fontWeight = thisFontWeight)
                                            Row(verticalAlignment = Alignment.CenterVertically,) {
                                                if(oneMessage.dMessage_context.isNotEmpty()) {
                                                    Icon(
                                                        Icons.Filled.SubdirectoryArrowRight,
                                                        UiText.StringResource(
                                                            R.string.core_replyIcon,
                                                            "asd"
                                                        ).asString(),
                                                        tint = MaterialTheme.colors.error,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                }
                                                Text(text=oneMessage.dMessage_title + " (${oneMessage.dMessage_sync})",
                                                    style = MaterialTheme.typography.h3,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    fontWeight = thisFontWeight)

                                            }


                                        }
                                    }


                                }
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                        if(oneMessage.dMessage_author_mail!= (fbAuth.currentUser?.email ?: "unknown")) {
                                            Column {
                                                ReplyIcon(tint = MaterialTheme.colors.primary) {
                                                    reAction(oneMessage._did.toString())
                                                }
                                            }
                                        }
                                        Column {
                                            DeleteIcon(tint = MaterialTheme.colors.primary) {
                                                deleteAction(oneMessage._did.toString())
                                            }
                                        }
                                    }
                                }

                            }

                        }
                        Spacer(modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.primary))



                    }

                }

            }




}