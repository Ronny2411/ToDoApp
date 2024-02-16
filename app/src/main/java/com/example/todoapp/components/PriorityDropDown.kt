package com.example.todoapp.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.data.models.Priority
import com.example.todoapp.ui.theme.DarkGray
import com.example.todoapp.ui.theme.PRIORITY_DROP_DOWN_HEIGHT
import com.example.todoapp.ui.theme.PRIORITY_INDICATOR_SIZE
import com.example.todoapp.ui.theme.taskItemContentColor

@Composable
fun PriorityDropDown(
    priority: Priority,
    onPrioritySelected:(Priority)->Unit
) {
    var darkTheme : Color = Color.White
    if (isSystemInDarkTheme())
        darkTheme = DarkGray

    var expanded by remember {
        mutableStateOf(false)
    }
    val angle : Float by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f, label = ""
    )

    var parentSize by remember {
        mutableStateOf(IntSize.Zero)
    }

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { 
                parentSize = it.size
            }
            .height(PRIORITY_DROP_DOWN_HEIGHT)
            .clickable { expanded = true }
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.taskItemContentColor,
                shape = MaterialTheme.shapes.small
            )
            .background(darkTheme),
        verticalAlignment = Alignment.CenterVertically
    ){
        Canvas(modifier = Modifier
            .size(PRIORITY_INDICATOR_SIZE)
            .weight(1f),
            onDraw = {
            drawCircle(color = priority.color)
        })
        Text(text = priority.name,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.weight(8f),
            color = MaterialTheme.colors.taskItemContentColor)
        IconButton(onClick = { expanded = true },
            modifier = Modifier
                .weight(1.5f)
                .rotate(angle)
                .alpha(ContentAlpha.medium)) {
            Icon(imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = stringResource(id = R.string.arrow_drop_down),
                tint = MaterialTheme.colors.taskItemContentColor)
        }
        DropdownMenu(expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current){parentSize.width.toDp()})
        ) {
            DropdownMenuItem(onClick = { onPrioritySelected(Priority.LOW)
                                            expanded = false}) {
                PriorityItem(priority = Priority.LOW)
            }
            DropdownMenuItem(onClick = { onPrioritySelected(Priority.MEDIUM)
                expanded = false}) {
                PriorityItem(priority = Priority.MEDIUM)
            }
            DropdownMenuItem(onClick = { onPrioritySelected(Priority.HIGH)
                expanded = false}) {
                PriorityItem(priority = Priority.HIGH)
            }
        }
    }
}

@Preview
@Composable
fun PriorityDropDownPreview(){
    PriorityDropDown(priority = Priority.HIGH, onPrioritySelected = {})
}