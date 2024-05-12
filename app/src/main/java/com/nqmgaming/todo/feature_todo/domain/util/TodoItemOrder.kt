package com.nqmgaming.todo.feature_todo.domain.util

sealed class TodoItemOrder(
    val sortingDireaction: SortingDireaction,
    val showArchived: Boolean
) {
    class Title(
        sortingDireaction: SortingDireaction,
        showArchived: Boolean
    ) : TodoItemOrder(sortingDireaction, showArchived)

    class DateCreated(
        sortingDireaction: SortingDireaction,
        showArchived: Boolean
    ) : TodoItemOrder(sortingDireaction, showArchived)

    class Completed(
        sortingDireaction: SortingDireaction,
        showArchived: Boolean
    ) : TodoItemOrder(sortingDireaction, showArchived)

    fun copy(sortingDireaction: SortingDireaction, showArchived: Boolean): TodoItemOrder {
        return when (this) {
            is Title -> Title(sortingDireaction, showArchived)
            is DateCreated -> DateCreated(sortingDireaction, showArchived)
            is Completed -> Completed(sortingDireaction, showArchived)
        }
    }
}