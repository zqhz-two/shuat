package com.example.shuat.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shuat.domain.model.CategoryStatistics
import com.example.shuat.domain.model.Statistics
import com.example.shuat.domain.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 统计 ViewModel
 */
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: QuestionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    init {
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            repository.getStatistics().collect { stats ->
                _uiState.value = _uiState.value.copy(statistics = stats)
            }
        }

        viewModelScope.launch {
            repository.getCategoryStatistics().collect { categoryStats ->
                _uiState.value = _uiState.value.copy(categoryStatistics = categoryStats)
            }
        }
    }
}

/**
 * 统计 UI 状态
 */
data class StatisticsUiState(
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
    val categoryStatistics: List<CategoryStatistics> = emptyList()
)
