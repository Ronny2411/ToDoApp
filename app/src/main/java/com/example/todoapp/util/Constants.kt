package com.example.todoapp.util

object Constants {
    const val DATABASE_TABLE_NAME = "todo_table"
    const val DATABASE_NAME = "todo_database"

    const val SPLASH_SCREEN = "splash"
    const val LIST_SCREEN = "list/{action}"
    const val TASK_SCREEN = "task/{taskId}"

    const val LIST_ARGUMENT_KEY = "action"
    const val TASK_ARGUMENT_KEY = "taskId"

    const val PREFERENCE_NAME = "todo_preferences"
    const val PREFERENCE_KEY = "sort_state"

    const val MAX_TITLE_LENGTH = 20
    const val SPLASH_SCREEN_DELAY = 2000L

    const val NOTIFICATION_ID = 1
    const val CHANNEL_ID = "channelId"
    const val TITLE_EXTRA = "titleExtra"
    const val MESSAGE_EXTRA = "messageExtra"
}