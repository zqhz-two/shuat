package com.example.shuat.presentation.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shuat.domain.model.Question
import com.example.shuat.domain.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 复习 ViewModel
 */
@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val repository: QuestionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    fun loadQuestions(type: String) {
        viewModelScope.launch {
            when (type) {
                "wrong" -> {
                    repository.getWrongQuestions().collect { questions ->
                        _uiState.value = _uiState.value.copy(
                            questions = questions,
                            type = type
                        )
                    }
                }
                "favorite" -> {
                    repository.getFavoriteQuestions().collect { questions ->
                        _uiState.value = _uiState.value.copy(
                            questions = questions,
                            type = type
                        )
                    }
                }
            }
        }
    }

    fun toggleFavorite(questionId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(questionId)
        }
    }

    fun removeFromWrong(questionId: String) {
        viewModelScope.launch {
            repository.removeFromWrong(questionId)
        }
    }
}

/**
 * 复习 UI 状态
 */
data class ReviewUiState(
    val questions: List<Question> = emptyList(),
    val type: String = ""
)
