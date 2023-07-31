package com.arturlasok.webapp.feature_auth.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxColors
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AuthCheckBox(
    modifier: Modifier,
    textStyling: androidx.compose.ui.text.TextStyle,
    checkQuestion: String,
    checkValue: Boolean,
    setNewCheckValue:(newVal: Boolean)->Unit) {

    Row(modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {

        Text(text = checkQuestion,
            style = textStyling,
            modifier = Modifier.clickable(onClick = { setNewCheckValue(!checkValue)})
            )

        Checkbox(
            checked = checkValue,
            onCheckedChange = {
            setNewCheckValue(it)
            },
            colors = CheckboxDefaults.colors(
                checkmarkColor = Color.White,
                checkedColor = MaterialTheme.colors.primary,
                uncheckedColor = MaterialTheme.colors.primary.copy(alpha = 0.7f)
            )
        )

    }




}