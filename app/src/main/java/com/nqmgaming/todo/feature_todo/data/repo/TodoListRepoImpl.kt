package com.nqmgaming.todo.feature_todo.data.repo

import android.util.Log
import com.nqmgaming.todo.feature_todo.data.di.IoDispatcher
import com.nqmgaming.todo.feature_todo.data.local.TodoDao
import com.nqmgaming.todo.feature_todo.data.mapper.toLocalTodoItem
import com.nqmgaming.todo.feature_todo.data.mapper.toLocalTodoItemsFromRemote
import com.nqmgaming.todo.feature_todo.data.mapper.toRemoteTodoItem
import com.nqmgaming.todo.feature_todo.data.mapper.toTodoItem
import com.nqmgaming.todo.feature_todo.data.mapper.toTodoItemsFromLocalItem
import com.nqmgaming.todo.feature_todo.data.remote.TodoApi
import com.nqmgaming.todo.feature_todo.domain.model.TodoItem
import com.nqmgaming.todo.feature_todo.domain.repo.TodoListRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

class TodoListRepoImpl(
    private val dao: TodoDao,
    private val api: TodoApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : TodoListRepo {
    override suspend fun getAllTodos(): List<TodoItem> {
        getAllTodosFromRemote()
        return dao.getAllTodoItems().toTodoItemsFromLocalItem()
    }

    override suspend fun getAllTodosFromLocalCache(): List<TodoItem> {
        return dao.getAllTodoItems().toTodoItemsFromLocalItem()
    }

    override suspend fun getAllTodosFromRemote() {
        return withContext(dispatcher) {
            try {
                refreshRoomCache()
            } catch (e: Exception) {
                when (e) {
                    is UnknownHostException, is ConnectException, is HttpException -> {
                        Log.e("TodoListRepoImpl", "Error fetching data from remote", e)
                        if (isCacheEmpty()) {
                            Log.e("TodoListRepoImpl", "Cache is empty")
                            throw Exception("Error: Device is offline and cache is empty")
                        } else {
                            throw Exception("Error: Device is offline: $e")
                        }
                    }

                    else -> {
                        Log.e("TodoListRepoImpl", "Error fetching data from remote", e)
                        throw Exception("Error: $e")
                    }

                }
            }
        }
    }

    override suspend fun getSingleTodoById(id: Int): TodoItem? {
        return dao.getTodoItemById(id)?.toTodoItem()
    }

    override suspend fun addTodoItem(todoItem: TodoItem) {
        val newId = dao.addTodoItem(todoItem.toLocalTodoItem())
        val id = newId.toInt()
        val url = "todo/$id.json"
        api.addTodo(url, todoItem.toRemoteTodoItem().copy(id = id))
    }

    override suspend fun updateTodoItem(todoItem: TodoItem) {
        dao.addTodoItem(todoItem.toLocalTodoItem())
        api.updateTodoItem(todoItem.id, todoItem.toRemoteTodoItem())
    }

    override suspend fun deleteTodoItem(todoItem: TodoItem) {
        try {
            val response = api.deleteTodoItem(todoItem.id)
            if (response.isSuccessful) {
                Log.d("API_DELETE", "Item deleted")
                dao.deleteTodoItem(todoItem.toLocalTodoItem())
            } else {
                throw Exception("Error: Could not delete item ${response.errorBody()}")
            }
        } catch (e: Exception) {
            when (e) {
                is UnknownHostException, is ConnectException, is HttpException -> {
                    Log.e("HTTP", "Error: Could not delete item", e)
                    throw Exception("Error: Device is offline: $e")
                }

                else -> {
                    throw Exception("Error: $e")
                }

            }
        }
    }

    private fun isCacheEmpty(): Boolean {
        var isEmpty = false
        if (dao.getAllTodoItems().isEmpty()) {
            isEmpty = true
        }
        return isEmpty
    }

    private suspend fun refreshRoomCache() {
        val remoteBooks = api.getAllTodos()
        dao.addAllTodoItems(remoteBooks.toLocalTodoItemsFromRemote())
    }
}