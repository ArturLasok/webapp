package com.arturlasok.feature_creator.presentation.creator_module

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.arturlasok.feature_creator.model.ModuleDataState

@Composable
fun EditModuleBar(
    moduleDataState: ModuleDataState,
    updateOpenModuleId:(id: String) -> Unit,
) {
    Column(modifier = Modifier
        .height(74.dp)
        .fillMaxWidth())
    {
        Surface(
            shape = MaterialTheme.shapes.medium,
            elevation = 6.dp,
            color = MaterialTheme.colors.background
            ,
            modifier = Modifier
                .padding(2.dp)
                //  .padding(top = 16.dp)
                .height(70.dp)
                .fillMaxWidth()
                .zIndex(1.0f)
        ) {

        }
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            //.border(1.dp, Color.Cyan)
            .padding(6.dp)
            .padding(start = 8.dp, end = 8.dp)
    ) {
        Text(
            text = "TEXT MODULE",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "PAGE: "+moduleDataState.projectPageName.value.uppercase(),
            style = MaterialTheme.typography.h5
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            //.horizontalScroll(rememberScrollState())
            .padding(6.dp, top = 20.dp)
            .zIndex(0.9f),
        verticalArrangement = Arrangement.Top,

        ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.zIndex(1.0f).fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.zIndex(1.0f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = { updateOpenModuleId("") }) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "Back",
                            tint = MaterialTheme.colors.error,
                            modifier = Modifier
                                .width(32.dp)
                                .zIndex(1.0f)

                        )
                        Text("Anuluj", style = MaterialTheme.typography.h5)
                    }

                }

            }
            Column(
                modifier = Modifier.zIndex(1.0f).padding(end = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
               Button(
                   onClick = { /*TODO*/ },
                   shape = MaterialTheme.shapes.medium,
                   colors = ButtonDefaults.buttonColors(
                       backgroundColor = Color.Green,
                       disabledBackgroundColor = Color.LightGray,
                       contentColor = Color.Black,
                       disabledContentColor = Color.DarkGray,
                       ),
                   content = {
                   Text("Zapisz zmiany", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
                   }

                   )

            }
        }
    }
}