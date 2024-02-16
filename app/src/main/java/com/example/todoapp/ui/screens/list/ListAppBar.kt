package com.example.todoapp.ui.screens.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import com.example.todoapp.R
import com.example.todoapp.components.DisplayAlertDialog
import com.example.todoapp.components.PriorityItem
import com.example.todoapp.data.models.Priority
import com.example.todoapp.ui.theme.SMALL_PADDING
import com.example.todoapp.ui.theme.TOP_APP_BAR_HEIGHT
import com.example.todoapp.ui.theme.topAppBarBackgroundColor
import com.example.todoapp.ui.theme.topAppBarContentColor
import com.example.todoapp.ui.viewmodels.SharedViewModel
import com.example.todoapp.util.Action
import com.example.todoapp.util.SearchAppBarState

@Composable
fun ListAppBar(sharedViewModel: SharedViewModel,
               searchAppBarState: SearchAppBarState,
               searchTextState: String){
    when(searchAppBarState){
        SearchAppBarState.CLOSED->{
            DefaultListAppBar(onSearchClicked = {
                sharedViewModel.updateSearchAppBarState(SearchAppBarState.OPENED)
            },
                              onSortClicked = { sharedViewModel.persistSortState(it)  },
                              onDeleteAllClicked = {sharedViewModel.updateAction(Action.DELETE_ALL)})
        }
        else ->
            SearchAppBar(text = searchTextState,
                onTextChanged = {sharedViewModel.updateSearchTextState(it)},
                onCloseClicked = {
                    sharedViewModel.updateSearchAppBarState(SearchAppBarState.CLOSED)
                    sharedViewModel.updateSearchTextState("")
                },
                onSearchClicked = {sharedViewModel.searchDatabase(it)})
    }
}

@Composable
fun DefaultListAppBar(onSearchClicked:() -> Unit,onSortClicked:(Priority) -> Unit,onDeleteAllClicked:() -> Unit){
    TopAppBar(title = { Text(text = stringResource(id = R.string.app_Bar_title),
        color = MaterialTheme.colors.topAppBarContentColor) },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {
            ListAppBarActions(onSearchClicked,onSortClicked,onDeleteAllClicked)
        }
    )
}

@Composable
fun ListAppBarActions(onSearchClicked:() -> Unit,onSortClicked:(Priority) -> Unit,onDeleteAllClicked:() -> Unit){
    var openDialog by remember {
        mutableStateOf(false)
    }

    DisplayAlertDialog(title = stringResource(id = R.string.delete_all_task),
        message = stringResource(id = R.string.delete_all_task_confirmation),
        openDialog = openDialog,
        closeDialog = {openDialog = false},
        onYesClicked = {
            onDeleteAllClicked()
        })
    SearchAction(onSearchClicked = onSearchClicked)
    SortAction(onSortClicked = onSortClicked)
    DeleteAllAction(onDeleteAllClicked = {openDialog = true})
}

@Composable
fun SearchAction(
    onSearchClicked:() -> Unit
){
    IconButton(onClick = {
        onSearchClicked()
    }) {
        Icon(imageVector = Icons.Filled.Search, contentDescription = stringResource(
            id = R.string.search_action
        ), tint = MaterialTheme.colors.topAppBarContentColor)
    }
}

@Composable
fun SortAction(
    onSortClicked:(Priority) -> Unit
){
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = { expanded = true }) {
        Icon(painter = painterResource(id = R.drawable.ic_filter), contentDescription = stringResource(
            id = R.string.sort_action
        ), tint = MaterialTheme.colors.topAppBarContentColor)
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false },
        ) {
        DropdownMenuItem(onClick = {
            expanded = false
            onSortClicked(Priority.LOW)
        }) {
            PriorityItem(priority = Priority.LOW)
        }
        DropdownMenuItem(onClick = {
            expanded = false
            onSortClicked(Priority.HIGH)
        }) {
            PriorityItem(priority = Priority.HIGH)
        }
        DropdownMenuItem(onClick = {
            expanded = false
            onSortClicked(Priority.NONE)
        }) {
            PriorityItem(priority = Priority.NONE)
        }
    }
}

@Composable
fun DeleteAllAction(
    onDeleteAllClicked:() -> Unit
){
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = { expanded = true }) {
        Icon(painter = painterResource(id = R.drawable.ic_more_vert), contentDescription = stringResource(
            id = R.string.delete_all_action
        ), tint = MaterialTheme.colors.topAppBarContentColor)
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false },
        ) {
        DropdownMenuItem(onClick = {
            expanded = false
            onDeleteAllClicked()
        }) {
            Text(text = stringResource(id = R.string.delete_all_action),
                modifier = Modifier.padding(start = SMALL_PADDING),
                style = MaterialTheme.typography.subtitle2)
        }
    }
}

@Composable
fun SearchAppBar(
    text : String,
    onTextChanged:(String) -> Unit,
    onCloseClicked:()-> Unit,
    onSearchClicked: (String) -> Unit
){
    Surface(modifier = Modifier
        .fillMaxWidth()
        .height(TOP_APP_BAR_HEIGHT),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.topAppBarBackgroundColor) {
        TextField(value = text,
            onValueChange = {onTextChanged(it)},
            placeholder = {Text(text = stringResource(id = R.string.search_placeholder),
                color = Color.White,
                modifier = Modifier.alpha(ContentAlpha.medium))},
            modifier =
            Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                color = MaterialTheme.colors.topAppBarContentColor,
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(modifier = Modifier.alpha(ContentAlpha.disabled),
                    onClick = { }) {
                    Icon(imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search_icon),
                        tint = MaterialTheme.colors.topAppBarContentColor)
                }
            },
            trailingIcon = {
                IconButton(onClick = {
                    if (text.isNotEmpty()){
                        onTextChanged("")
                    } else {
                        onCloseClicked()
                    }
                }) {
                    Icon(imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.close_icon),
                        tint = MaterialTheme.colors.topAppBarContentColor)
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {onSearchClicked(text)}
            ),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.topAppBarContentColor,
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
            )
    }

}
