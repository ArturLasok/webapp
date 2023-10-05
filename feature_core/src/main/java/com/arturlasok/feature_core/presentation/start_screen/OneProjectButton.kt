package com.arturlasok.feature_core.presentation.start_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arturlasok.feature_core.R
import com.arturlasok.feature_core.data.datasource.api.model.WebProject
import com.arturlasok.feature_core.navigation.Screen

@Composable
fun OneProjectButton(
    navigateTo: (route: String) -> Unit,
    navigateUp:()->Unit,
    project: WebProject,
    setOpenProject:(projectId:String) -> Unit,
) {
    if(project.wProject_address.isNotEmpty()) {
        Surface(
            modifier = Modifier.padding(10.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 1.0f),
            shape = MaterialTheme.shapes.medium,
            border = BorderStroke(1.dp,MaterialTheme.colors.primary),
            elevation = 8.dp
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(100.dp).height(100.dp)
                    .clickable(onClick = { setOpenProject(project._id.toString()); })
            ) {

                Image(
                    bitmap = ImageBitmap.imageResource(
                        id = R.drawable.appbutton
                    ),
                    modifier = Modifier
                        .size(
                            80.dp,
                            80.dp
                        )
                        .padding(bottom = 4.dp)
                        .alpha(0.9f),
                    contentDescription = "Logo",
                    )
                Text(
                    text ="${project.wProject_address}",
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6,
                    textDecoration = TextDecoration.Underline,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 2.dp),
                    )
            }

        }






    } else {
        //add button icon
        Surface(
            modifier = Modifier.padding(10.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.primary.copy(alpha = 0.5f),
            border = BorderStroke(1.dp,MaterialTheme.colors.primary)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .clickable(onClick = {navigateTo(Screen.AddProjectScreen.route)})

            ) {

                Image(
                    bitmap = ImageBitmap.imageResource(
                        id = R.drawable.website1
                    ),
                    modifier = Modifier
                        .size(
                            80.dp,
                            80.dp
                        )
                        .padding(bottom = 4.dp)
                        .alpha(0.9f),
                    contentDescription = "Logo",
                )
                Text(
                    text ="Add next project",
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6,
                    textDecoration = TextDecoration.Underline,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 2.dp),
                )
            }

        }
    }

}