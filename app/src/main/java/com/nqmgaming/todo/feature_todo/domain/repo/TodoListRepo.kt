package com.nqmgaming.todo.feature_todo.domain.repo

import com.nqmgaming.todo.feature_todo.domain.model.TodoItem

interface TodoListRepo {

    // Get all todo items
    suspend fun getAllTodos(): List<TodoItem>

    // Get all todo items from local cache
    suspend fun getAllTodosFromLocalCache(): List<TodoItem>

    // Get all todo items from remote
    suspend fun getAllTodosFromRemote()

    // Get todo item by id
    suspend fun getSingleTodoById(id: Int): TodoItem?

    // Add a todo item
    suspend fun addTodoItem(todoItem: TodoItem)

    // Update a todo item
    suspend fun updateTodoItem(todoItem: TodoItem)

    // Delete a todo item
    suspend fun deleteTodoItem(todoItem: TodoItem)
}