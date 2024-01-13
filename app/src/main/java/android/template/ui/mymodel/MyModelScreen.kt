/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.template.ui.mymodel

import android.template.R
import android.template.data.local.database.Movie
import android.template.ui.theme.MyApplicationTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyModelScreen(
    modifier: Modifier = Modifier,
    viewModel: MyModelViewModel = hiltViewModel(),
    onAddNewMovie: () -> Unit,
    onMovieClicked: (name: Movie) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            MoviesTopAppBar(
                onSortNameAscending = { viewModel.sortMoviesBy(MoviesSortType.SORT_NAME_ASCENDING) },
                onSortNameDescending = { viewModel.sortMoviesBy(MoviesSortType.SORT_NAME_DESCENDING) },
                onSortRatingAscending = { viewModel.sortMoviesBy(MoviesSortType.SORT_RATING_ASCENDING) },
                onSortRatingDescending = { viewModel.sortMoviesBy(MoviesSortType.SORT_RATING_DESCENDING) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddNewMovie() }) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        val items by viewModel.uiState.collectAsStateWithLifecycle()
        when (items) {
            is MyModelUiState.Error -> {
                LaunchedEffect(snackbarHostState) {
                    snackbarHostState.showSnackbar(
                        (items as MyModelUiState.Error).throwable.message ?: "Some error occurred"
                    )
                }
            }

            MyModelUiState.Loading -> {
                CircularProgressIndicator(
                    Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }

            is MyModelUiState.Success -> {
                MyModelScreen(
                    movies = (items as MyModelUiState.Success).data,
                    onMovieClicked = { onMovieClicked(it) },
                    modifier = modifier.padding(paddingValues)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MyModelScreen(
    movies: List<Movie>, onMovieClicked: (name: Movie) -> Unit, modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(movies, key = { it.id }) { movie ->
            Card(
                onClick = { onMovieClicked(movie) },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = movie.name,
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = movie.desc, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "${movie.rating}/10", modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesTopAppBar(
    onSortNameAscending: () -> Unit,
    onSortNameDescending: () -> Unit,
    onSortRatingAscending: () -> Unit,
    onSortRatingDescending: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        actions = {
            SortMoviesMenu(
                onSortNameAscending,
                onSortNameDescending,
                onSortRatingAscending,
                onSortRatingDescending
            )
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun SortMoviesMenu(
    onSortNameAscending: () -> Unit,
    onSortNameDescending: () -> Unit,
    onSortRatingAscending: () -> Unit,
    onSortRatingDescending: () -> Unit
) {
    TopAppBarDropdownMenu(
        iconContent = {
            Icon(
                painterResource(id = R.drawable.ic_sort_list),
                stringResource(id = R.string.menu_filter)
            )
        }
    ) { closeMenu ->
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.ascending_name)) },
            onClick = { onSortNameAscending(); closeMenu() }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.descending_name)) },
            onClick = { onSortNameDescending(); closeMenu() }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.ascending_rating)) },
            onClick = { onSortRatingAscending(); closeMenu() }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.descending_rating)) },
            onClick = { onSortRatingDescending(); closeMenu() }
        )
    }
}

@Composable
private fun TopAppBarDropdownMenu(
    iconContent: @Composable () -> Unit,
    content: @Composable ColumnScope.(() -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
        IconButton(onClick = { expanded = !expanded }) {
            iconContent()
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentSize(Alignment.TopEnd)
        ) {
            content { expanded = !expanded }
        }
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        MyModelScreen(fakeMovies, onMovieClicked = {})
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        MyModelScreen(fakeMovies, onMovieClicked = {})
    }
}

val fakeMovies = listOf(
    Movie(
        name = "The Dictator", desc = "Best comedy movie", rating = 10
    ),
    Movie(
        name = "Kho gaye hum kaha", desc = "Really nice Indian movie", rating = 9
    ),
    Movie(
        name = "Parasite", desc = "Oscar winning South Korean movie", rating = 10
    ),
)