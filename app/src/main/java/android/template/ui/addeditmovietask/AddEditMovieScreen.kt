@file:OptIn(ExperimentalMaterial3Api::class)

package android.template.ui.addeditmovietask

import android.template.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMovieScreen(
    toolbarTitle: String,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: AddEditMovieScreenViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            AddEditMovieTopAppBar(title = toolbarTitle, onBack = { onBack() }) {
                viewModel.saveMovie()
                onBack()
            }
        },
        modifier = modifier
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            TextField(
                value = viewModel.name,
                onValueChange = { name ->
                    viewModel.updateName(name)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Enter movie name")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = viewModel.desc,
                onValueChange = viewModel::updateDesc,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Enter movie description")
                },
                minLines = 4
            )
            Spacer(modifier = Modifier.height(16.dp))

            var expanded by remember { mutableStateOf(false) }
            val options = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

            // We want to react on tap/press on TextField to show menu
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
            ) {
                TextField(
                    // The `menuAnchor` modifier must be passed to the text field for correctness.
                    value = viewModel.rating.toString(),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true,
                    onValueChange = {},
                    label = {
                        Text(text = "Select rating")
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption.toString()) },
                            onClick = {
                                viewModel.updateRating(selectionOption)
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMovieTopAppBar(title: String, onBack: () -> Unit, onSave: () -> Unit) {
    TopAppBar(title = { Text(text = title) }, navigationIcon = {
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