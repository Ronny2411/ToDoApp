package com.example.todoapp.ui.screens.task

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoapp.R
import com.example.todoapp.components.DisplayAlertDialog
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoTask
import com.example.todoapp.navigation.Screens
import com.example.todoapp.ui.theme.topAppBarBackgroundColor
import com.example.todoapp.ui.theme.topAppBarContentColor
import com.example.todoapp.util.Action

@Composable
fun TaskAppBar(selectedTask: ToDoTask?,
               navigateToListScreens:(Action) -> Unit){
    if (selectedTask == null) {
        NewTaskAppBar(navigateToListScreens = navigateToListScreens)
    } else {
        ExistingTaskAppBar(toDoTask = selectedTask, navigateToListScreens = navigateToListScreens)
    }
}

@Composable
fun NewTaskAppBar(
    navigateToListScreens:(Action) -> Unit
){
    TopAppBar(title = { Text(text = stringResource(R.string.add_task),
                            color = MaterialTheme.colors.topAppBarContentColor) },
            navigationIcon = { BackAction(onBackClicked = navigateToListScreens)},
            backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = { AddAction(onAddClicked = navigateToListScreens)}
            )
}

@Composable
fun BackAction(
    onBackClicked:(Action)-> Unit
){
    IconButton(onClick = { onBackClicked(Action.NO_ACTION) }) {
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back_arrow_icon),
            tint = MaterialTheme.colors.topAppBarContentColor
            )
    }
}

@Composable
fun AddAction(
    onAddClicked:(Action)-> Unit
){
    IconButton(onClick = { onAddClicked(Action.ADD) }) {
        Icon(imageVector = Icons.Filled.Check,
            contentDescription = stringResource(R.string.add_task),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun ExistingTaskAppBar(
    toDoTask: ToDoTask,
    navigateToListScreens:(Action) -> Unit
){
    TopAppBar(title = { Text(text = toDoTask.title,
        color = MaterialTheme.colors.topAppBarContentColor,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis) },
        navigationIcon = { CloseAction(onCloseClicked = navigateToListScreens) },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {
            ExistingTaskAppBarActions(toDoTask = toDoTask, navigateToListScreens = navigateToListScreens)
        }
    )
}

@Composable
fun ExistingTaskAppBarActions(
    toDoTask: ToDoTask,
    navigateToListScreens:(Action) -> Unit
){
    var openDialog by remember {
        mutableStateOf(false)
    }

    DisplayAlertDialog(title = stringResource(id = R.string.delete_task,toDoTask.title),
        message = stringResource(id = R.string.delete_task_confirmation,toDoTask.title),
        openDialog = openDialog,
        closeDialog = {openDialog = false},
        onYesClicked = {
            navigateToListScreens(Action.DELETE)
        })

    DeleteAction(onDeleteClicked = {
        openDialog = true
    })
    UpdateAction(onAddClicked = navigateToListScreens)
}


@Composable
fun UpdateAction(
    onAddClicked:(Action)-> Unit
){
    IconButton(onClick = { onAddClicked(Action.UPDATE) }) {
        Icon(imageVector = Icons.Filled.Check,
            contentDescription = stringResource(R.string.update_icon),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun DeleteAction(
    onDeleteClicked:()-> Unit
){
    IconButton(onClick = { onDeleteClicked() }) {
        Icon(imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.delete_icon),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun CloseAction(
    onCloseClicked:(Action)-> Unit
){
    IconButton(onClick = { onCloseClicked(Action.NO_ACTION) }) {
        Icon(imageVector = Icons.Filled.Close,
            contentDescription = stringResource(R.string.close_icon),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}
