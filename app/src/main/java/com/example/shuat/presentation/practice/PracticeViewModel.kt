package com.example.shuat.presentation.practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shuat.domain.model.AnswerRecord
import com.example.shuat.domain.model.Question
import com.example.shuat.domain.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 练习 ViewModel
 */
@HiltViewModel
class PracticeViewModel @Inject constructor(
    private val repository: QuestionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PracticeUiState())
    val uiState: StateFlow<PracticeUiState> = _uiState.asStateFlow()

    private var startTime: Long = 0

    fun loadQuestions(mode: String) {
        viewModelScope.launch {
            when {
                mode == "random" -> {
                    repository.getRandomQuestions(20).collect { questions ->
                        _uiState.value = _uiState.value.copy(
                            questions = questions,
                            currentQuestion = questions.firstOrNull(),
                            currentIndex = 0
                        )
                        startTimer()
                    }
                }
                mode.startsWith("category_") -> {
                    val category = mode.removePrefix("category_")
                    repository.getQuestionsByCategory(category).collect { questions ->
                        _uiState.value = _uiState.value.copy(
                            questions = questions,
                            currentQuestion = questions.firstOrNull(),
                            currentIndex = 0
                        )
                        startTimer()
                    }
                }
                else -> {
                    repository.getAllQuestions().collect { questions ->
                        _uiState.value = _uiState.value.copy(
                            questions = questions,
                            currentQuestion = questions.firstOrNull(),
                            currentIndex = 0
                        )
                        startTimer()
                    }
                }
            }
        }
    }

    private fun startTimer() {
        startTime = System.currentTimeMillis()
    }

    fun selectAnswer(answer: String) {
        _uiState.value = _uiState.value.copy(selectedAnswer = answer)
    }

    fun submitAnswer() {
        val state = _uiState.value
        val question = state.currentQuestion ?: return
        val selectedAnswer = state.selectedAnswer ?: return

        val isCorrect = selectedAnswer == question.answer
        val timeSpent = (System.currentTimeMillis() - startTime) / 1000

        // 保存答题记录
        viewModelScope.launch {
            val record = AnswerRecord(
                questionId = question.id,
                userAnswer = selectedAnswer,
                correctAnswer = question.answer,
                isCorrect = isCorrect,
                timeSpent = timeSpent
            )
            repository.insertAnswerRecord(record)

            // 如果答错，标记为错题
            if (!isCorrect) {
                repository.markAsWrong(question.id)
            }
        }

        _uiState.value = state.copy(
            showAnswer = true,
            isCorrect = isCorrect
        )
    }

    fun nextQuestion() {
        val state = _uiState.value
        val nextIndex = state.currentIndex + 1

        if (nextIndex < state.questions.size) {
            _uiState.value = state.copy(
                currentIndex = nextIndex,
                currentQuestion = state.questions[nextIndex],
                selectedAnswer = null,
                showAnswer = false,
                isCorrect = null
            )
            startTimer()
        } else {
            _uiState.value = state.copy(isFinished = true)
        }
    }

    fun toggleFavorite() {
        val question = _uiState.value.currentQuestion ?: return
        viewModelScope.launch {
            repository.toggleFavorite(question.id)
            _uiState.value = _uiState.value.copy(
                currentQuestion = question.copy(isFavorite = !question.isFavorite)
            )
        }
    }
}

/**
 * 练习 UI 状态
 */
data class PracticeUiState(
    val questions: List<Question> = emptyList(),
    val currentQuestion: Question? = null,
    val currentIndex: Int = 0,
    val selectedAnswer: String? = null,
    val showAnswer: Boolean = false,
    val isCorrect: Boolean? = null,
    val isFinished: Boolean = false
)
