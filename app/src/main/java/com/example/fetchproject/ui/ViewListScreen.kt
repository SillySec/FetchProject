package com.example.fetchproject.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fetchproject.R
import com.example.fetchproject.data.repo.RepoListItem
import com.example.fetchproject.data.repo.RepoResult

@Composable
fun ViewListScreen(
    viewModel: ViewListViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    when (val state = uiState) {
        is RepoResult.Error -> Error(state, { viewModel.refreshList() }, modifier)
        RepoResult.Loading -> Loading(modifier)
        is RepoResult.Success -> Success(state.data, modifier)
    }
}

@Composable
fun Error(
    error: RepoResult.Error,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.error_screen_title),
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = when(error) {
                    is RepoResult.Error.Database -> stringResource(R.string.error_description_db)
                    RepoResult.Error.EmptyData -> stringResource(R.string.error_description_empty)
                    is RepoResult.Error.Network -> stringResource(R.string.error_description_network)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onRefresh,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(stringResource(R.string.error_screen_refresh_btn))
            }
        }

    }

}

@Composable
fun Loading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Success(list: List<RepoListItem>,modifier: Modifier = Modifier) {
    val groupedByListIdMap = list.groupBy {it.listId}
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        groupedByListIdMap.forEach { (listId, listItems) ->
            stickyHeader {
                Text(
                    text = "List ID - $listId",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.background(MaterialTheme.colorScheme.tertiaryContainer)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)

                )
            }

            items(listItems) { listItem ->
                ListItemRow(listItem)
            }
        }
    }

}

@Composable
fun ListItemRow(listItem: RepoListItem, modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Text(
                text = "${listItem.name}",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "id: ${listItem.id}, listId: ${listItem.listId}",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}