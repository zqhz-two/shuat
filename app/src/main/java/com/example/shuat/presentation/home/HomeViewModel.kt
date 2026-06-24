package com.example.shuat.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shuat.domain.model.Statistics
import com.example.shuat.domain.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 首页 ViewModel
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: QuestionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadStatistics()
        loadCategories()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            repository.getStatistics().collect { stats ->
                _uiState.value = _uiState.value.copy(statistics = stats)
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            repository.getAllCategories().collect { categories ->
                _uiState.value = _uiState.value.copy(categories = categories)
            }
        }
    }
}

/**
 * 首页 UI 状态
 */
data class HomeUiState(
    val statistics: Statistics = Statistics(
        totalQuestions = 0,
        answeredQuestions = 0,
        correctAnswers = 0,
        wrongAnswers = 0,
        favoriteCount = 0,
        wrongQuestionCount = 0,
        accuracy = 0f,
        averageTimeSpent = 0L
    ),
    val categories: List<String> = emptyList()
)
