package com.nqmgaming.todo.feature_todo.domain.util

sealed class SortingDireaction {
    object Up: SortingDireaction()
    object Down: SortingDireaction()
}