package com.example.todoapp.navigation.destinations

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.todoapp.ui.screens.splash.SplashScreen
import com.example.todoapp.util.Constants.SPLASH_SCREEN

fun NavGraphBuilder.splashComposable(
    navigateToListScreen:()->Unit
){
    composable(route=SPLASH_SCREEN,
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = {it},
                animationSpec = tween(300)
            )
        }
    ){
        SplashScreen(navigateToListScreen)
    }
}

