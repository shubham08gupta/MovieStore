@file:OptIn(ExperimentalMaterial3Api::class)

package android.template.ui.addeditmovietask

import android.template.R
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import java.util.UUID

@Composable
fun AddEditMovieScreen(
    movieId: Int = -1,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddEditMovieScreenViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            AddEditMovieTopAppBar(title = R.string.menu_back, onBack = { onBack() }) {
                // TODO: save data
                onBack()
            }
        },
        modifier = modifier
    ) {
        Text(
            text = "Details page", modifier = Modifier.padding(it)
        )
    }
}


@Composable
fun AddEditMovieTopAppBar(@StringRes title: Int?, onBack: () -> Unit, onSave: () -> Unit) {
    TopAppBar(title = { if (title != null) Text(text = stringResource(title)) }, navigationIcon = {
        IconButton(onClick = onBack) {
            Icon(Icons.Filled.ArrowBack, stringResource(id = R.string.menu_back))
        }
    }, actions = {
        IconButton(onClick = onSave) {
            Icon(Icons.Filled.Check, stringResource(id = R.string.menu_save))
        }
    }, modifier = Modifier.fillMaxWidth()
    )
}