package com.example.jetpack.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jetpack.core.common.Resource
import com.example.jetpack.domain.model.Movie
import com.example.jetpack.domain.model.TvShow
import com.example.jetpack.domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val heroMovie: Movie? = null,
    val trending: List<Movie> = emptyList(),
    val nowPlaying: List<Movie> = emptyList(),
    val popular: List<Movie> = emptyList(),
    val topRated: List<Movie> = emptyList(),
    val tvShows: List<TvShow> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            repository.getTrendingMovies().collect { res ->
                when (res) {
                    is Resource.Success -> {
                        val movies = res.data
                        _uiState.update { state ->
                            state.copy(
                                trending = movies,
                                heroMovie = movies.firstOrNull(),
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(error = res.message, isLoading = false) }
                    }
                    is Resource.Loading -> Unit
                }
            }
        }

        viewModelScope.launch {
            repository.getNowPlayingMovies().collect { res ->
                if (res is Resource.Success) {
                    _uiState.update { it.copy(nowPlaying = res.data) }
                }
            }
        }

        viewModelScope.launch {
            repository.getPopularMovies().collect { res ->
                if (res is Resource.Success) {
                    _uiState.update { it.copy(popular = res.data) }
                }
            }
        }

        viewModelScope.launch {
            repository.getTopRatedMovies().collect { res ->
                if (res is Resource.Success) {
                    _uiState.update { it.copy(topRated = res.data) }
                }
            }
        }

        viewModelScope.launch {
            repository.getPopularTvShows().collect { res ->
                if (res is Resource.Success) {
                    _uiState.update { it.copy(tvShows = res.data) }
                }
            }
        }
    }

    companion object {
        fun provideFactory(repository: MovieRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomeViewModel(repository) as T
                }
            }
    }
}

