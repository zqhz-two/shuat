package com.example.shuat.presentation.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shuat.domain.model.QuestionType
import com.example.shuat.presentation.theme.*

/**
 * 练习屏幕
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(
    mode: String,
    onNavigateBack: () -> Unit,
    viewModel: PracticeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(mode) {
        viewModel.loadQuestions(mode)
    }

    if (uiState.isFinished) {
        FinishScreen(
            totalQuestions = uiState.questions.size,
            onNavigateBack = onNavigateBack
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "练习 ${uiState.currentIndex + 1}/${uiState.questions.size}",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite() }) {
                        Icon(
                            imageVector = if (uiState.currentQuestion?.isFavorite == true)
                                Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "收藏",
                            tint = if (uiState.currentQuestion?.isFavorite == true)
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        uiState.currentQuestion?.let { question ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 题目信息
                item {
                    QuestionInfoCard(
                        type = question.type,
                        category = question.category,
                        difficulty = question.difficulty.name,
                        tags = question.tags
                    )
                }

                // 题目内容
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Text(
                                text = question.question,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // 选项
                if (question.type == QuestionType.SINGLE_CHOICE ||
                    question.type == QuestionType.MULTIPLE_CHOICE ||
                    question.type == QuestionType.TRUE_FALSE
                ) {
                    itemsIndexed(question.options) { index, option ->
                        OptionItem(
                            option = option,
                            index = index,
                            isSelected = uiState.selectedAnswer == index.toString(),
                            isCorrect = if (uiState.showAnswer) {
                                index.toString() == question.answer
                            } else null,
                            isWrong = if (uiState.showAnswer) {
                                index.toString() == uiState.selectedAnswer &&
                                        index.toString() != question.answer
                            } else false,
                            enabled = !uiState.showAnswer,
                            onClick = { viewModel.selectAnswer(index.toString()) }
                        )
                    }
                }

                // 解析
                if (uiState.showAnswer) {
                    item {
                        ExplanationCard(
                            explanation = question.explanation,
                            isCorrect = uiState.isCorrect ?: false
                        )
                    }
                }

                // 按钮
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    if (!uiState.showAnswer) {
                        Button(
                            onClick = { viewModel.submitAnswer() },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = uiState.selectedAnswer != null,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("提交答案", modifier = Modifier.padding(vertical = 8.dp))
                        }
                    } else {
                        Button(
                            onClick = { viewModel.nextQuestion() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("下一题", modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuestionInfoCard(
    type: QuestionType,
    category: String,
    difficulty: String,
    tags: List<String>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        InfoChip(text = type.name)
        InfoChip(text = category)
        InfoChip(text = difficulty)
        tags.take(2).forEach { tag ->
            InfoChip(text = tag)
        }
    }
}

@Composable
private fun InfoChip(text: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun OptionItem(
    option: String,
    index: Int,
    isSelected: Boolean,
    isCorrect: Boolean?,
    isWrong: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isCorrect == true -> Success.copy(alpha = 0.1f)
        isWrong -> Error.copy(alpha = 0.1f)
        isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        else -> MaterialTheme.colorScheme.surface
    }

    val borderColor = when {
        isCorrect == true -> Success
        isWrong -> Error
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(enabled = enabled, onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${'A' + index}.",
                style = MaterialTheme.typography.titleMedium,
                color = borderColor,
                modifier = Modifier.width(32.dp)
            )
            Text(
                text = option,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            if (isCorrect == true) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "正确",
                    tint = Success
                )
            } else if (isWrong) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "错误",
                    tint = Error
                )
            }
        }
    }
}

@Composable
private fun ExplanationCard(
    explanation: String,
    isCorrect: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCorrect)
                Success.copy(alpha = 0.1f)
            else
                Error.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = if (isCorrect) Icons.Default.CheckCircle
                    else Icons.Default.Cancel,
                    contentDescription = if (isCorrect) "正确" else "错误",
                    tint = if (isCorrect) Success else Error
                )
                Text(
                    text = if (isCorrect) "回答正确！" else "回答错误",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isCorrect) Success else Error
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "解析：",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = explanation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun FinishScreen(
    totalQuestions: Int,
    onNavigateBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "完成",
                modifier = Modifier.size(80.dp),
                tint = Success
            )
            Text(
                text = "练习完成！",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "已完成 $totalQuestions 道题目",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(
                onClick = onNavigateBack,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("返回首页", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            }
        }
    }
}
