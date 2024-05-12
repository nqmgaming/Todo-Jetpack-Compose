package com.nqmgaming.todo.feature_todo.presentation.todo_new_update

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nqmgaming.todo.core.util.NewUpdateStrings
import com.nqmgaming.todo.feature_todo.data.di.IoDispatcher
import com.nqmgaming.todo.feature_todo.domain.use_case.TodoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoNewUpdateViewModel @Inject constructor(
    private val todoUseCase: TodoUseCase,
    saveSateHandle: SavedStateHandle,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _state = mutableStateOf(TodoNewUpdateState())
    val state: State<TodoNewUpdateState> = _state

    private var errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _state.value = _state.value.copy(
            isLoading = false,
            error = throwable.message
        )
    }

    private var currentTodoId: Int? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object SaveTodo : UiEvent()
        data object Back : UiEvent()
    }

    init {
        saveSateHandle.get<Int>("todoId")?.let { todoId ->
            if (todoId != -1) {
                viewModelScope.launch {
                    todoUseCase.getTodoItemById(todoId)?.also { todoItem ->
                        currentTodoId = todoItem.id
                        _state.value = _state.value.copy(
                            todo = todoItem,
                            isTitleHintVisible = todoItem.title.isBlank(),
                            isDescriptionHintVisible = todoItem.description.isBlank(),
                            isLoading = false
                        )
                    }
                }
            } else {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    fun onEvent(event: TodoNewUpdateEvent) {
        when (event) {
            TodoNewUpdateEvent.Back -> {
                viewModelScope.launch(ioDispatcher + errorHandler) {
                    _eventFlow.emit(UiEvent.Back)
                }
            }

            is TodoNewUpdateEvent.ChangedDescriptionFocus -> {
                val shouldDescriptionHintBeVisible =
                    !event.focusState.isFocused && _state.value.todo.description.isBlank()
                _state.value = _state.value.copy(
                    isDescriptionHintVisible = shouldDescriptionHintBeVisible
                )
            }

            is TodoNewUpdateEvent.ChangedTitleFocus -> {
                val shouldTitleHintBeVisible =
                    !event.focusState.isFocused && _state.value.todo.title.isBlank()
                _state.value = _state.value.copy(
                    isTitleHintVisible = shouldTitleHintBeVisible
                )

            }

            TodoNewUpdateEvent.DeleteTodo -> {
                viewModelScope.launch(ioDispatcher + errorHandler) {
                    try {
                        if (currentTodoId != null) {
                            todoUseCase.deleteTodoItem(_state.value.todo)
                            _eventFlow.emit(UiEvent.ShowSnackbar(NewUpdateStrings.TODO_ITEM_DELETED))
                        }
                        _eventFlow.emit(UiEvent.Back)
                    } catch (e: Exception) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                e.message ?: NewUpdateStrings.ERROR_DELETING_TODO_ITEM
                            )
                        )
                    }
                }

            }

            is TodoNewUpdateEvent.EnteredDescription -> {
                _state.value = _state.value.copy(
                    todo = _state.value.todo.copy(description = event.description)
                )

            }

            is TodoNewUpdateEvent.EnteredTitle -> {
                _state.value = _state.value.copy(
                    todo = _state.value.todo.copy(title = event.title)
                )

            }

            TodoNewUpdateEvent.SaveTodo -> {
                viewModelScope.launch(ioDispatcher + errorHandler) {
                    try {
                        if (currentTodoId != null) {
                            todoUseCase.updateTodoItem(_state.value.todo)
                            _eventFlow.emit(UiEvent.ShowSnackbar(NewUpdateStrings.TODO_ITEM_UPDATED))
                        } else {
                            todoUseCase.addTodoItem(
                                _state.value.todo.copy(
                                    id = null,
                                    timestamp = System.currentTimeMillis()
                                )
                            )
                            _eventFlow.emit(UiEvent.ShowSnackbar(NewUpdateStrings.TODO_ITEM_ADDED))
                        }
                        _eventFlow.emit(UiEvent.SaveTodo)
                    } catch (e: Exception) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                e.message ?: NewUpdateStrings.ERROR_SAVING_TODO_ITEM
                            )
                        )
                    }
                }

            }

            TodoNewUpdateEvent.ToggleArchived -> {
                _state.value = _state.value.copy(
                    todo = _state.value.todo.copy(archived = !_state.value.todo.archived)
                )
            }

            TodoNewUpdateEvent.ToggleCompleted -> {
                _state.value = _state.value.copy(
                    todo = _state.value.todo.copy(completed = !_state.value.todo.completed)
                )
            }
        }

    }
}