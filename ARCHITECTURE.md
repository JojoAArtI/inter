# Architecture — Internship Uncle

## 1. High-Level Architecture

Internship Uncle should use a **mobile client + backend services + admin panel** architecture.

```text
Android App (Kotlin)
    |
    | HTTPS / Authenticated Requests
    v
Supabase Backend
    ├── Auth
    ├── Postgres Database
    ├── Storage Buckets
    ├── Row Level Security
    └── Edge Functions
            |
            v
        AI Provider APIs
```

Separate from this:

```text
Admin Web Panel
    |
    v
Supabase Backend
```

## 2. Architectural Principle

The app is AI-assisted, but the real product is a workflow system. That means:

- store structured data first
- generate reusable outputs
- do not depend on one-off giant text responses
- avoid placing API keys or model logic in the client

## 3. Main Components

## 3.1 Android Client
Responsible for:
- user interaction
- auth session handling
- screen state
- data display
- file selection/upload
- local caching
- offline-friendly reads where possible

Should **not** be responsible for:
- AI secret management
- prompt logic
- schema validation of AI outputs beyond display validation
- privileged data operations

## 3.2 Supabase
Responsible for:
- authentication
- database
- row-level access control
- file storage
- server-side functions
- persistence of structured outputs

## 3.3 Edge Functions
Responsible for:
- AI orchestration
- secure access to model APIs
- file processing orchestration
- data enrichment workflows
- structured output validation
- business logic that should not live on device

## 3.4 Admin Panel
Responsible for:
- uploading jobs
- editing jobs
- reviewing generated job analysis
- publishing/unpublishing jobs
- tracking usage metrics

## 4. Why Supabase Fits Well

Supabase is a good MVP backend because it provides:

- auth
- Postgres
- storage
- edge functions
- row-level security
- easy iteration speed

This makes it possible to move fast without separately building:
- auth service
- custom file storage
- a separate primary database layer

## 5. Backend Design Strategy

Use Supabase as the **system of record**.

### Persist in database
- jobs
- job analysis
- profiles
- resumes
- roast results
- mock sessions
- answers
- readiness data
- saved jobs

### Persist in storage
- uploaded resume files
- generated PDFs
- optionally company logos and assets

### Compute through functions
- parse resume
- analyze JD
- roast resume
- generate role-based resume
- generate and evaluate mock interviews

## 6. AI-Oriented Architecture Principles

## 6.1 Use Structured Outputs
Never rely on uncontrolled freeform paragraphs for UI-critical features.

Every important AI feature should return structured JSON:
- numeric scores
- issue lists
- keyword arrays
- bullet rewrites
- question sets
- answer evaluation fields

## 6.2 Precompute When Possible
Some data should be computed once and reused.

### Good candidates
- job analysis after admin uploads job
- parsed resume after upload
- cached roast result for same resume + target role
- saved interview session results

## 6.3 Separate Cheap vs Expensive Inference
Not every request needs the strongest model.

### Cheap tasks
- keyword extraction
- basic parsing
- lightweight matching
- formatting checks

### Expensive tasks
- bullet rewriting
- detailed role-aware roast
- mock answer evaluation
- personalized fit plans

## 7. System Flows

## 7.1 Job Upload + Enrichment
1. Admin creates or pastes job
2. Job is stored in `jobs`
3. `analyze-job` function runs
4. Structured output stored in `job_analysis`
5. Job becomes visible to users

## 7.2 Resume Upload + Roast
1. User uploads resume file
2. File stored in storage bucket
3. `parse-resume` function extracts structured sections
4. Resume record updated
5. User triggers `roast-resume`
6. Roast result saved in `resume_roasts`
7. User sees scores and suggestions

## 7.3 Resume Generation
1. User selects target role or target job
2. Source data is assembled from profile, resume, and job
3. `generate-resume` function returns resume JSON
4. Resume JSON saved
5. `export-resume-pdf` creates or renders PDF
6. PDF URL stored in `generated_resumes`

## 7.4 Mock Interview Flow
1. User starts session
2. `generate-mock-session` creates structured questions
3. Questions saved
4. User submits answers
5. `evaluate-answer` scores each answer
6. Results saved in `mock_answers`
7. Session summary computed

## 8. Suggested Deployment Layout

## 8.1 Android App
Single Android app codebase:
- app module
- feature modules
- core shared modules

## 8.2 Supabase Project
Use one Supabase project for MVP.

Folders:
```text
supabase/
├── functions/
│   ├── analyze-job/
│   ├── parse-resume/
│   ├── roast-resume/
│   ├── generate-resume/
│   ├── export-resume-pdf/
│   ├── generate-mock-session/
│   ├── evaluate-answer/
│   └── personalized-fit-check/
├── migrations/
└── seed/
```

## 8.3 Admin Web
Separate repo or folder:
```text
admin-web/
├── pages or app/
├── components/
├── services/
└── auth/
```

## 9. Access Control Model

## 9.1 Public-like reads
Authenticated users may read:
- active jobs
- their related job analysis
- their own saved records

## 9.2 Private reads/writes
A user may only access:
- their profile
- their resumes
- their roast history
- their generated resumes
- their mock sessions
- their answers
- their application tracker records

## 9.3 Admin-only operations
Only admins may:
- create jobs
- edit jobs
- publish/unpublish jobs
- mark featured jobs
- trigger enrichment for jobs in admin workflow

## 10. Caching Strategy

## 10.1 Client Caching
Use Room for:
- jobs list
- job details
- saved jobs snapshot
- recent dashboard summaries

## 10.2 Server Caching
Reuse stored outputs instead of recomputing:
- job analysis
- parsed resume
- roast results if the same input set repeats

## 10.3 Invalidation Triggers
Recompute when:
- job description changes
- resume file changes
- user requests a new roast for a different target job
- rubric or scoring logic version changes

Add `version` fields to structured AI outputs later if needed.

## 11. Failure Handling Strategy

AI workflows will fail sometimes. Design for it.

### Principles
- save partial progress
- show recoverable errors
- retry idempotent steps safely
- keep file upload and analysis decoupled
- avoid losing user input

### Example
Resume upload can succeed even if roast generation fails. The user should be able to retry roast without re-uploading.

## 12. Observability

Track these:
- function latency
- AI call latency
- error rate by function
- user flow conversion
- file processing failures
- export failures

Basic observability for MVP can be:
- logs in edge functions
- error tracking
- database audit-style tables later if needed

## 13. Readiness Computation Architecture

Readiness can be:
- computed on the fly in the app for MVP, or
- returned from a backend endpoint if you want consistency

Recommended for MVP:
- compute from latest stored values in backend query or lightweight function
- keep formula centralized so all platforms match later

## 14. Future Architecture Extensions

Not needed now, but easy later:
- web app for students
- voice interview pipeline
- background jobs for heavy document processing
- vector search for role similarity
- recommendation engine for roles and projects
- multi-model routing

## 15. Final Architecture Principle

Keep the architecture boring in the right places:
- standard Android client
- Supabase as backend
- Edge Functions for AI
- structured JSON everywhere

That will make the product easier to ship and easier to maintain.
