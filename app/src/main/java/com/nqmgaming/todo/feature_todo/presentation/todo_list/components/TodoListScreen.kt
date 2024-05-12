package com.nqmgaming.todo.feature_todo.presentation.todo_list.components

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nqmgaming.todo.R
import com.nqmgaming.todo.core.util.ContentDescription
import com.nqmgaming.todo.core.util.TodoListStrings
import com.nqmgaming.todo.feature_todo.presentation.todo_list.TodoListEvent
import com.nqmgaming.todo.feature_todo.presentation.todo_list.TodoListViewModel
import com.nqmgaming.todo.feature_todo.presentation.util.Screen
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    navController: NavController,
    viewModel: TodoListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    val backgroundImage = if (isPortrait) {
        R.drawable.background_portrait
    } else {
        R.drawable.background_landscape
    }

    LaunchedEffect(key1 = true) {
        viewModel.getTodoItems()

    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(modifier = Modifier.fillMaxWidth(0.65f)){
                ModalDrawerSheet {
                    Text(
                        text = TodoListStrings.SORT_BY,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 34.sp,
                        lineHeight = 38.sp
                    )
                    Divider()
                    SortingDrawerOptions(
                        todoItemOrder = state.todoItemOrder,
                        onOrderChange = { order ->
                            viewModel.onEvent(TodoListEvent.Sort(order))
                        }
                    )
                }
            }

        }
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screen.TodoNewUpdateScreen.route)
                    },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = ContentDescription.ADD_TODO_ITEM,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = TodoListStrings.TODO_LIST,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.9f
                        ),
                        scrolledContainerColor = MaterialTheme.colorScheme.primary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    navigationIcon = {},
                    actions = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = ContentDescription.SORTING_MENU
                            )
                        }
                    },
                    scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
        ) { paddingValues ->
            Box(
                contentAlignment = Alignment.TopStart,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Image(
                    painter = painterResource(id = backgroundImage),
                    contentDescription = ContentDescription.BACKGROUND_IMAGE,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxSize(),
                    alignment = Alignment.TopStart
                )

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp)
                            .padding(top = 110.dp),
                        contentPadding = PaddingValues(bottom = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(state.todoItems) { todo ->
                            TodoItemCard(
                                todo = todo,
                                onCompleteClick = {
                                    viewModel.onEvent(TodoListEvent.ToggleComplete(todo))
                                },
                                onArchiveClick = {
                                    viewModel.onEvent(TodoListEvent.ToggleArchive(todo))
                                },
                                onDeleteClick = {
                                    viewModel.onEvent(TodoListEvent.Delete(todo))
                                    scope.launch {
                                        val undo = snackbarHostState.showSnackbar(
                                            message = TodoListStrings.TODO_ITEM_DELETED,
                                            actionLabel = TodoListStrings.UNDO
                                        )
                                        if (undo == SnackbarResult.ActionPerformed) {
                                            viewModel.onEvent(TodoListEvent.UndoDelete)
                                        }
                                    }
                                },
                                onCardClick = {
                                    navController.navigate(
                                        Screen.TodoNewUpdateScreen.route + "?todoId=${todo.id}"
                                    )
                                }
                            )

                        }
                    }

                }

                if (state.isLoading) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .semantics {
                                    this.contentDescription = ContentDescription.LOADING_INDICATOR
                                }
                        )
                    }
                }
                if (state.error != null) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.error,
                            fontSize = 30.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }

}