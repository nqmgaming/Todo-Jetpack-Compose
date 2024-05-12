package com.nqmgaming.todo.feature_todo.presentation.todo_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nqmgaming.todo.core.util.TodoListStrings
import com.nqmgaming.todo.feature_todo.data.di.IoDispatcher
import com.nqmgaming.todo.feature_todo.domain.model.TodoItem
import com.nqmgaming.todo.feature_todo.domain.use_case.TodoUseCase
import com.nqmgaming.todo.feature_todo.domain.use_case.TodoUseCaseResult
import com.nqmgaming.todo.feature_todo.domain.util.TodoItemOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val todoUseCases: TodoUseCase,
   @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _state = mutableStateOf(TodoListState())
    val state: State<TodoListState> = _state

    private var undoTodoItem: TodoItem? = null
    private var getTodoItemsJob: Job? = null
    private var errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _state.value = _state.value.copy(isLoading = false, error = throwable.message)
    }

    fun onEvent(event: TodoListEvent) {
        when (event) {
            is TodoListEvent.Delete -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    todoUseCases.deleteTodoItem(event.todoItem)
                    getTodoItems()
                    undoTodoItem = event.todoItem
                }
            }

            is TodoListEvent.Sort -> {
                val stateOrderAlreadyMatchesEventOrder =
                    _state.value.todoItemOrder::class == event.todoItemOrder::class &&
                            _state.value.todoItemOrder.showArchived == event.todoItemOrder.showArchived &&
                            _state.value.todoItemOrder.sortingDireaction == event.todoItemOrder.sortingDireaction
                if (
                    stateOrderAlreadyMatchesEventOrder
                ) {
                    return
                }
                _state.value = _state.value.copy(
                    todoItemOrder = event.todoItemOrder
                )
                getTodoItems()

            }

            is TodoListEvent.ToggleArchive -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    todoUseCases.toggleArchiveTodoItem(event.todoItem)
                    getTodoItems()
                }

            }

            is TodoListEvent.ToggleComplete -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    todoUseCases.toggleCompleteTodoItem(event.todoItem)
                    getTodoItems()
                }

            }

            TodoListEvent.UndoDelete -> {
                undoTodoItem?.let {
                    viewModelScope.launch(dispatcher + errorHandler) {
                        todoUseCases.addTodoItem(it)
                        getTodoItems()
                        undoTodoItem = null
                    }
                }
            }
            else -> {
                // Do nothing
            }
        }


    }

    fun getTodoItems() {
        getTodoItemsJob?.cancel()
        getTodoItemsJob = viewModelScope.launch(dispatcher + errorHandler) {
            var result = todoUseCases.getTodoItems(
                todoItemOrder = _state.value.todoItemOrder
            )
            when (result) {
                is TodoUseCaseResult.Success -> {
                    _state.value = _state.value.copy(
                        todoItems = result.todoItems,
                        todoItemOrder = _state.value.todoItemOrder,
                        isLoading = false
                    )
                }

                is TodoUseCaseResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = TodoListStrings.CANT_GET_TODO_ITEMS
                    )

                }

                is TodoUseCaseResult.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }

                else -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = TodoListStrings.CANT_GET_TODO_ITEMS
                    )
                }
            }
        }
    }
}