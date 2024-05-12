package com.nqmgaming.todo.feature_todo.data.remote.dto

import com.google.gson.annotations.SerializedName


data class RemoteTodoItem(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("timestamp")
    val timestamp: Long,
    @SerializedName("completed")
    val completed: Boolean,
    @SerializedName("archived")
    val archived: Boolean,
    @SerializedName("id")
    val id: Int?
)