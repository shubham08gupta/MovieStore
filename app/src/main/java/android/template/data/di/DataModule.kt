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

package android.template.data.di

import android.template.data.DefaultMovieRepository
import android.template.data.MovieRepository
import android.template.data.local.database.Movie
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsMyModelRepository(
        myModelRepository: DefaultMovieRepository
    ): MovieRepository
}

class FakeMovieRepository @Inject constructor() : MovieRepository {
    override val myModels: Flow<List<Movie>> = flowOf(fakeMyModels)
    override suspend fun add(movie: Movie) {
        fakeMyModels.add(movie)
    }

    override suspend fun getMovieById(id: Int): Movie {
        return fakeMyModels.first { it.id == id }
    }
}

val fakeMyModels = mutableListOf(
    Movie(
        name = "The Dictator",
        desc = "Best comedy movie",
        rating = 10
    ),
    Movie(
        name = "Kho gaye hum kaha",
        desc = "Really nice Indian movie",
        rating = 9
    ),
    Movie(
        name = "Parasite",
        desc = "Oscar winning South Korean movie",
        rating = 10
    ),
)
