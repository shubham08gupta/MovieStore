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

package android.template.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import android.template.data.local.database.Movie
import android.template.data.local.database.MyModelDao
import android.template.ui.mymodel.fakeMovies

/**
 * Unit tests for [DefaultMovieRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DefaultMovieRepositoryTest {

    @Test
    fun myModels_newItemSaved_itemIsReturned() = runTest {
        val repository = DefaultMovieRepository(FakeMyModelDao())

        repository.add(fakeMovies.first())

        assertEquals(repository.moviesFlow.first().size, 1)
    }

}

private class FakeMyModelDao : MyModelDao {

    private val data = mutableListOf<Movie>()

    override fun getAllMoviesFlow(): Flow<List<Movie>> = flow {
        emit(data)
    }

    override suspend fun getMovieById(id: Int): Movie {
        return data.first { it.id == id }
    }

    override suspend fun insertMovie(item: Movie) {
        data.add(0, item)
    }
}
