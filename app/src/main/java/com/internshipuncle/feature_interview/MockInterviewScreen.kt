package com.internshipuncle.feature_interview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.internshipuncle.core.ui.PlaceholderScreen
import com.internshipuncle.data.repository.InterviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class MockInterviewUiState(
    val role: String = "Android Intern",
    val modes: String = "quick, full, pressure, resume_crossfire",
    val previousSessionCount: Int = 0
)

@HiltViewModel
class MockInterviewViewModel @Inject constructor(
    interviewRepository: InterviewRepository
) : ViewModel() {
    val uiState: StateFlow<MockInterviewUiState> = interviewRepository.recentSessions()
        .map { sessions ->
            MockInterviewUiState(
                previousSessionCount = sessions.size
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MockInterviewUiState()
        )
}

@Composable
fun MockInterviewScreen(
    viewModel: MockInterviewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PlaceholderScreen(
        eyebrow = "Interview Prep",
        title = "Practice for the role you actually want.",
        description = "This shell keeps interview prep tied to the selected role and reserves evaluation for backend functions.",
        sections = listOf(
            "Default role" to uiState.role,
            "Future modes" to uiState.modes,
            "Planned output" to "Structured question sets, saved answers, answer scoring, improved answers, and session summaries.",
            "Stored sessions" to "${uiState.previousSessionCount}"
        )
    )
}
