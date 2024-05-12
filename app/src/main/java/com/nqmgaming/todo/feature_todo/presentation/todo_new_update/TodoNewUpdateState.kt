package com.nqmgaming.todo.feature_todo.presentation.todo_new_update

import com.nqmgaming.todo.feature_todo.domain.model.TodoItem

data class TodoNewUpdateState(
    val isTitleHintVisible: Boolean = true,
    val isDescriptionHintVisible: Boolean = true,
    val todo: TodoItem = TodoItem(
        title = "",
        description = "",
        timestamp = 0,
        completed = false,
        archived = false,
        id = null
    ),
    val isLoading: Boolean = true,
    val error: String? = null,
)