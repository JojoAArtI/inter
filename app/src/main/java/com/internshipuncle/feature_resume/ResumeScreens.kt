package com.internshipuncle.feature_resume

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.internshipuncle.core.design.InternshipUncleTheme
import com.internshipuncle.core.ui.PlaceholderScreen
import com.internshipuncle.data.repository.ResumeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class ResumeUploadUiState(
    val note: String = "Resume upload, parsing, and roasting will stay backend-led through Supabase Storage + Edge Functions.",
    val existingResumeCount: Int = 0
)

data class ResumeRoastUiState(
    val resumeId: String,
    val structure: String,
    val latestKnownOverallScore: Int? = null
)

@HiltViewModel
class ResumeUploadViewModel @Inject constructor(
    resumeRepository: ResumeRepository
) : ViewModel() {
    val uiState: StateFlow<ResumeUploadUiState> = resumeRepository.resumes()
        .map { resumes ->
            ResumeUploadUiState(
                existingResumeCount = resumes.size
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ResumeUploadUiState()
        )
}

@HiltViewModel
class ResumeRoastViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    resumeRepository: ResumeRepository
) : ViewModel() {
    private val resumeId: String = checkNotNull(savedStateHandle["resumeId"])

    val uiState: StateFlow<ResumeRoastUiState> = resumeRepository.roastSummary(resumeId)
        .map { roastSummary ->
            ResumeRoastUiState(
                resumeId = resumeId,
                structure = "Overall score, ATS score, relevance, clarity, formatting, issues, missing keywords, weak bullets, rewritten bullets.",
                latestKnownOverallScore = roastSummary?.overallScore
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ResumeRoastUiState(
                resumeId = resumeId,
                structure = "Overall score, ATS score, relevance, clarity, formatting, issues, missing keywords, weak bullets, rewritten bullets."
            )
        )
}

@Composable
fun ResumeUploadScreen(
    viewModel: ResumeUploadViewModel = hiltViewModel(),
    onRoast: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PlaceholderScreen(
        eyebrow = "Resume Lab",
        title = "Upload once. Improve against the role.",
        description = uiState.note,
        sections = listOf(
            "Storage boundary" to "Original files belong in private Supabase Storage buckets.",
            "Client responsibility" to "The Android app handles selection, upload state, and result rendering. It does not parse or score the resume locally.",
            "Known resumes" to "${uiState.existingResumeCount}"
        ),
        actions = {
            Column(verticalArrangement = Arrangement.spacedBy(InternshipUncleTheme.spacing.small)) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onRoast
                ) {
                    Text("Open roast placeholder")
                }
            }
        }
    )
}

@Composable
fun ResumeRoastScreen(
    viewModel: ResumeRoastViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PlaceholderScreen(
        eyebrow = "Resume Roast",
        title = "Brutally honest, still actionable.",
        description = "The results surface is structured first so the eventual backend output can slot straight in.",
        sections = listOf(
            "Resume ID" to uiState.resumeId,
            "Expected response contract" to uiState.structure,
            "Latest stored score" to (uiState.latestKnownOverallScore?.toString() ?: "No roast stored yet")
        )
    )
}
