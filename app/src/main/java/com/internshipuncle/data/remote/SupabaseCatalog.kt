package com.internshipuncle.data.remote

object SupabaseTables {
    const val PROFILES = "profiles"
    const val JOBS = "jobs"
    const val JOB_ANALYSIS = "job_analysis"
    const val SAVED_JOBS = "saved_jobs"
    const val RESUMES = "resumes"
    const val RESUME_ROASTS = "resume_roasts"
    const val MOCK_SESSIONS = "mock_sessions"
}

object SupabaseBuckets {
    const val RESUME_UPLOADS = "resume-uploads"
    const val GENERATED_RESUMES = "generated-resumes"
}

object SupabaseFunctions {
    const val PARSE_RESUME = "parse-resume"
    const val ROAST_RESUME = "roast-resume"
    const val GENERATE_MOCK_SESSION = "generate-mock-session"
    const val EVALUATE_ANSWER = "evaluate-answer"
}
