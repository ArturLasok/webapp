package com.arturlasok.feature_creator.presentation.creator_addproject

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_core.navigation.Screen
import com.arturlasok.feature_core.util.UiText
import com.arturlasok.feature_creator.R
import com.arturlasok.feature_creator.components.SubmitButton
import com.arturlasok.feature_creator.model.NewProjectDataState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProjectSuccess(
    newProjectDataState: NewProjectDataState,
    openLink: () -> Unit,
    shareLink: () -> Unit,
    darkTheme: Boolean,
    navigateTo: (route: String) -> Unit,
    copyToClipboard: () -> Unit,

) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {


        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = UiText.StringResource(R.string.creator_done, "asd").asString().uppercase(),
            style = MaterialTheme.typography.h1.copy(fontWeight = FontWeight.Medium)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            bitmap = ImageBitmap.imageResource(
                id = com.arturlasok.feature_creator.R.drawable.smile
            ),
            modifier = Modifier
                .size(
                    96.dp,
                    96.dp
                )
                .padding(bottom = 2.dp)
                .alpha(0.9f),
            contentDescription = "Logo",

            )
        Text(
            text = UiText.StringResource(R.string.creator_successInfo, "asd").asString(),
            style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Normal),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Chip(
            enabled = true,
            colors = ChipDefaults.chipColors(
                backgroundColor = Color.Transparent,
            ),
            border = BorderStroke(3.dp, MaterialTheme.colors.surface),
            onClick = { },
        ) {
            Text(
                modifier = Modifier.clickable(onClick = {
                    openLink()
                }).padding(12.dp),
                text = "https://www." + newProjectDataState.newProjectAddress + "." + newProjectDataState.newProjectDomain,
                style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.Medium),
                textDecoration = TextDecoration.Underline,
                color = if (darkTheme) {
                    MaterialTheme.colors.primary
                } else {
                    Color.Blue
                },
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row() {
            Chip(
                enabled = true,
                colors = ChipDefaults.chipColors(
                    disabledBackgroundColor = MaterialTheme.colors.background,
                    disabledContentColor = Color.LightGray,
                    backgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colors.primary
                ),
                border = BorderStroke(1.dp, MaterialTheme.colors.surface),
                onClick = { copyToClipboard() },
                leadingIcon = {
                    Icon(
                        Icons.Filled.CopyAll,
                        UiText.StringResource(R.string.creator_copy, "asd").asString(),
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            ) {
                Text(
                    UiText.StringResource(R.string.creator_copyLink, "asd").asString(),
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(end = 6.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Chip(
                enabled = true,
                colors = ChipDefaults.chipColors(
                    disabledBackgroundColor = MaterialTheme.colors.background,
                    disabledContentColor = Color.LightGray,
                    backgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colors.primary
                ),
                border = BorderStroke(1.dp, MaterialTheme.colors.surface),
                onClick = { shareLink() },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Share,
                        UiText.StringResource(R.string.creator_share, "asd").asString(),
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            ) {
                Text(
                    UiText.StringResource(R.string.creator_share, "asd").asString(),
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(end = 6.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = UiText.StringResource(R.string.creator_successInfoTwo, "asd").asString(),
            style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Normal),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        SubmitButton(
            buttonText = UiText.StringResource(
                R.string.creator_next_step,
                "asd"
            )
                .asString(),
            textPadding = 30.dp,
            buttonAction = {
                navigateTo(Screen.StartScreen.route)
            },
            buttonEnabled = true,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}