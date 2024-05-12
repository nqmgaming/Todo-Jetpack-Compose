package com.nqmgaming.todo.feature_todo.presentation.todo_list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Divider
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nqmgaming.todo.core.util.TodoListStrings
import com.nqmgaming.todo.feature_todo.domain.util.SortingDireaction
import com.nqmgaming.todo.feature_todo.domain.util.TodoItemOrder

@Composable
fun SortingDrawerOptions(
    todoItemOrder: TodoItemOrder,
    onOrderChange: (TodoItemOrder) -> Unit
) {
    @Composable
    fun createNavigationDrawerItem(
        text: String,
        isChecked: Boolean,
        onClick: () -> Unit
    ) {
        NavigationDrawerItem(
            label = {
                IconRow(
                    text = text,
                    isChecked = isChecked
                )
            },
            selected = false,
            onClick = onClick
        )
    }

    createNavigationDrawerItem(
        text = TodoListStrings.TITLE,
        isChecked = todoItemOrder::class == TodoItemOrder.Title::class,
        onClick = { onOrderChange(TodoItemOrder.Title(todoItemOrder.sortingDireaction, todoItemOrder.showArchived)) }
    )

    createNavigationDrawerItem(
        text = TodoListStrings.TIME,
        isChecked = todoItemOrder::class == TodoItemOrder.DateCreated::class,
        onClick = { onOrderChange(TodoItemOrder.DateCreated(todoItemOrder.sortingDireaction, todoItemOrder.showArchived)) }
    )

    createNavigationDrawerItem(
        text = TodoListStrings.COMPLETED,
        isChecked = todoItemOrder::class == TodoItemOrder.Completed::class,
        onClick = { onOrderChange(TodoItemOrder.Completed(todoItemOrder.sortingDireaction, todoItemOrder.showArchived)) }
    )

    Divider()

    createNavigationDrawerItem(
        text = TodoListStrings.SORT_DOWN,
        isChecked = todoItemOrder.sortingDireaction == SortingDireaction.Down,
        onClick = { onOrderChange(todoItemOrder.copy(SortingDireaction.Down, todoItemOrder.showArchived)) }
    )

    createNavigationDrawerItem(
        text = TodoListStrings.SORT_UP,
        isChecked = todoItemOrder.sortingDireaction == SortingDireaction.Up,
        onClick = { onOrderChange(todoItemOrder.copy(SortingDireaction.Up, todoItemOrder.showArchived)) }
    )

    Divider()

    createNavigationDrawerItem(
        text = TodoListStrings.SHOW_ARCHIVED,
        isChecked = todoItemOrder.showArchived,
        onClick = { onOrderChange(todoItemOrder.copy(todoItemOrder.sortingDireaction, !todoItemOrder.showArchived)) }
    )
}


@Preview
@Composable
fun SortingDrawerOptionsPreview() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SortingDrawerOptions(
            TodoItemOrder.Title(SortingDireaction.Down, false)
        ) {}
    }
}