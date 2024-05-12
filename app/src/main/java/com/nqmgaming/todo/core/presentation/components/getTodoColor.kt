package com.nqmgaming.todo.core.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.nqmgaming.todo.feature_todo.domain.model.TodoItem

data class TodoItemColors(
    val backgroundColor: Color,
    val textColor: Color,
    val archiveColor: Color,
    val checkColor: Color
)

@Composable
fun getTodoColor(todo: TodoItem): TodoItemColors {
    return if (todo.archived) {
        TodoItemColors(
            backgroundColor = MaterialTheme.colorScheme.secondary.copy(
                alpha = 0.6f
            ),
            textColor = MaterialTheme.colorScheme.onSecondary,
            archiveColor = MaterialTheme.colorScheme.onSecondary,
            checkColor = if (todo.completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
        )
    } else {
        TodoItemColors(
            backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(
                alpha = 0.6f
            ),
            textColor = MaterialTheme.colorScheme.onPrimaryContainer,
            archiveColor = MaterialTheme.colorScheme.secondary,
            checkColor = if (todo.completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
        )
    }
}