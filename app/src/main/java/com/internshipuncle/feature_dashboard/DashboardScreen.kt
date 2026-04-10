package com.internshipuncle.feature_dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.internshipuncle.core.model.RepositoryStatus
import com.internshipuncle.core.ui.PlaceholderScreen
import com.internshipuncle.data.repository.AuthRepository
import com.internshipuncle.data.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DashboardUiState(
    val readinessScore: Int? = null,
    val summary: String = "Dashboard will aggregate saved jobs, next deadlines, roast movement, interview scores, and readiness.",
    val savedJobsCount: Int = 0,
    val resumesCount: Int = 0,
    val mockSessionsCount: Int = 0,
    val nextDeadline: String? = null,
    val isConfigured: Boolean = false,
    val isSigningOut: Boolean = false,
    val signOutError: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    dashboardRepository: DashboardRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val signOutState = MutableStateFlow(
        DashboardUiState(
            isSigningOut = false,
            signOutError = null
        )
    )

    val uiState: StateFlow<DashboardUiState> = combine(
        dashboardRepository.snapshot(),
        signOutState
    ) { snapshot, actionState ->
        DashboardUiState(
            readinessScore = snapshot.readinessScore,
            summary = "Dashboard will aggregate saved jobs, next deadlines, roast movement, interview scores, and readiness.",
            savedJobsCount = snapshot.savedJobsCount,
            resumesCount = snapshot.resumesCount,
            mockSessionsCount = snapshot.mockSessionsCount,
            nextDeadline = snapshot.nextDeadline,
            isConfigured = snapshot.isConfigured,
            isSigningOut = actionState.isSigningOut,
            signOutError = actionState.signOutError
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DashboardUiState()
        )

    fun signOut() {
        if (uiState.value.isSigningOut) {
            return
        }

        viewModelScope.launch {
            signOutState.update {
                it.copy(
                    isSigningOut = true,
                    signOutError = null
                )
            }

            val result = authRepository.signOut()

            signOutState.update {
                it.copy(
                    isSigningOut = false,
                    signOutError = when (result) {
                        RepositoryStatus.Success -> null
                        RepositoryStatus.NotConfigured -> "Supabase client config is missing."
                        RepositoryStatus.BackendNotReady -> "Auth backend is not fully ready yet."
                        is RepositoryStatus.Failure -> result.message
                    }
                )
            }
        }
    }
}

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PlaceholderScreen(
        eyebrow = "Dashboard",
        title = "Your prep should feel measurable.",
        description = "The dashboard is where the workflow comes back together instead of leaving users with disconnected feature histories.",
        sections = listOf(
            "Readiness placeholder" to (uiState.readinessScore?.let { "$it/100" } ?: "Waiting on backend scoring"),
            "Planned aggregation" to uiState.summary,
            "Saved jobs" to "${uiState.savedJobsCount}",
            "Resumes" to "${uiState.resumesCount}",
            "Mock sessions" to "${uiState.mockSessionsCount}",
            "Next deadline" to (uiState.nextDeadline ?: "No active deadline loaded"),
            "Supabase config" to if (uiState.isConfigured) "Configured" else "Missing",
            "Session action" to if (uiState.isSigningOut) "Signing out" else "Signed in"
        ),
        actions = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                uiState.signOutError?.let { error ->
                    Text(
                        text = error,
                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.error
                    )
                }
                Button(
                    onClick = viewModel::signOut,
                    enabled = !uiState.isSigningOut,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (uiState.isSigningOut) "Signing out..." else "Sign out")
                }
            }
        }
    )
}
