package com.nqmgaming.todo.feature_todo.presentation.todo_new_update.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun HintTextField(
    modifier: Modifier = Modifier,
    text: String,
    hint: String,
    fontSize: TextUnit = 32.sp,
    textColor: Color,
    isHintVisible: Boolean = true,
    singleLine: Boolean = false,
    onValueChange: (String) -> Unit
) {}