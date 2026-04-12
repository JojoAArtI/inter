package com.internshipuncle.feature_dashboard

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.MicNone
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.RecordVoiceOver
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.internshipuncle.core.design.CanvasWhite
import com.internshipuncle.core.design.CharcoalDark
import com.internshipuncle.core.design.DividerGray
import com.internshipuncle.core.design.GreenPositive
import com.internshipuncle.core.design.InkBlack
import com.internshipuncle.core.design.InternshipUncleTheme
import com.internshipuncle.core.design.PureWhite
import com.internshipuncle.core.design.RedNegative
import com.internshipuncle.core.design.SilverMist
import com.internshipuncle.core.design.SlateGray
import com.internshipuncle.core.design.SurfaceGray
import com.internshipuncle.core.design.SurfaceLight
import com.internshipuncle.core.ui.PlaceholderScreen
import com.internshipuncle.core.model.QueryResult
import com.internshipuncle.core.model.RepositoryStatus
import com.internshipuncle.data.repository.AuthRepository
import com.internshipuncle.data.repository.DashboardRepository
import com.internshipuncle.domain.model.DashboardActivityItem
import com.internshipuncle.domain.model.DashboardActivityType
import com.internshipuncle.domain.model.DashboardDeadlineItem
import com.internshipuncle.domain.model.DashboardSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ── State & ViewModel (unchanged logic) ────────────────────────────────

data class DashboardUiState(
    val isLoading: Boolean = true,
    val readinessScore: Int? = null,
    val readinessSummary: String = "Your readiness dashboard will populate once Supabase returns real prep data.",
    val latestResumeScore: Int? = null,
    val latestMockScore: Int? = null,
    val savedJobsCount: Int = 0,
    val upcomingDeadlines: List<DashboardDeadlineItem> = emptyList(),
    val recentActivity: List<DashboardActivityItem> = emptyList(),
    val nextStepSuggestions: List<String> = emptyList(),
    val isConfigured: Boolean = true,
    val isSigningOut: Boolean = false,
    val signOutError: String? = null,
    val errorMessage: String? = null
) {
    val hasContent: Boolean
        get() = (readinessScore ?: 0) > 0 ||
            latestResumeScore != null ||
            latestMockScore != null ||
            savedJobsCount > 0 ||
            upcomingDeadlines.isNotEmpty() ||
            recentActivity.isNotEmpty()

    val isEmpty: Boolean
        get() = !isLoading && errorMessage == null && !hasContent
}

private data class DashboardActionState(
    val isSigningOut: Boolean = false,
    val signOutError: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val signOutState = MutableStateFlow(DashboardActionState())

    val uiState: StateFlow<DashboardUiState> = combine(
        dashboardRepository.snapshot(),
        signOutState
    ) { snapshotResult, signOutUiState ->
        snapshotResult.toDashboardUiState().copy(
            isSigningOut = signOutUiState.isSigningOut,
            signOutError = signOutUiState.signOutError
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState()
    )

    fun refresh() { dashboardRepository.refresh() }

    fun signOut() {
        if (uiState.value.isSigningOut) return
        viewModelScope.launch {
            signOutState.update { it.copy(isSigningOut = true, signOutError = null) }
            val result = authRepository.signOut()
            signOutState.update {
                it.copy(isSigningOut = false, signOutError = result.toSignOutMessage())
            }
        }
    }
}

// ── Entry point ────────────────────────────────────────────────────────

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onOpenJobs: () -> Unit,
    onOpenSavedJobs: () -> Unit,
    onOpenResumeLab: () -> Unit,
    onOpenInterview: () -> Unit,
    onOpenJob: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when {
        uiState.isLoading -> PlaceholderScreen(
            eyebrow     = "Dashboard",
            title       = "Loading your readiness",
            description = "Pulling scores, deadlines and activity from Supabase.",
            actions     = {
                CircularProgressIndicator(
                    modifier    = Modifier.size(32.dp),
                    strokeWidth = 2.5.dp,
                    color       = InkBlack
                )
            }
        )

        uiState.errorMessage != null -> PlaceholderScreen(
            eyebrow     = "Dashboard",
            title       = "Dashboard unavailable",
            description = uiState.errorMessage ?: "The dashboard could not be loaded.",
            sections    = listOf(
                "Status" to if (uiState.isConfigured) "Configured but unavailable." else "Supabase config is missing."
            ),
            actions     = {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FintechPillButton(onClick = viewModel::refresh, label = "Retry")
                }
            }
        )

        else -> DashboardContent(
            uiState          = uiState,
            onOpenJobs       = onOpenJobs,
            onOpenSavedJobs  = onOpenSavedJobs,
            onOpenResumeLab  = onOpenResumeLab,
            onOpenInterview  = onOpenInterview,
            onOpenJob        = onOpenJob,
            onRefresh        = viewModel::refresh,
            onSignOut        = viewModel::signOut
        )
    }
}

// ── Main Dashboard Content — fintech layout ────────────────────────────

@Composable
private fun DashboardContent(
    uiState: DashboardUiState,
    onOpenJobs: () -> Unit,
    onOpenSavedJobs: () -> Unit,
    onOpenResumeLab: () -> Unit,
    onOpenInterview: () -> Unit,
    onOpenJob: (String) -> Unit,
    onRefresh: () -> Unit,
    onSignOut: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CanvasWhite)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // ── 1. Welcome header (avatar + name + icons)
        WelcomeHeader(onRefresh = onRefresh, onSignOut = onSignOut)

        Spacer(Modifier.height(24.dp))

        // ── 2. Hero readiness score (fintech balance style)
        ReadinessHeroSection(uiState = uiState)

        Spacer(Modifier.height(28.dp))

        // ── 3. Primary action buttons (circular icons row — like Top-up/Send)
        PrimaryActionRow(
            onOpenJobs      = onOpenJobs,
            onOpenResumeLab = onOpenResumeLab,
            onOpenInterview = onOpenInterview,
            onSignOut       = onSignOut,
            isSigningOut    = uiState.isSigningOut
        )

        Spacer(Modifier.height(28.dp))

        // ── 4. Shortcuts row (smaller circular icons — like Transport/Internet)
        ShortcutsSection(
            savedJobsCount       = uiState.savedJobsCount,
            onOpenSavedJobs      = onOpenSavedJobs,
            onOpenResumeLab      = onOpenResumeLab,
            onOpenInterview      = onOpenInterview,
            onOpenJobs           = onOpenJobs
        )

        Spacer(Modifier.height(28.dp))

        // ── 5. Metrics strip (2-column inline tiles)
        MetricsStrip(
            resumeScore     = uiState.latestResumeScore,
            mockScore       = uiState.latestMockScore,
            savedJobsCount  = uiState.savedJobsCount,
            deadlinesCount  = uiState.upcomingDeadlines.size
        )

        Spacer(Modifier.height(28.dp))

        // ── 6. Recent activity (transaction-list style)
        RecentActivitySection(
            activities      = uiState.recentActivity,
            onOpenJob       = onOpenJob,
            onOpenResumeLab = onOpenResumeLab,
            onOpenInterview = onOpenInterview
        )

        Spacer(Modifier.height(28.dp))

        // ── 7. Upcoming deadlines
        if (uiState.upcomingDeadlines.isNotEmpty()) {
            DeadlinesSection(
                deadlines  = uiState.upcomingDeadlines,
                onOpenJob  = onOpenJob
            )
            Spacer(Modifier.height(28.dp))
        }

        // ── 8. Next steps (if suggestions exist)
        if (uiState.nextStepSuggestions.isNotEmpty()) {
            NextStepsSection(suggestions = uiState.nextStepSuggestions)
            Spacer(Modifier.height(28.dp))
        }

        // ── Sign-out error notice
        uiState.signOutError?.let { error ->
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                ErrorNotice(message = error)
            }
            Spacer(Modifier.height(16.dp))
        }

        // Bottom padding for nav bar
        Spacer(Modifier.height(96.dp))
    }
}

// ── 1. Welcome Header ─────────────────────────────────────────────────
// Left: circle avatar with initials + "Welcome back, / User"
// Right: notification bell + settings gear (outlined circles)

@Composable
private fun WelcomeHeader(onRefresh: () -> Unit, onSignOut: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        // Avatar + greeting
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment     = Alignment.CenterVertically
        ) {
            // Initials avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(InkBlack),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = "IU",           // Internship Uncle initials
                    color      = PureWhite,
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            Column {
                Text(
                    text  = "Welcome back,",
                    style = MaterialTheme.typography.bodySmall,
                    color = SlateGray
                )
                Text(
                    text       = "Internship Uncle",
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color      = InkBlack
                )
            }
        }

        // Right action icons
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HeaderIconButton(icon = Icons.Outlined.Star, onClick = onSignOut)
            HeaderIconButton(icon = Icons.Outlined.Notifications, onClick = onRefresh)
        }
    }
}

@Composable
private fun HeaderIconButton(icon: ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(SurfaceGray)
            .clickable(
                indication        = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick           = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector        = icon,
            contentDescription = null,
            tint               = InkBlack,
            modifier           = Modifier.size(20.dp)
        )
    }
}

// ── 2. Readiness Hero Section ─────────────────────────────────────────
// Like the large balance number in fintech apps

@Composable
private fun ReadinessHeroSection(uiState: DashboardUiState) {
    Column(
        modifier            = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text  = "Current readiness",
            style = MaterialTheme.typography.bodySmall,
            color = SlateGray
        )

        Spacer(Modifier.height(6.dp))

        // Large score number — like ₦674,981.65 in screenshot
        val scoreInt     = uiState.readinessScore ?: 0
        val scoreFraction = scoreInt % 10

        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text       = "$scoreInt",
                style      = MaterialTheme.typography.displayLarge.copy(fontSize = 52.sp),
                color      = InkBlack,
                fontWeight = FontWeight.Bold
            )
            Text(
                text       = ".$scoreFraction",
                style      = MaterialTheme.typography.displayLarge.copy(fontSize = 30.sp),
                color      = SlateGray,
                modifier   = Modifier.padding(bottom = 6.dp)
            )
            Text(
                text     = " / 100",
                style    = MaterialTheme.typography.bodyMedium,
                color    = SilverMist,
                modifier = Modifier.padding(bottom = 10.dp, start = 4.dp)
            )
        }

        Spacer(Modifier.height(8.dp))

        // Progress bar (thin)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(SurfaceGray)
        ) {
            val progress = (scoreInt.coerceIn(0, 100) / 100f)
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(InkBlack)
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text  = uiState.readinessSummary.take(80),
            style = MaterialTheme.typography.bodySmall,
            color = SlateGray,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ── 3. Primary Action Row ─────────────────────────────────────────────
// Matches screenshot: "Top-up" (filled dark circle) + 3 outlined circles
// First button is filled black (primary CTA), rest are outlined

@Composable
private fun PrimaryActionRow(
    onOpenJobs: () -> Unit,
    onOpenResumeLab: () -> Unit,
    onOpenInterview: () -> Unit,
    onSignOut: () -> Unit,
    isSigningOut: Boolean
) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Primary action — filled dark circle (like "Top-up" in screenshot)
        PrimaryCircleAction(
            icon    = Icons.Outlined.Add,
            label   = "Find Jobs",
            filled  = true,
            onClick = onOpenJobs
        )
        PrimaryCircleAction(
            icon    = Icons.Outlined.Send,
            label   = "Resume",
            filled  = false,
            onClick = onOpenResumeLab
        )
        PrimaryCircleAction(
            icon    = Icons.Outlined.MicNone,
            label   = "Interview",
            filled  = false,
            onClick = onOpenInterview
        )
        PrimaryCircleAction(
            icon    = Icons.Outlined.MoreHoriz,
            label   = if (isSigningOut) "..." else "More",
            filled  = false,
            onClick = onSignOut
        )
    }
}

@Composable
private fun PrimaryCircleAction(
    icon: ImageVector,
    label: String,
    filled: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(if (filled) InkBlack else CanvasWhite)
                .then(
                    if (!filled) Modifier.then(
                        Modifier.background(SurfaceGray, CircleShape)
                    ) else Modifier
                )
                .clickable(
                    indication        = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick           = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = icon,
                contentDescription = label,
                tint               = if (filled) PureWhite else InkBlack,
                modifier           = Modifier.size(24.dp)
            )
        }

        Text(
            text      = label,
            style     = MaterialTheme.typography.labelSmall,
            color     = InkBlack,
            fontWeight = FontWeight.Medium
        )
    }
}

// ── 4. Shortcuts Section ──────────────────────────────────────────────
// Matches screenshot: "Shortcuts" label + row of smaller outlined icons

@Composable
private fun ShortcutsSection(
    savedJobsCount: Int,
    onOpenSavedJobs: () -> Unit,
    onOpenResumeLab: () -> Unit,
    onOpenInterview: () -> Unit,
    onOpenJobs: () -> Unit
) {
    Column(
        modifier            = Modifier
            .fillMaxWidth()
            .background(SurfaceGray)
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(
                text       = "Shortcuts",
                style      = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color      = InkBlack
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ShortcutItem(
                icon    = Icons.Outlined.BusinessCenter,
                label   = "Browse",
                onClick = onOpenJobs
            )
            ShortcutItem(
                icon    = Icons.Outlined.BookmarkBorder,
                label   = "Saved (${savedJobsCount})",
                onClick = onOpenSavedJobs
            )
            ShortcutItem(
                icon    = Icons.Outlined.Description,
                label   = "Resume",
                onClick = onOpenResumeLab
            )
            ShortcutItem(
                icon    = Icons.Outlined.RecordVoiceOver,
                label   = "Interview",
                onClick = onOpenInterview
            )
            ShortcutItem(
                icon    = Icons.Outlined.Analytics,
                label   = "Analyze",
                onClick = onOpenJobs
            )
        }
    }
}

@Composable
private fun ShortcutItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(PureWhite)
                .clickable(
                    indication        = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick           = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = icon,
                contentDescription = label,
                tint               = InkBlack,
                modifier           = Modifier.size(22.dp)
            )
        }

        Text(
            text  = label.take(10),
            style = MaterialTheme.typography.labelSmall,
            color = SlateGray,
            maxLines = 1
        )
    }
}

// ── 5. Metrics Strip ──────────────────────────────────────────────────
// 2x2 grid of compact metric tiles (monochrome)

@Composable
private fun MetricsStrip(
    resumeScore: Int?,
    mockScore: Int?,
    savedJobsCount: Int,
    deadlinesCount: Int
) {
    Column(
        modifier            = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text       = "Your metrics",
            style      = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color      = InkBlack
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MetricTile(
                modifier = Modifier.weight(1f),
                label    = "Resume",
                value    = resumeScore?.toString() ?: "--",
                detail   = if (resumeScore == null) "No roast yet" else "Latest score",
                positive = resumeScore?.let { it >= 70 }
            )
            MetricTile(
                modifier = Modifier.weight(1f),
                label    = "Interview",
                value    = mockScore?.toString() ?: "--",
                detail   = if (mockScore == null) "No session yet" else "Latest score",
                positive = mockScore?.let { it >= 70 }
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MetricTile(
                modifier = Modifier.weight(1f),
                label    = "Saved Jobs",
                value    = savedJobsCount.toString(),
                detail   = if (savedJobsCount == 0) "Empty shortlist" else "Roles tracked",
                positive = savedJobsCount > 0
            )
            MetricTile(
                modifier = Modifier.weight(1f),
                label    = "Deadlines",
                value    = deadlinesCount.toString(),
                detail   = if (deadlinesCount == 0) "None watching" else "Upcoming",
                positive = null
            )
        }
    }
}

@Composable
private fun MetricTile(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    detail: String,
    positive: Boolean?
) {
    Surface(
        modifier        = modifier,
        shape           = RoundedCornerShape(18.dp),
        color           = SurfaceGray,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier            = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text  = label,
                style = MaterialTheme.typography.labelSmall,
                color = SlateGray
            )
            Text(
                text       = value,
                style      = MaterialTheme.typography.displaySmall.copy(fontSize = 28.sp),
                fontWeight = FontWeight.Bold,
                color      = when (positive) {
                    true  -> InkBlack
                    false -> SlateGray
                    null  -> InkBlack
                }
            )
            Text(
                text  = detail,
                style = MaterialTheme.typography.bodySmall,
                color = SilverMist
            )
        }
    }
}

// ── 6. Recent Activity — transaction list ─────────────────────────────

@Composable
private fun RecentActivitySection(
    activities: List<DashboardActivityItem>,
    onOpenJob: (String) -> Unit,
    onOpenResumeLab: () -> Unit,
    onOpenInterview: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        // Section header (matches "Recent transactions  See all")
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(
                text       = "Recent activity",
                style      = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color      = InkBlack
            )
            Text(
                text  = "See all",
                style = MaterialTheme.typography.bodySmall,
                color = SlateGray,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(16.dp))

        if (activities.isEmpty()) {
            EmptyActivityHint()
        } else {
            // Transaction-list style items with dividers
            Surface(
                shape           = RoundedCornerShape(18.dp),
                color           = SurfaceGray,
                shadowElevation = 0.dp,
                modifier        = Modifier.fillMaxWidth()
            ) {
                Column {
                    activities.take(5).forEachIndexed { index, activity ->
                        ActivityListItem(
                            item            = activity,
                            onOpenJob       = onOpenJob,
                            onOpenResumeLab = onOpenResumeLab,
                            onOpenInterview = onOpenInterview
                        )
                        if (index < minOf(activities.size - 1, 4)) {
                            HorizontalDivider(
                                modifier  = Modifier.padding(start = 68.dp),
                                color     = DividerGray,
                                thickness = 0.5.dp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActivityListItem(
    item: DashboardActivityItem,
    onOpenJob: (String) -> Unit,
    onOpenResumeLab: () -> Unit,
    onOpenInterview: () -> Unit
) {
    val (icon, bgColor) = when (item.type) {
        DashboardActivityType.SavedJob       -> Pair(Icons.Outlined.WorkOutline,      CharcoalDark)
        DashboardActivityType.ResumeRoast    -> Pair(Icons.Outlined.Description,      InkBlack)
        DashboardActivityType.MockInterview  -> Pair(Icons.Outlined.MicNone,          SlateGray)
    }

    val onAction: (() -> Unit)? = when (item.type) {
        DashboardActivityType.SavedJob       -> item.jobId?.let { { onOpenJob(it) } }
        DashboardActivityType.ResumeRoast    -> onOpenResumeLab
        DashboardActivityType.MockInterview  -> onOpenInterview
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled           = onAction != null,
                indication        = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick           = { onAction?.invoke() }
            )
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment     = Alignment.CenterVertically
    ) {
        // Circle icon (like transaction avatars in screenshot)
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = icon,
                contentDescription = null,
                tint               = PureWhite,
                modifier           = Modifier.size(20.dp)
            )
        }

        // Title + timestamp
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text      = item.title,
                style     = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                color     = InkBlack,
                maxLines  = 1,
                overflow  = TextOverflow.Ellipsis
            )
            Text(
                text  = item.details.take(45),
                style = MaterialTheme.typography.bodySmall,
                color = SlateGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Right: score or amount (like ₦10,000 in screenshot)
        Column(horizontalAlignment = Alignment.End) {
            item.score?.let { score ->
                Text(
                    text       = "$score/100",
                    style      = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color      = if (score >= 70) GreenPositive else InkBlack
                )
            }
            item.createdAt?.takeIf(String::isNotBlank)?.let { ts ->
                Text(
                    text  = formatActivityTime(ts),
                    style = MaterialTheme.typography.bodySmall,
                    color = SilverMist
                )
            }
        }
    }
}

@Composable
private fun EmptyActivityHint() {
    Surface(
        shape  = RoundedCornerShape(16.dp),
        color  = SurfaceGray,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text  = "No activity yet",
                style = MaterialTheme.typography.titleSmall,
                color = InkBlack,
                fontWeight = FontWeight.Medium
            )
            Text(
                text  = "Roast a resume, run a mock interview or save a role to get started.",
                style = MaterialTheme.typography.bodySmall,
                color = SlateGray
            )
        }
    }
}

// ── 7. Deadlines Section ──────────────────────────────────────────────

@Composable
private fun DeadlinesSection(
    deadlines: List<DashboardDeadlineItem>,
    onOpenJob: (String) -> Unit
) {
    Column(
        modifier            = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(
                text       = "Upcoming deadlines",
                style      = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color      = InkBlack
            )
        }

        Surface(
            shape  = RoundedCornerShape(18.dp),
            color  = SurfaceGray,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                deadlines.take(3).forEachIndexed { index, deadline ->
                    DeadlineListItem(item = deadline, onOpenJob = onOpenJob)
                    if (index < minOf(deadlines.size - 1, 2)) {
                        HorizontalDivider(
                            modifier  = Modifier.padding(start = 68.dp),
                            color     = DividerGray,
                            thickness = 0.5.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DeadlineListItem(
    item: DashboardDeadlineItem,
    onOpenJob: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication        = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onOpenJob(item.jobId) }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(InkBlack),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = Icons.Outlined.WorkOutline,
                contentDescription = null,
                tint               = PureWhite,
                modifier           = Modifier.size(20.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text      = item.title,
                style     = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                color     = InkBlack,
                maxLines  = 1,
                overflow  = TextOverflow.Ellipsis
            )
            Text(
                text  = item.company ?: "Unknown company",
                style = MaterialTheme.typography.bodySmall,
                color = SlateGray
            )
        }

        Text(
            text       = formatDeadlineLabel(item.deadline),
            style      = MaterialTheme.typography.labelSmall,
            color      = RedNegative,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ── 8. Next Steps Section ─────────────────────────────────────────────

@Composable
private fun NextStepsSection(suggestions: List<String>) {
    Column(
        modifier            = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text       = "Focus next",
            style      = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color      = InkBlack
        )

        Surface(
            shape  = RoundedCornerShape(18.dp),
            color  = SurfaceGray,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                suggestions.take(3).forEachIndexed { index, suggestion ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment     = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(InkBlack),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text  = "${index + 1}",
                                color = PureWhite,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text  = suggestion,
                            style = MaterialTheme.typography.bodyMedium,
                            color = SlateGray
                        )
                    }
                }
            }
        }
    }
}

// ── Shared helpers ────────────────────────────────────────────────────

@Composable
private fun ErrorNotice(message: String) {
    Surface(
        shape  = RoundedCornerShape(14.dp),
        color  = RedNegative.copy(alpha = 0.07f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text       = "Notice",
                style      = MaterialTheme.typography.labelMedium,
                color      = RedNegative,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text  = message,
                style = MaterialTheme.typography.bodySmall,
                color = SlateGray
            )
        }
    }
}

@Composable
private fun FintechPillButton(
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(50.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(InkBlack)
            .clickable(
                indication        = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick           = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text       = label,
            color      = PureWhite,
            style      = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            modifier   = Modifier.padding(horizontal = 28.dp)
        )
    }
}

// ── Mapping helpers ───────────────────────────────────────────────────

private fun QueryResult<DashboardSnapshot>.toDashboardUiState(): DashboardUiState {
    return when (this) {
        QueryResult.Loading         -> DashboardUiState(isLoading = true)
        QueryResult.NotConfigured   -> DashboardUiState(
            isLoading      = false,
            isConfigured   = false,
            errorMessage   = "Add `SUPABASE_URL` and `SUPABASE_PUBLISHABLE_KEY` or `SUPABASE_ANON_KEY` to load the dashboard."
        )
        QueryResult.BackendNotReady -> DashboardUiState(
            isLoading    = false,
            errorMessage = "The dashboard backend tables or functions are not ready yet."
        )
        is QueryResult.Failure      -> DashboardUiState(isLoading = false, errorMessage = message)
        is QueryResult.Success      -> data.toDashboardUiState()
    }
}

private fun DashboardSnapshot.toDashboardUiState(): DashboardUiState {
    return DashboardUiState(
        isLoading            = false,
        readinessScore       = readinessScore,
        readinessSummary     = buildReadinessSummary(),
        latestResumeScore    = latestResumeScore,
        latestMockScore      = latestMockScore,
        savedJobsCount       = savedJobsCount,
        upcomingDeadlines    = upcomingDeadlines,
        recentActivity       = recentActivity,
        nextStepSuggestions  = buildNextStepSuggestions(),
        isConfigured         = isConfigured
    )
}

private fun DashboardSnapshot.buildReadinessSummary(): String {
    val readiness = readinessScore ?: 0
    return when {
        !isConfigured                                                        -> "Connect Supabase before the dashboard can calculate readiness."
        latestResumeScore == null && latestMockScore == null && savedJobsCount == 0 ->
            "Start by saving one internship, then roast a resume and run a mock interview."
        readiness >= 80 -> "Your prep loop is strong. Keep tightening the role fit."
        readiness >= 50 -> "You have momentum. A cleaner resume score will move you forward."
        else            -> "You need more proof points. Save a role and add a resume or interview score."
    }
}

private fun DashboardSnapshot.buildNextStepSuggestions(): List<String> {
    return buildList {
        if (savedJobsCount == 0) add("Save one internship so the dashboard can track a real target.")
        if (latestResumeScore == null) add("Upload or roast a resume to get your first resume score.")
        if (latestMockScore == null) add("Run one mock interview so you can compare progress over time.")
        if (upcomingDeadlines.isEmpty()) add("Pick a role with a deadline so prep has a clear finish line.")
    }.take(3)
}

private fun formatDeadlineLabel(deadline: String): String = "Due ${parseDateLabel(deadline)}"

private fun formatActivityTime(createdAt: String): String {
    val parsed = parseDate(createdAt) ?: return createdAt.substringBefore("T")
    val elapsedMillis = System.currentTimeMillis() - parsed.time
    val minutes = (elapsedMillis / 60_000).coerceAtLeast(0)
    return when {
        minutes < 1          -> "Just now"
        minutes < 60         -> "${minutes}m ago"
        minutes < 60 * 24    -> "${minutes / 60}h ago"
        minutes < 60 * 24 * 7 -> "${minutes / (60 * 24)}d ago"
        else                 -> parseDateLabel(createdAt)
    }
}

private fun parseDateLabel(value: String): String {
    return parseDate(value)?.let { date ->
        SimpleDateFormat("dd MMM yyyy", Locale.US).format(date)
    } ?: value.substringBefore("T")
}

private fun parseDate(value: String): Date? {
    val formats = listOf(
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US),
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX",   Locale.US),
        SimpleDateFormat("yyyy-MM-dd",                Locale.US)
    )
    return formats.firstNotNullOfOrNull { format ->
        runCatching { format.parse(value) }.getOrNull()
    }
}

private fun RepositoryStatus.toSignOutMessage(): String? {
    return when (this) {
        RepositoryStatus.Success          -> null
        RepositoryStatus.NotConfigured    -> "Supabase client config is missing."
        RepositoryStatus.BackendNotReady  -> "The auth backend is not fully ready yet."
        is RepositoryStatus.Failure       -> message
    }
}
