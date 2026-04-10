package com.internshipuncle.navigation

sealed class AppDestination(val route: String) {
    data object Splash : AppDestination("splash")
    data object Login : AppDestination("login")
    data object Signup : AppDestination("signup")
    data object Onboarding : AppDestination("onboarding")
    data object Jobs : AppDestination("jobs")
    data object SavedJobs : AppDestination("jobs/saved")
    data object JobDetail : AppDestination("job/{jobId}") {
        fun createRoute(jobId: String) = "job/$jobId"
    }

    data object Analysis : AppDestination("analysis/{jobId}") {
        fun createRoute(jobId: String) = "analysis/$jobId"
    }

    data object ResumeUpload : AppDestination("resume/upload")
    data object ResumeRoast : AppDestination("resume/roast/{resumeId}") {
        fun createRoute(resumeId: String) = "resume/roast/$resumeId"
    }

    data object MockInterview : AppDestination("interview/mock")
    data object Dashboard : AppDestination("dashboard")
}
