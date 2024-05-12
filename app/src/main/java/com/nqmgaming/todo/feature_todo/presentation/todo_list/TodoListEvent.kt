package com.nqmgaming.todo.feature_todo.presentation.todo_list

import com.nqmgaming.todo.feature_todo.domain.model.TodoItem
import com.nqmgaming.todo.feature_todo.domain.util.TodoItemOrder

sealed class TodoListEvent {
    data class Sort(val todoItemOrder: TodoItemOrder): TodoListEvent()
    data class Delete(val todoItem: TodoItem): TodoListEvent()
    data class ToggleComplete(val todoItem: TodoItem): TodoListEvent()
    data class ToggleArchive(val todoItem: TodoItem): TodoListEvent()
    data object UndoDelete: TodoListEvent()
}