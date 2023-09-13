package com.arturlasok.webapp.feature_auth.presentation.auth_messages

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SubdirectoryArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import com.arturlasok.feature_core.util.TAG
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_core.util.getDarkBoolean
import com.arturlasok.webapp.feature_auth.util.DateTimeType
import com.arturlasok.webapp.feature_auth.util.TimeMilisecondsTo
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageList(
    darkTheme: Int,
    fbAuth: FirebaseAuth,
    rowClicked : String,
    toDeleteRowSelectedItemId: String,
    messagesList: SnapshotStateList<Message>,
    deletedMessagesList: SnapshotStateList<String>,
    deleteAction:(messageId: String, tab: Int) ->Unit,
    reAction:(messageId:String, tab: Int)-> Unit,
    navigateTo: (route: String, tab: Int) -> Unit,
) {
    val isDark: Boolean = getDarkBoolean(isSystemInDark = isSystemInDarkTheme(), darkTheme)
    val messagesColumnState: LazyListState = rememberLazyListState()


    Log.i(TAG, "MessageList recompose, listsize: ${messagesList.size} ")

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                if(messagesList.size==0) {
                    Text(
                        text =  UiText.StringResource(com.arturlasok.feature_auth.R.string.auth_emptyMessageList,"asd").asString(),
                        style = MaterialTheme.typography.h3,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(top=30.dp)
                    )
                }
                else {
                    LazyColumn(
                        state = messagesColumnState,
                        modifier = Modifier.padding(bottom = 0.dp)
                    ) {

                        itemsIndexed(
                            items = messagesList,
                            key = { _, item -> item._did.toString() }
                        ) { index, oneMessage ->
                            AnimatedVisibility(
                                visible = !deletedMessagesList.contains(oneMessage._did.toString()),
                                exit = shrinkVertically(
                                    animationSpec = tween(
                                        delayMillis = 0,
                                        easing = FastOutSlowInEasing,
                                        durationMillis = 500
                                    )
                                ),
                                enter = expandVertically()
                            ) {
                                var thisFontWeight =
                                    if (oneMessage.dMessage_sync < 0 && oneMessage.dMessage_author_mail != (fbAuth.currentUser?.email
                                            ?: "unknown")
                                    ) FontWeight.Bold else FontWeight.Normal

                                Column(
                                    modifier = Modifier
                                        .clickable(onClick = {
                                            messagesList[index] =
                                                oneMessage.copy(dMessage_sync = 0L); thisFontWeight =
                                            FontWeight.Normal
                                            navigateTo(
                                                Screen.OneMessageScreen.routeWithArgs + "?mesId=${oneMessage._did.toString()}",
                                                if (oneMessage.dMessage_author_mail == (fbAuth.currentUser?.email
                                                        ?: "unknown")
                                                ) {
                                                    1
                                                } else {
                                                    0
                                                }
                                            )
                                        })
                                        .fillMaxWidth()
                                        .background(
                                            if (rowClicked == oneMessage._did.toString()) {
                                                MaterialTheme.colors.primary.copy(alpha = 0.6f)
                                            } else {
                                                if (toDeleteRowSelectedItemId == oneMessage._did.toString()) {
                                                    Color.Red.copy(alpha = 0.5f)
                                                } else {
                                                    Color.Transparent
                                                }
                                            }
                                        ).animateItemPlacement(
                                            animationSpec = tween(
                                                delayMillis = 0,
                                                easing = FastOutSlowInEasing,
                                                durationMillis = 1000
                                            )
                                        )

                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Column(Modifier.weight(0.6f)) {
                                            Row() {
                                                Column(
                                                    Modifier.padding(3.dp, end = 5.dp),
                                                    verticalArrangement = Arrangement.Center,
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    UserLogoCircle(
                                                        letter = oneMessage.dMessage_author_mail.substring(
                                                            0,
                                                            1
                                                        ) ?: "@",
                                                        letterSize = 24.sp,
                                                        color = MaterialTheme.colors.onSurface,
                                                        colorsecond = MaterialTheme.colors.onSurface,
                                                        size = 36
                                                    )
                                                }
                                                Column {

                                                    Text(
                                                        text = oneMessage.dMessage_author_mail + " | " + TimeMilisecondsTo(
                                                            DateTimeType.DATE_MEDIUM,
                                                            oneMessage.dMessage_added
                                                        ) + " " + TimeMilisecondsTo(
                                                            DateTimeType.TIME_H_AND_MIN,
                                                            oneMessage.dMessage_added
                                                        ),
                                                        maxLines = 1,
                                                        style = MaterialTheme.typography.h5,
                                                        overflow = TextOverflow.Ellipsis,
                                                        fontWeight = thisFontWeight
                                                    )
                                                    Row(verticalAlignment = Alignment.CenterVertically,) {
                                                        if (oneMessage.dMessage_context.isNotEmpty()) {
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
                                                        Text(
                                                            text = oneMessage.dMessage_title,
                                                            style = MaterialTheme.typography.h3,
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis,
                                                            fontWeight = thisFontWeight
                                                        )

                                                    }


                                                }
                                            }


                                        }
                                        Column {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                if (oneMessage.dMessage_author_mail != (fbAuth.currentUser?.email
                                                        ?: "unknown")
                                                ) {
                                                    Column {
                                                        ReplyIcon(tint = MaterialTheme.colors.primary) {

                                                            reAction(
                                                                oneMessage._did.toString(),
                                                                if (oneMessage.dMessage_author_mail == (fbAuth.currentUser?.email
                                                                        ?: "unknown")
                                                                ) {
                                                                    1
                                                                } else {
                                                                    0
                                                                }
                                                            )
                                                        }
                                                    }
                                                }
                                                Column {
                                                    if(toDeleteRowSelectedItemId.isEmpty()) {
                                                        DeleteIcon(tint = MaterialTheme.colors.primary) {

                                                            deleteAction(
                                                                oneMessage._did.toString(),
                                                                if (oneMessage.dMessage_author_mail == (fbAuth.currentUser?.email
                                                                        ?: "unknown")
                                                                ) {
                                                                    1
                                                                } else {
                                                                    0
                                                                }
                                                            )
                                                        }
                                                    } else {
                                                        DeleteIcon(tint = MaterialTheme.colors.primary.copy(alpha = 0.3f)) {

                                                        }
                                                    }

                                                }
                                            }
                                        }

                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .height(1.dp)
                                            .fillMaxWidth()
                                            .background(if(!isDark) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant)
                                    )
                                }

                            }


                        }

                    }
                }

            }




}