package com.nqmgaming.todo.feature_todo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nqmgaming.todo.feature_todo.data.local.dto.LocalTodoItem

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo")
    fun getAllTodoItems(): List<LocalTodoItem>

    @Query("SELECT * FROM todo WHERE id = :id")
    suspend fun getTodoItemById(id: Int): LocalTodoItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllTodoItems(todoItems: List<LocalTodoItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodoItem(todoItem: LocalTodoItem): Long

    @Delete
    suspend fun deleteTodoItem(todoItem: LocalTodoItem): Int

}