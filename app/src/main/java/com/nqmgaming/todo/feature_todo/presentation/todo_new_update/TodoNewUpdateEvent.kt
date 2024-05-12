package com.nqmgaming.todo.feature_todo.presentation.todo_new_update

import androidx.compose.ui.focus.FocusState

sealed class TodoNewUpdateEvent {
    data class EnteredTitle(val title: String) : TodoNewUpdateEvent()
    data class ChangedTitleFocus(val focusState: FocusState) : TodoNewUpdateEvent()
    data class EnteredDescription(val description: String) : TodoNewUpdateEvent()
    data class ChangedDescriptionFocus(val focusState: FocusState) : TodoNewUpdateEvent()
    data object DeleteTodo: TodoNewUpdateEvent()
    data object ToggleCompleted: TodoNewUpdateEvent()
    data object ToggleArchived: TodoNewUpdateEvent()
    data object SaveTodo: TodoNewUpdateEvent()
    data object Back: TodoNewUpdateEvent()
}