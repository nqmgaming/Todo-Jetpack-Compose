package com.nqmgaming.todo.feature_todo.data.remote

import androidx.room.Query
import com.nqmgaming.todo.feature_todo.data.remote.dto.RemoteTodoItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Url

interface TodoApi {

    // Get all todo items
    @GET("todo.json")
    suspend fun getAllTodos(): List<RemoteTodoItem>

    // Get todo item by id
    @GET("todo.json?orderBy=\"id\"&equalTo={id}")
    suspend fun getSingleTodoById(@Path("id") id: Int?): Map<String, RemoteTodoItem>

    // Add a todo item
//    @POST("todo.json")
//    suspend fun addTodoItem(@Body url:String, @Body updateTodo: RemoteTodoItem): Response<Unit>

    // Add a todo item
    @PUT
    suspend fun addTodo(@Url url: String, @Body updateTodo: RemoteTodoItem): Response<Unit>

    // Delete a todo item
    @DELETE("todo/{id}.json")
    suspend fun deleteTodoItem(@Path("id") id: Int?): Response<Unit>

    // Update a todo item
    @PUT("todo/{id}.json")
    suspend fun updateTodoItem(
        @Path("id") id: Int?,
        @Body updateTodo: RemoteTodoItem
    ): Response<Unit>


}