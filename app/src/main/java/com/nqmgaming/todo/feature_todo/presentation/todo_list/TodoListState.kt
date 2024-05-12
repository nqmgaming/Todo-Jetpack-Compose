package com.nqmgaming.todo.feature_todo.presentation.todo_list

import com.nqmgaming.todo.feature_todo.domain.model.TodoItem
import com.nqmgaming.todo.feature_todo.domain.util.SortingDireaction
import com.nqmgaming.todo.feature_todo.domain.util.TodoItemOrder

data class TodoListState(
    val todoItems: List<TodoItem> = emptyList(),
    val todoItemOrder: TodoItemOrder = TodoItemOrder.DateCreated(SortingDireaction.Down, true),
    val isLoading: Boolean = true,
    val error: String? = null
)
