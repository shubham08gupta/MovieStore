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

import android.template.data.local.database.Movie
import android.template.ui.theme.MyApplicationTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MyModelScreen(
    modifier: Modifier = Modifier,
    viewModel: MyModelViewModel = hiltViewModel(),
    onAddNewMovie: () -> Unit,
    onMovieClicked: (name: Movie) -> Unit
) {
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { onAddNewMovie() }) {
            Icon(Icons.Filled.Add, "Floating action button.")
        }
    }) {
        val items by viewModel.uiState.collectAsStateWithLifecycle()
        if (items is MyModelUiState.Success) {
            MyModelScreen(
                items = (items as MyModelUiState.Success).data,
                onMovieClicked = { onMovieClicked(it) },
                modifier = modifier.padding(it)
            )
        }
    }
}

@Composable
internal fun MyModelScreen(
    items: List<Movie>, onMovieClicked: (name: Movie) -> Unit, modifier: Modifier = Modifier
) {
    items.forEach {
        Text("Saved item: $it")
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