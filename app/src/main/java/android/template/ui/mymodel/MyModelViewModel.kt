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

import android.template.data.MovieRepository
import android.template.data.local.database.Movie
import android.template.ui.mymodel.MyModelUiState.Error
import android.template.ui.mymodel.MyModelUiState.Loading
import android.template.ui.mymodel.MyModelUiState.Success
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyModelViewModel @Inject constructor(
    movieRepository: MovieRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _sortType: StateFlow<MoviesSortType> = savedStateHandle.getStateFlow(
        MOVIES_SORT_SAVED_STATE_KEY,
        MoviesSortType.SORT_NAME_ASCENDING
    )

    val uiState: StateFlow<MyModelUiState> = combine(
        movieRepository.moviesFlow,
        _sortType
    ) { movies: List<Movie>, moviesSortType: MoviesSortType ->
        when (moviesSortType) {
            MoviesSortType.SORT_NAME_ASCENDING -> movies.sortedWith(
                compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
            )

            MoviesSortType.SORT_NAME_DESCENDING -> movies.sortedWith(
                compareByDescending(String.CASE_INSENSITIVE_ORDER) { it.name }
            )

            MoviesSortType.SORT_RATING_ASCENDING -> movies.sortedBy { it.rating }
            MoviesSortType.SORT_RATING_DESCENDING -> movies.sortedByDescending { it.rating }
        }
    }.map<List<Movie>, MyModelUiState>(::Success)
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun sortMoviesBy(sortType: MoviesSortType) {
        if (uiState.value !is Success) {
            return
        }
        savedStateHandle[MOVIES_SORT_SAVED_STATE_KEY] = sortType
    }
}

sealed interface MyModelUiState {
    data object Loading : MyModelUiState
    data class Error(val throwable: Throwable) : MyModelUiState
    data class Success(val data: List<Movie>) : MyModelUiState
}

const val MOVIES_SORT_SAVED_STATE_KEY = "MOVIES_SORT_SAVED_STATE_KEY"
