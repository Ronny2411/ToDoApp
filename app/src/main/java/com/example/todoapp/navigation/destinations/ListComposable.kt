package com.example.todoapp.navigation.destinations

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todoapp.ui.screens.list.ListScreen
import com.example.todoapp.ui.viewmodels.SharedViewModel
import com.example.todoapp.util.Action
import com.example.todoapp.util.Constants.LIST_ARGUMENT_KEY
import com.example.todoapp.util.Constants.LIST_SCREEN
import com.example.todoapp.util.toAction


fun NavGraphBuilder.listComposable(
    navigateToTaskScreen : (taskId : Int) -> Unit,
    sharedViewModel: SharedViewModel
){
    composable(
        enterTransition = {
            slideInHorizontally(initialOffsetX = {-it}, animationSpec = tween(300))
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = {-it}, animationSpec = tween(300))
        },
        route = LIST_SCREEN,
        arguments = listOf(navArgument(LIST_ARGUMENT_KEY){
            type = NavType.StringType
        })
    ){navBackStackEntry->
        val action = navBackStackEntry.arguments?.getString(LIST_ARGUMENT_KEY).toAction()

        var myAction by rememberSaveable {
            mutableStateOf(Action.NO_ACTION)
        }

        LaunchedEffect(key1 = myAction){
            if (action != myAction){
                myAction = action
                sharedViewModel.updateAction(action)
            }
        }

        ListScreen(navigateToTaskScreen,sharedViewModel)
    }
}