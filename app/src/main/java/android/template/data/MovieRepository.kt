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

import android.template.data.local.database.Movie
import android.template.data.local.database.MyModelDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface MovieRepository {
    val myModels: Flow<List<Movie>>
    suspend fun add(movie: Movie)

    suspend fun getMovieById(id: Int): Movie
}

class DefaultMovieRepository @Inject constructor(
    private val myModelDao: MyModelDao
) : MovieRepository {

    override val myModels: Flow<List<Movie>> = myModelDao.getAllMoviesFlow()

    override suspend fun add(movie: Movie) {
        myModelDao.insertMovie(movie)
    }

    override suspend fun getMovieById(id: Int): Movie {
        return myModelDao.getMovieById(id)
    }
}
