package android.template.ui.addeditmovietask

import android.template.data.MovieRepository
import android.template.data.local.database.Movie
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditMovieScreenViewModel @Inject constructor(
    private val movieRepo: MovieRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val movieId: Int? = savedStateHandle["id"]

    var name by mutableStateOf("")
        private set

    var desc by mutableStateOf("")
        private set

    var rating by mutableIntStateOf(0)
        private set


//    private val shouldEnableSave: StateFlow<Boolean> = name. name.map {
//        it.isNotBlank()
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        if (movieId != null && movieId != -1) {
            // user wants to edit the movie, fetch it's details
            fetchMovieDetails(movieId)
        }
    }

    private fun fetchMovieDetails(movieId: Int) = viewModelScope.launch {
        with(movieRepo.getMovieById(movieId)) {
            updateName(name)
            updateDesc(desc)
            updateRating(rating)
        }
    }

    private fun saveMovieId() {
        savedStateHandle["id"] = movieId
    }

    fun updateName(name: String) {
        this.name = name
    }

    fun updateDesc(desc: String) {
        this.desc = desc
    }

    fun updateRating(rating: Int) {
        this.rating = rating
    }

    fun saveMovie() {
        check(name.isNotBlank()) {
            throw RuntimeException("Cannot save a movie without a name")
        }
        viewModelScope.launch {
            movieRepo.add(
                Movie(
                    name = name,
                    desc = desc,
                    rating = rating
                )
            )
        }
    }
}