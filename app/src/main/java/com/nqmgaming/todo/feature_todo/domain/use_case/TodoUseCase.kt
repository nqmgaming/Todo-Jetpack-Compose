package com.nqmgaming.todo.feature_todo.domain.use_case

import com.nqmgaming.todo.core.util.TodoConstants
import com.nqmgaming.todo.feature_todo.domain.model.TodoItem
import com.nqmgaming.todo.feature_todo.domain.repo.TodoListRepo
import com.nqmgaming.todo.feature_todo.domain.util.InvalidTodoItemException
import com.nqmgaming.todo.feature_todo.domain.util.SortingDireaction
import com.nqmgaming.todo.feature_todo.domain.util.TodoItemOrder
import javax.inject.Inject

class TodoUseCase @Inject constructor(
    private val repository: TodoListRepo
) {
    suspend fun addTodoItem(todoItem: TodoItem) {
        validateTodoItem(todoItem)
        repository.addTodoItem(todoItem)
    }

    suspend fun updateTodoItem(todoItem: TodoItem) {
        validateTodoItem(todoItem)
        repository.updateTodoItem(todoItem)
    }

    suspend fun deleteTodoItem(todoItem: TodoItem) {
        repository.deleteTodoItem(todoItem)
    }

    suspend fun toggleCompleteTodoItem(todoItem: TodoItem) {
        repository.updateTodoItem(todoItem.copy(completed = !todoItem.completed))
    }

    suspend fun toggleArchiveTodoItem(todoItem: TodoItem) {
        repository.updateTodoItem(todoItem.copy(archived = !todoItem.archived))
    }

    suspend fun getTodoItemById(id: Int): TodoItem? {
        return repository.getSingleTodoById(id)
    }

    suspend fun getTodoItems(
        todoItemOrder: TodoItemOrder = TodoItemOrder.DateCreated(SortingDireaction.Down, true),
    ): TodoUseCaseResult {
        var todos = repository.getAllTodosFromLocalCache()

        if (todos.isEmpty()) {
            todos = repository.getAllTodos()
        }

        val filterTodo = if (todoItemOrder.showArchived) {
            todos
        } else {
            todos.filter { !it.archived }
        }

        return when (todoItemOrder.sortingDireaction) {
            is SortingDireaction.Up -> {
                when (todoItemOrder) {
                    is TodoItemOrder.Title -> {
                        TodoUseCaseResult.Success(filterTodo.sortedBy { it.title.lowercase() })
                    }

                    is TodoItemOrder.DateCreated -> {
                        TodoUseCaseResult.Success(filterTodo.sortedBy { it.timestamp })
                    }

                    is TodoItemOrder.Completed -> {
                        TodoUseCaseResult.Success(filterTodo.sortedBy { it.completed })
                    }

                    else -> {
                        TodoUseCaseResult.Error("Invalid sorting order")
                    }
                }
            }

            is SortingDireaction.Down -> {
                when (todoItemOrder) {
                    is TodoItemOrder.Title -> {
                        TodoUseCaseResult.Success(filterTodo.sortedByDescending { it.title.lowercase() })
                    }

                    is TodoItemOrder.DateCreated -> {
                        TodoUseCaseResult.Success(filterTodo.sortedByDescending { it.timestamp })
                    }

                    is TodoItemOrder.Completed -> {
                        TodoUseCaseResult.Success(filterTodo.sortedByDescending { it.completed })
                    }

                    else -> {
                        TodoUseCaseResult.Error("Invalid sorting order")
                    }
                }

            }

            else -> {
                TodoUseCaseResult.Error("Invalid sorting direction")
            }
        }
    }

    private fun validateTodoItem(todoItem: TodoItem) {
        if (todoItem.title.isBlank() || todoItem.description.isBlank()) {
            throw InvalidTodoItemException(TodoConstants.EMPTY_TITLE_OR_DESCRIPTION)
        }
    }
}

sealed class TodoUseCaseResult {
    data class Loading(val message: String) : TodoUseCaseResult()
    data class Success(val todoItems: List<TodoItem>) : TodoUseCaseResult()
    data class Error(val message: String) : TodoUseCaseResult()
}