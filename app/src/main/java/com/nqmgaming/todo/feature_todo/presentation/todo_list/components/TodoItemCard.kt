package com.nqmgaming.todo.feature_todo.presentation.todo_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.nqmgaming.todo.core.presentation.components.ArchiveButton
import com.nqmgaming.todo.core.presentation.components.CompleteButtons
import com.nqmgaming.todo.core.presentation.components.DeleteButton
import com.nqmgaming.todo.core.presentation.components.getTodoColor
import com.nqmgaming.todo.feature_todo.domain.model.TodoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoItemCard(
    todo: TodoItem,
    modifier: Modifier = Modifier,
    onCompleteClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCardClick: () -> Unit
) {
    val todoColors = getTodoColor(todo = todo)
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onCardClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = todoColors.backgroundColor
        )
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompleteButtons(
                onCompleteClick = { onCompleteClick() },
                color = todoColors.checkColor,
                completed = todo.completed
            )
            Text(
                text = todo.title, style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = todoColors.textColor,
                fontSize = 28.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Row(
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = todo.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = todoColors.textColor,
                    fontSize = 24.sp,
                    maxLines = 10,
                    overflow = TextOverflow.Ellipsis,
                )

            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxWidth()
                    .weight(0.1f)
                    .padding(end = 4.dp)
            ) {
                ArchiveButton(
                    onArchiveClick = { onArchiveClick() },
                    color = todoColors.archiveColor
                )
                DeleteButton(
                    onDeleteClick = { onDeleteClick() },

                    )
            }
        }

    }
}

@Preview
@Composable
fun TodoItemCardPreview() {
    TodoItemCard(
        todo = TodoItem(
            title = "Learn Jetpack Compose",
            description = "Learn Jetpack Compose by building a todo app, a weather app, and a cryptocurrency app",
            timestamp = 12345,
            completed = true,
            archived = false,
            id = 0

        ),
        onCompleteClick = {},
        onArchiveClick = {},
        onDeleteClick = {},
        onCardClick = {}
    )
}