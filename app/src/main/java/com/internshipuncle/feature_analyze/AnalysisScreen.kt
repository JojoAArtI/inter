package com.internshipuncle.feature_analyze

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.internshipuncle.core.ui.PlaceholderScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AnalysisUiState(
    val jobId: String,
    val summary: String,
    val gapSummary: String
)

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val state = MutableStateFlow(
        AnalysisUiState(
            jobId = checkNotNull(savedStateHandle["jobId"]),
            summary = "JD analysis will come from a secure backend function and persist to the `job_analysis` table.",
            gapSummary = "Expected output stays structured: summary, role reality, skills, keywords, interview topics, and difficulty."
        )
    )

    val uiState: StateFlow<AnalysisUiState> = state.asStateFlow()
}

@Composable
fun AnalysisScreen(
    viewModel: AnalysisViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PlaceholderScreen(
        eyebrow = "Analyze",
        title = "Turn the JD into something useful.",
        description = "This screen is the client placeholder for `analyze-job`. The Android app will render structured JSON, not raw AI paragraphs.",
        sections = listOf(
            "Linked job" to uiState.jobId,
            "Backend contract" to uiState.summary,
            "UI payload" to uiState.gapSummary
        )
    )
}
