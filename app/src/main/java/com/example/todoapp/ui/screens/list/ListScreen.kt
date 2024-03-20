package com.example.todoapp.ui.screens.list

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoapp.R
import com.example.todoapp.ui.theme.DarkGray
import com.example.todoapp.ui.theme.fabBarBackgroundColor
import com.example.todoapp.ui.theme.MediumGray
import com.example.todoapp.ui.viewmodels.SharedViewModel
import com.example.todoapp.util.Action
import com.example.todoapp.util.SearchAppBarState
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListScreen(navigateToTaskScreen : (taskId: Int) -> Unit,sharedViewModel: SharedViewModel){
    var darkTheme : Color = Color.White
    if (isSystemInDarkTheme())
        darkTheme = DarkGray

    val action = sharedViewModel.action

    val context = LocalContext.current

    val allTasks by sharedViewModel.allTasks.collectAsState()
    val searchTasks by sharedViewModel.searchTasks.collectAsState()
    val sortState by sharedViewModel.sortState.collectAsState()
    val lowPriorityTasks  by sharedViewModel.lowPriorityTasks.collectAsState(initial = listOf())
    val highPriorityTasks by sharedViewModel.highPriorityTasks.collectAsState(initial = listOf())
    val searchAppBarState:SearchAppBarState = sharedViewModel.searchAppBarState
    val searchTextState : String = sharedViewModel.searchTextState

    val scaffoldState = rememberScaffoldState()

    DisplaySnackBar(
        scaffoldState = scaffoldState,
        handleDatabaseActions = {
            sharedViewModel.handleDatabaseAction(action)
        },
        onUndoClicked = {sharedViewModel.updateAction(it)},
        taskTitle = sharedViewModel.title,
        action = action
    )

    Scaffold (
        scaffoldState = scaffoldState,
        topBar = {
        ListAppBar(
            sharedViewModel = sharedViewModel,
            searchAppBarState = searchAppBarState,
            searchTextState = searchTextState
        )}
        ,floatingActionButton = {
        ListFab(navigateToTaskScreen)
    }, backgroundColor = darkTheme){
        Modifier.padding(it)
        ListContent(
            allTasks,
            searchTasks,
            sortState,
            lowPriorityTasks,
            highPriorityTasks,
            {action,task->
                sharedViewModel.updateAction(action)
                sharedViewModel.updateTaskFields(task)
                scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()},
            searchAppBarState,
            navigateToTaskScreen)
    }
}

@Composable
fun ListFab(navigateToTaskScreen : (taskId:Int) -> Unit){
    FloatingActionButton(onClick = {
        navigateToTaskScreen(-1)
    }, backgroundColor = MaterialTheme.colors.fabBarBackgroundColor) {
        Icon(imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.add_Button),
            tint = Color.White)
    }
}

@Composable
fun DisplaySnackBar(
    scaffoldState: ScaffoldState,
    handleDatabaseActions:()->Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle : String,
    action : Action
){
    handleDatabaseActions()

    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action){
        scope.launch {
            if(action!=Action.NO_ACTION) {
                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = setMessage(action,taskTitle),
                    actionLabel = setActionLabel(action)
                )
                if (snackBarResult == SnackbarResult.ActionPerformed && action == Action.DELETE){
                    onUndoClicked(Action.UNDO)
                }
            }
        }
    }
}

private fun setMessage(action: Action,taskTitle: String):String{
    return if (action == Action.DELETE_ALL){
        "All Tasks Deleted"
    } else {
        "${action.name}: $taskTitle"
    }
}

private fun setActionLabel(action: Action):String{
    return if (action.name == "DELETE") "UNDO" else "OK"
}
