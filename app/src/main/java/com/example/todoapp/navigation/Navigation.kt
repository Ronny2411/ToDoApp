package com.example.todoapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.todoapp.navigation.destinations.listComposable
import com.example.todoapp.navigation.destinations.splashComposable
import com.example.todoapp.navigation.destinations.taskComposable
import com.example.todoapp.ui.viewmodels.SharedViewModel
import com.example.todoapp.util.Constants.SPLASH_SCREEN


@Composable
fun SetupNavigation(navController: NavHostController,sharedViewModel: SharedViewModel){
    val screen = remember (navController) {
        Screens(navController)
    }

    NavHost(navController = navController, startDestination = SPLASH_SCREEN){
        splashComposable(screen.splash)
        listComposable(screen.list,sharedViewModel)
        taskComposable(screen.task,sharedViewModel)
    }
}