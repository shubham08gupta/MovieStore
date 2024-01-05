package android.template.ui.addeditmovietask

import android.template.data.MyModelRepository
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddEditMovieScreenViewModel @Inject constructor(
    repository: MyModelRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val movieId: Int? = savedStateHandle["id"]

    init {
//        movieId = repository.
    }

    fun saveMovieId() {
        savedStateHandle["id"] = movieId
    }
}