package com.example.fetchproject.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetchproject.data.repo.RepoListItem
import com.example.fetchproject.data.repo.RepoResult
import com.example.fetchproject.domain.RefreshListUseCase
import com.example.fetchproject.domain.ViewListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewListViewModel @Inject constructor(
    private val viewListUseCase: ViewListUseCase,
    private val refreshListUseCase: RefreshListUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<RepoResult<List<RepoListItem>>>(RepoResult.Loading)
    val uiState: StateFlow<RepoResult<List<RepoListItem>>> = _uiState.asStateFlow()

    // handle on the job so we can cancel if we want to start a new refresh action before the other finishes
    private var currentRefreshJob: Job? = null

    init {
        refreshList()
    }

    fun refreshList() {
        // cancel the current job if it exists
        currentRefreshJob?.cancel()

        // set the current job to our
        currentRefreshJob = viewModelScope.launch {
            _uiState.value = RepoResult.Loading
            val refreshResult = refreshListUseCase()
            if (refreshResult is RepoResult.Error) {
                // update UI state
                _uiState.value = refreshResult
                return@launch
            }

            // update UI state with result of Repo
            // set it cancellable in case we issue another refreshAction to prevent leaks
            viewListUseCase().cancellable().collect { result ->
                _uiState.value = result
            }
        }
    }

    // if a job is running, cancel it when we clear the viewModel
    override fun onCleared() {
        super.onCleared()
        currentRefreshJob?.cancel()
    }
}