package com.example.todoapp.ui.screens.splash

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.ui.theme.Purple40
import com.example.todoapp.util.Constants.SPLASH_SCREEN_DELAY
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigateToListScreen:()->Unit
){
    var startAnimation by remember {
        mutableStateOf(false)
    }
    val offsetState by animateDpAsState(
        targetValue = if (startAnimation) 0.dp else 100.dp,
        animationSpec = tween(1000)
    )
    val alphaState by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(1000)
    )
    LaunchedEffect(key1 = true){
        startAnimation = true
        delay(SPLASH_SCREEN_DELAY)
        navigateToListScreen()
    }

    Splash(offsetState = offsetState, alphaState = alphaState)

}


@Composable
fun Splash(offsetState : Dp,alphaState : Float){
    val darktheme = if (isSystemInDarkTheme()) Color.Black else Purple40
    Box(modifier = Modifier
        .fillMaxSize()
        .background(darktheme)
        , contentAlignment = Alignment.Center){
        Column(modifier = Modifier
            .offset(y = offsetState)
            .alpha(alphaState)) {
            Text(text = "To Do App", color = Color.White, fontWeight = FontWeight.ExtraBold,
                fontSize = MaterialTheme.typography.h4.fontSize)
            Icon(painter = painterResource(id = R.drawable.to_do_list),
                contentDescription = "App Logo",
                tint = Color.White, modifier = Modifier.size(125.dp))
        }
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SplashScreenPreview(){
    Splash(
        offsetState = 0.dp,
        alphaState = 1f
    )
}