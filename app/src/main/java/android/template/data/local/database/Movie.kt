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

package android.template.data.local.database

import androidx.annotation.IntRange
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity
data class Movie(
    val name: String,
    val desc: String = "",
    @IntRange(
        from = 0, to = 10
    )
    val rating: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

@Dao
interface MyModelDao {
    @Query("SELECT * FROM movie ORDER BY id DESC")
    fun getAllMoviesFlow(): Flow<List<Movie>>

    @Query("SELECT * FROM movie WHERE id IS :id")
    suspend fun getMovieById(id: Int): Movie

    @Insert
    suspend fun insertMovie(item: Movie)
}
