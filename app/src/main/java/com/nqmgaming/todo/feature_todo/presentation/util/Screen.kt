package com.nqmgaming.todo.feature_todo.presentation.util

sealed class Screen(val route: String) {
    data object TodoItemListScreen: Screen("todo_list_screen")
    object TodoNewUpdateScreen: Screen("todo_new_update_screen")
}