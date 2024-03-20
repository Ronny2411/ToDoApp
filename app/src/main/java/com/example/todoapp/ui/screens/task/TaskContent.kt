package com.example.todoapp.ui.screens.task

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoapp.R
import com.example.todoapp.components.PriorityDropDown
import com.example.todoapp.data.models.Priority
import com.example.todoapp.ui.theme.DarkGray
import com.example.todoapp.ui.theme.LARGE_PADDING
import com.example.todoapp.ui.theme.MEDIUM_PADDING
import com.example.todoapp.ui.theme.taskItemContentColor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskContent(
    title : String,
    onTitleChanged:(String)->Unit,
    description : String,
    onDescriptionChanged:(String)->Unit,
    priority: Priority,
    onPriorityChanged:(Priority)->Unit,
    date: String,
    onDateChanged:(String)->Unit
){
    var darkTheme : Color = Color.White
    if (isSystemInDarkTheme())
        darkTheme = DarkGray
    val state = rememberDatePickerState()
    var showDialog by remember {
        mutableStateOf(false)
    }
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val selectedDate= state.selectedDateMillis?.let { Date(it) }?.let { dateFormat.format(it) }
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
        OutlinedTextField(value = date,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth().clickable { showDialog = true },
            label = { Text(text = "Date")},
            readOnly = true,
            textStyle = MaterialTheme.typography.body1,
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = MaterialTheme.colors.taskItemContentColor,
                backgroundColor = Color.Transparent,
                textColor = MaterialTheme.colors.taskItemContentColor,
                unfocusedBorderColor = MaterialTheme.colors.taskItemContentColor,
                unfocusedLabelColor = MaterialTheme.colors.taskItemContentColor
            ),
            trailingIcon = {
                IconButton(onClick = { showDialog = true }) {
                    Icon(imageVector = Icons.Default.DateRange,
                        contentDescription = "Date")
                }
            })
        Spacer(modifier = Modifier.height(MEDIUM_PADDING))
        PriorityDropDown(priority = priority,
            onPrioritySelected = onPriorityChanged)
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
    if (showDialog){
        DatePickerDialog(onDismissRequest = { showDialog = false },
            confirmButton = { Button(onClick = {
                onDateChanged(selectedDate.toString())
                showDialog = false }) {
                Text(text = "Confirm")
            } }) {
            DatePicker(state = state)
        }
    }
}


@Preview
@Composable
fun TaskContentPrev() {
    TaskContent(
        title = "",
        onTitleChanged = {},
        description = "",
        onDescriptionChanged = {},
        priority = Priority.HIGH,
        onPriorityChanged = {},
        date = "",
        onDateChanged = {}
    )
}