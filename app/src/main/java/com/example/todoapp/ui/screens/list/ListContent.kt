package com.example.todoapp.ui.screens.list

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoapp.R
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoTask
import com.example.todoapp.ui.theme.DarkGray
import com.example.todoapp.ui.theme.HighPriorityColor
import com.example.todoapp.ui.theme.LARGEST_PADDING
import com.example.todoapp.ui.theme.LARGE_PADDING
import com.example.todoapp.ui.theme.PRIORITY_INDICATOR_SIZE
import com.example.todoapp.ui.theme.TASK_ITEM_ELEVATION
import com.example.todoapp.ui.theme.taskItemBackgroundColor
import com.example.todoapp.ui.theme.taskItemContentColor
import com.example.todoapp.ui.theme.topAppBarContentColor
import com.example.todoapp.util.Action
import com.example.todoapp.util.RequestState
import com.example.todoapp.util.SearchAppBarState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ListContent(
    allTasks: RequestState<List<ToDoTask>>,
    searchTasks : RequestState<List<ToDoTask>>,
    sortState : RequestState<Priority>,
    lowPriorityTasks : List<ToDoTask>,
    highPriorityTasks : List<ToDoTask>,
    onSwipeToDelete:(Action, ToDoTask)->Unit,
    searchAppBarState: SearchAppBarState,
    navigateToTaskScreen:(taskID:Int)->Unit
){
    if (allTasks is RequestState.Loading || allTasks is RequestState.Idle){
        LoadingAnimation()
    }
    if (sortState is RequestState.Success){
        when{
            searchAppBarState == SearchAppBarState.TRIGGERED->{
                if(searchTasks is RequestState.Success){
                    HandleListContent(tasks = searchTasks.data,onSwipeToDelete, navigateToTaskScreen = navigateToTaskScreen)
                }
            }
            sortState.data == Priority.NONE->{
                if (allTasks is RequestState.Success){
                    HandleListContent(tasks = allTasks.data,onSwipeToDelete, navigateToTaskScreen = navigateToTaskScreen)
                }
            }
            sortState.data == Priority.LOW->{
                HandleListContent(tasks = lowPriorityTasks,onSwipeToDelete, navigateToTaskScreen = navigateToTaskScreen)
            }
            sortState.data == Priority.HIGH->{
                HandleListContent(tasks = highPriorityTasks,onSwipeToDelete, navigateToTaskScreen = navigateToTaskScreen)
            }
        }
    }
}

@Composable
fun HandleListContent(
    tasks: List<ToDoTask>,
    onSwipeToDelete:(Action, ToDoTask)->Unit,
    navigateToTaskScreen:(taskID:Int)->Unit
){
        if (tasks.isEmpty()) {
            EmptyContent()
        } else {
            DisplayTasks(tasks = tasks, onSwipeToDelete = onSwipeToDelete, navigateToTaskScreen = navigateToTaskScreen)
        }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DisplayTasks(
    tasks: List<ToDoTask>,
    onSwipeToDelete:(Action, ToDoTask)->Unit,
    navigateToTaskScreen:(taskID:Int)->Unit
){
    LazyColumn{
        items(items = tasks, key = {task->task.id}){ task->
            val dismissState = rememberDismissState()
            val degrees by animateFloatAsState(targetValue = if(dismissState.targetValue == DismissValue.Default)  0f else -45f,
                label = ""
            )
            val dismissDirection = dismissState.dismissDirection
            val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
            if (isDismissed && dismissDirection == DismissDirection.EndToStart){
                val scope = rememberCoroutineScope()
                scope.launch {
                    Log.d("task","${tasks.size}")
                    delay(300)
                    onSwipeToDelete(Action.DELETE, task)
                    Log.d("task","${tasks.size}")
                }
            }
            var itemAppeared by remember{ mutableStateOf(false) }
            LaunchedEffect(key1 = true){
                itemAppeared = true
            }
            AnimatedVisibility(visible = itemAppeared && !isDismissed,
                enter = expandVertically(tween(300)),
                exit = shrinkVertically(tween(300))
            ) {
                SwipeToDismiss(state = dismissState,
                    background = {RedBackground(degrees = degrees)},
                    directions = setOf(DismissDirection.EndToStart),
                    dismissThresholds = {FractionalThreshold(0.2f)},
                    dismissContent = {TaskItem(toDoTask = task, navigateToTaskScreen = navigateToTaskScreen)}
                )
            }
        }
    }
}

@Composable
fun LoadingAnimation(){
    var darkTheme : Color = Color.White
    if (isSystemInDarkTheme())
        darkTheme = DarkGray
    Column(
        Modifier
            .fillMaxSize()
            .background(darkTheme),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator()
    }
}

@Composable
fun RedBackground(degrees : Float) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(HighPriorityColor)
        .padding(LARGEST_PADDING),
        contentAlignment = Alignment.CenterEnd){
        Icon(modifier = Modifier.rotate(degrees),
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(id = R.string.delete_icon),
            tint = Color.White)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskItem(
    toDoTask: ToDoTask,
    navigateToTaskScreen:(taskID:Int)->Unit
){
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.taskItemBackgroundColor,
        shape = RectangleShape,
        elevation = TASK_ITEM_ELEVATION,
        onClick = {navigateToTaskScreen(toDoTask.id)}
    ) {
        Column(modifier = Modifier
            .padding(LARGE_PADDING)
            .fillMaxWidth()) {
            Row {
                Text(text = toDoTask.title,
                    color = MaterialTheme.colors.taskItemContentColor,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier.weight(8f))
                Box(modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.TopEnd){
                    Canvas(modifier = Modifier
                        .width(PRIORITY_INDICATOR_SIZE)
                        .height(
                            PRIORITY_INDICATOR_SIZE
                        )){
                        drawCircle(color = toDoTask.priority.color)
                    }
                }
            }
            Row {
                Text(text = toDoTask.description,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colors.taskItemContentColor,
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis)
                Text(text = toDoTask.date,
                    modifier = Modifier,
                    color = MaterialTheme.colors.taskItemContentColor,
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun TaskItemPreview(){
    TaskItem(toDoTask = ToDoTask(
        0,
        "Title",
        "Description",
        Priority.MEDIUM,
        "12/12/2002"
    ), navigateToTaskScreen = {})
}