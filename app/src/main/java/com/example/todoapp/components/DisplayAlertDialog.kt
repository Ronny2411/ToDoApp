package com.example.todoapp.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.todoapp.R
import com.example.todoapp.ui.theme.DarkGray
import com.example.todoapp.ui.theme.taskItemBackgroundColor
import com.example.todoapp.ui.theme.taskItemContentColor

@Composable
fun DisplayAlertDialog(
    title : String,
    message : String,
    openDialog : Boolean,
    closeDialog:()->Unit,
    onYesClicked:()->Unit
){
    var darkTheme : Color = Color.White
    if (isSystemInDarkTheme())
        darkTheme = DarkGray
    if (openDialog) {
        AlertDialog(
            title = { Text(text = title, fontSize = MaterialTheme.typography.h5.fontSize, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.taskItemContentColor)},
            text = { Text(text = message, fontSize = MaterialTheme.typography.subtitle1.fontSize, fontWeight = FontWeight.Normal,
                color = MaterialTheme.colors.taskItemContentColor)},
            onDismissRequest = { closeDialog() },
            confirmButton = { Button(onClick = {
                onYesClicked()
                closeDialog()
            }) {
                Text(text = stringResource(id = R.string.yes))
            }
            },
            dismissButton = { OutlinedButton(onClick = {
                closeDialog()
            }) {
                Text(text = stringResource(id = R.string.no))
            } },
            backgroundColor = darkTheme
        )
    }
}