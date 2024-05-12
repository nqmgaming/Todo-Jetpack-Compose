package com.nqmgaming.todo.feature_todo.data.mapper

import com.nqmgaming.todo.feature_todo.data.local.dto.LocalTodoItem
import com.nqmgaming.todo.feature_todo.data.remote.dto.RemoteTodoItem
import com.nqmgaming.todo.feature_todo.domain.model.TodoItem

fun TodoItem.toLocalTodoItem(): LocalTodoItem = convertTodoItemToLocal(this)
fun TodoItem.toRemoteTodoItem(): RemoteTodoItem = convertTodoItemToRemote(this)
fun LocalTodoItem.toTodoItem(): TodoItem = convertLocalToTodoItem(this)
fun LocalTodoItem.toRemoteTodoItem(): RemoteTodoItem = convertLocalToRemote(this)
fun RemoteTodoItem.toTodoItem(): TodoItem = convertRemoteToTodoItem(this)
fun RemoteTodoItem.toLocalTodoItem(): LocalTodoItem = convertRemoteToLocal(this)
fun List<TodoItem>.toLocalTodoItemsFromTodoItem(): List<LocalTodoItem> = map { it.toLocalTodoItem() }
fun List<TodoItem>.toRemoteTodoItemsFromTodoItem(): List<RemoteTodoItem> = map { it.toRemoteTodoItem() }
fun List<LocalTodoItem>.toTodoItemsFromLocalItem(): List<TodoItem> = map { it.toTodoItem() }
fun List<LocalTodoItem>.toRemoteTodoItemsFromLocal(): List<RemoteTodoItem> = map { it.toRemoteTodoItem() }
fun List<RemoteTodoItem>.toTodoItemsFromRemote(): List<TodoItem> = map { it.toTodoItem() }
fun List<RemoteTodoItem>.toLocalTodoItemsFromRemote(): List<LocalTodoItem> = map { it.toLocalTodoItem() }

private fun convertTodoItemToLocal(todoItem: TodoItem): LocalTodoItem {
    return LocalTodoItem(
        title = todoItem.title,
        description = todoItem.description,
        timestamp = todoItem.timestamp,
        completed = todoItem.completed,
        archived = todoItem.archived,
        id = todoItem.id
    )
}

private fun convertTodoItemToRemote(todoItem: TodoItem): RemoteTodoItem {
    return RemoteTodoItem(
        title = todoItem.title,
        description = todoItem.description,
        timestamp = todoItem.timestamp,
        completed = todoItem.completed,
        archived = todoItem.archived,
        id = todoItem.id
    )
}

private fun convertLocalToTodoItem(localTodoItem: LocalTodoItem): TodoItem {
    return TodoItem(
        title = localTodoItem.title,
        description = localTodoItem.description,
        timestamp = localTodoItem.timestamp,
        completed = localTodoItem.completed,
        archived = localTodoItem.archived,
        id = localTodoItem.id
    )
}

private fun convertLocalToRemote(localTodoItem: LocalTodoItem): RemoteTodoItem {
    return RemoteTodoItem(
        title = localTodoItem.title,
        description = localTodoItem.description,
        timestamp = localTodoItem.timestamp,
        completed = localTodoItem.completed,
        archived = localTodoItem.archived,
        id = localTodoItem.id
    )
}

private fun convertRemoteToTodoItem(remoteTodoItem: RemoteTodoItem): TodoItem {
    return TodoItem(
        title = remoteTodoItem.title,
        description = remoteTodoItem.description,
        timestamp = remoteTodoItem.timestamp,
        completed = remoteTodoItem.completed,
        archived = remoteTodoItem.archived,
        id = remoteTodoItem.id
    )
}

private fun convertRemoteToLocal(remoteTodoItem: RemoteTodoItem): LocalTodoItem {
    return LocalTodoItem(
        title = remoteTodoItem.title,
        description = remoteTodoItem.description,
        timestamp = remoteTodoItem.timestamp,
        completed = remoteTodoItem.completed,
        archived = remoteTodoItem.archived,
        id = remoteTodoItem.id
    )
}