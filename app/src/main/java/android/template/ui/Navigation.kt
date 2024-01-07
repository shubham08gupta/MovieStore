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

package android.template.ui

import android.template.ui.addeditmovietask.AddEditMovieScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.template.ui.mymodel.MyModelScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MyModelScreen(modifier = Modifier.padding(16.dp), onAddNewMovie = {
                navController.navigate("detail/-1")
            }, onMovieClicked = {
                navController.navigate("detail/${it.id}")
            })
        }
        composable(
            "detail/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    defaultValue = -1
                    nullable = false
                },
            )
        ) { entry ->
            val id = entry.arguments?.getInt("id")
            AddEditMovieScreen(
                toolbarTitle = if (id == -1 || id == null) {
                    "Add a movie"
                } else {
                    "Edit the movie"
                },
                onBack = { navController.navigateUp() }
            )
        }
    }
}
