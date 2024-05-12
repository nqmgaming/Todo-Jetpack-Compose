package com.nqmgaming.todo.core.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.nqmgaming.todo.core.util.ContentDescription

@Composable
fun CompleteButtons(
    onCompleteClick: () -> Unit,
    color: Color,
    completed: Boolean,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = { onCompleteClick() },
        modifier = modifier
    ) {
        if (completed) {
            Icon(
                imageVector = Icons.Default.CheckCircleOutline,
                contentDescription = ContentDescription.COMPLETE_TODO_ITEM,
                tint = color,
                modifier = Modifier.size(48.dp)
            )
        } else {
            EmptyCircle(color = color)
        }
    }
}

@Composable
fun EmptyCircle(color: Color, strokeWidth: Float = 9f) {
    Canvas(modifier = Modifier.fillMaxSize(),
        onDraw = {
            val radius = 39f
            drawCircle(
                color = color,
                radius = radius,
                center = center,
                alpha = 0.5f,
                style = Stroke(strokeWidth)
            )
        })
}

@Composable
fun ArchiveButton(
    modifier: Modifier = Modifier,
    onArchiveClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.secondary,
) {
    IconButton(
        onClick = { onArchiveClick() },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Archive,
            contentDescription = ContentDescription.ARCHIVE_TODO_ITEM,
            tint = color,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
fun DeleteButton(
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit,
) {
    IconButton(
        onClick = { onDeleteClick() },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = ContentDescription.DELETE_TODO_ITEM,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(32.dp)
        )
    }
}