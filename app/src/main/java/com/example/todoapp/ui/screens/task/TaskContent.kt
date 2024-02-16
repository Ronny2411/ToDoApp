package com.example.todoapp.ui.screens.task

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.todoapp.R
import com.example.todoapp.components.PriorityDropDown
import com.example.todoapp.data.models.Priority
import com.example.todoapp.ui.theme.DarkGray
import com.example.todoapp.ui.theme.LARGE_PADDING
import com.example.todoapp.ui.theme.MEDIUM_PADDING
import com.example.todoapp.ui.theme.taskItemContentColor

@Composable
fun TaskContent(
    title : String,
    onTitleChanged:(String)->Unit,
    description : String,
    onDescriptionChanged:(String)->Unit,
    priority: Priority,
    onPriorityChanged:(Priority)->Unit
){
    var darkTheme : Color = Color.White
    if (isSystemInDarkTheme())
        darkTheme = DarkGray
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkTheme)
            .padding(LARGE_PADDING)
    ) {
        OutlinedTextField(value = title,
            onValueChange = {onTitleChanged(it)},
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(id = R.string.title))},
            textStyle = MaterialTheme.typography.body1,
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = MaterialTheme.colors.taskItemContentColor,
                backgroundColor = Color.Transparent,
                textColor = MaterialTheme.colors.taskItemContentColor,
                unfocusedBorderColor = MaterialTheme.colors.taskItemContentColor,
                unfocusedLabelColor = MaterialTheme.colors.taskItemContentColor
            ))
        Spacer(modifier = Modifier.height(MEDIUM_PADDING))
        PriorityDropDown(priority = priority, onPrioritySelected = onPriorityChanged)
        OutlinedTextField(value = description,
            onValueChange = {onDescriptionChanged(it)},
            modifier = Modifier.fillMaxSize(),
            label = { Text(text = stringResource(id = R.string.description))},
            textStyle = MaterialTheme.typography.body1,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = MaterialTheme.colors.taskItemContentColor,
                backgroundColor = Color.Transparent,
                textColor = MaterialTheme.colors.taskItemContentColor,
                unfocusedBorderColor = MaterialTheme.colors.taskItemContentColor,
                unfocusedLabelColor = MaterialTheme.colors.taskItemContentColor
            ))
    }
}