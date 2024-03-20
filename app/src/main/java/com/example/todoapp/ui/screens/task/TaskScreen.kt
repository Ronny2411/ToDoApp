package com.example.todoapp.ui.screens.task

import android.content.Context
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoTask
import com.example.todoapp.ui.theme.DarkGray
import com.example.todoapp.ui.viewmodels.SharedViewModel
import com.example.todoapp.util.Action

@Composable
fun TaskScreen(selectedTask : ToDoTask?,
               navigateToListScreen : (Action) -> Unit,
               sharedViewModel: SharedViewModel){
    var darkTheme : Color = Color.White
    if (isSystemInDarkTheme())
        darkTheme = DarkGray

    val title : String = sharedViewModel.title
    val description : String = sharedViewModel.description
    val priority : Priority = sharedViewModel.priority
    val date : String = sharedViewModel.date

    val context = LocalContext.current

//    BackHandler(onBackPressed = {navigateToListScreen(Action.NO_ACTION)})

    BackHandler {
        navigateToListScreen(Action.NO_ACTION)
    }

    Scaffold(backgroundColor = darkTheme,
        topBar = {TaskAppBar(selectedTask = selectedTask,
            navigateToListScreens = { action ->
                if (action == Action.NO_ACTION) {
                    navigateToListScreen(action)
                } else {
                    if (sharedViewModel.validateFields()) {
                        sharedViewModel.scheduleNotification(context,sharedViewModel.date)
                        navigateToListScreen(action)
                    } else {
                        displayToast(context = context)
                    }
                }
            }
        )
        }
    ) {
        TaskContent(
            title = title,
            onTitleChanged = {sharedViewModel.updateTitleLength(it)},
            description = description,
            onDescriptionChanged = {sharedViewModel.updateDescription(it)},
            priority = priority,
            onPriorityChanged = {sharedViewModel.updatePriority(it)},
            date = date,
            onDateChanged = {sharedViewModel.updateDate(it)}
        )
        Modifier.padding(it)
    }
}

fun displayToast(context: Context){
    Toast.makeText(context,"Please Fill All Fields",Toast.LENGTH_SHORT).show()
}

//@Composable
//fun BackHandler(
//    backDispatcher: OnBackPressedDispatcher?=LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
//    onBackPressed:()->Unit
//){
//    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)
//
//    val backCallBack = remember {
//        object : OnBackPressedCallback(true){
//            override fun handleOnBackPressed() {
//                currentOnBackPressed()
//            }
//        }
//    }
//
//    DisposableEffect(key1 = backDispatcher){
//        backDispatcher?.addCallback(backCallBack)
//
//        onDispose { backCallBack.remove() }
//    }
//}