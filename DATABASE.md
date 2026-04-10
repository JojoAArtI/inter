# Database Schema — Internship Uncle

## 1. Design Principles

The schema should support:

- user accounts and onboarding
- curated jobs
- AI-enriched job analysis
- uploaded resumes
- structured roast outputs
- generated resume versions
- mock interview sessions
- answer feedback
- saved jobs and application tracking

The schema should also support future extension without a major rewrite.

## 2. Table Overview

Core tables:
- `profiles`
- `jobs`
- `job_analysis`
- `saved_jobs`
- `resumes`
- `resume_roasts`
- `generated_resumes`
- `mock_sessions`
- `mock_questions`
- `mock_answers`
- `application_tracker`

## 3. SQL Schema

## 3.1 Extensions

```sql
create extension if not exists pgcrypto;
```

## 3.2 Profiles

```sql
create table if not exists profiles (
  id uuid primary key references auth.users(id) on delete cascade,
  name text,
  email text,
  college text,
  degree text,
  graduation_year int,
  target_roles text[] default '{}',
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);
```

## 3.3 Jobs

```sql
create table if not exists jobs (
  id uuid primary key default gen_random_uuid(),
  title text not null,
  company text not null,
  location text,
  work_mode text,
  employment_type text,
  stipend text,
  apply_url text,
  deadline timestamptz,
  description_raw text,
  description_clean text,
  tags text[] default '{}',
  is_featured boolean not null default false,
  is_active boolean not null default true,
  created_by uuid,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);
```

## 3.4 Job Analysis

```sql
create table if not exists job_analysis (
  id uuid primary key default gen_random_uuid(),
  job_id uuid not null references jobs(id) on delete cascade,
  summary text,
  role_reality text,
  required_skills jsonb not null default '[]'::jsonb,
  preferred_skills jsonb not null default '[]'::jsonb,
  top_keywords jsonb not null default '[]'::jsonb,
  likely_interview_topics jsonb not null default '[]'::jsonb,
  difficulty text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  unique(job_id)
);
```

## 3.5 Saved Jobs

```sql
create table if not exists saved_jobs (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  job_id uuid not null references jobs(id) on delete cascade,
  status text not null default 'saved',
  created_at timestamptz not null default now(),
  unique(user_id, job_id)
);
```

## 3.6 Resumes

```sql
create table if not exists resumes (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  file_url text,
  file_name text,
  parsed_text text,
  parsed_sections jsonb not null default '{}'::jsonb,
  latest_score int,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);
```

## 3.7 Resume Roasts

```sql
create table if not exists resume_roasts (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  resume_id uuid not null references resumes(id) on delete cascade,
  target_job_id uuid references jobs(id) on delete set null,
  overall_score int,
  ats_score int,
  relevance_score int,
  clarity_score int,
  formatting_score int,
  roast_result jsonb not null default '{}'::jsonb,
  created_at timestamptz not null default now()
);
```

## 3.8 Generated Resumes

```sql
create table if not exists generated_resumes (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  source_resume_id uuid references resumes(id) on delete set null,
  target_job_id uuid references jobs(id) on delete set null,
  template_name text,
  resume_json jsonb not null default '{}'::jsonb,
  pdf_url text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);
```

## 3.9 Mock Sessions

```sql
create table if not exists mock_sessions (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  target_job_id uuid references jobs(id) on delete set null,
  role_name text,
  difficulty text,
  mode text,
  overall_score int,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);
```

## 3.10 Mock Questions

```sql
create table if not exists mock_questions (
  id uuid primary key default gen_random_uuid(),
  session_id uuid not null references mock_sessions(id) on delete cascade,
  question text not null,
  category text,
  sequence_no int not null,
  expected_points jsonb not null default '[]'::jsonb,
  created_at timestamptz not null default now()
);
```

## 3.11 Mock Answers

```sql
create table if not exists mock_answers (
  id uuid primary key default gen_random_uuid(),
  question_id uuid not null references mock_questions(id) on delete cascade,
  answer_text text,
  feedback jsonb not null default '{}'::jsonb,
  score int,
  improved_answer text,
  created_at timestamptz not null default now()
);
```

## 3.12 Application Tracker

```sql
create table if not exists application_tracker (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  job_id uuid not null references jobs(id) on delete cascade,
  stage text,
  applied_at timestamptz,
  notes text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);
```

## 4. Indexes

```sql
create index if not exists idx_jobs_is_active on jobs(is_active);
create index if not exists idx_jobs_is_featured on jobs(is_featured);
create index if not exists idx_jobs_deadline on jobs(deadline);
create index if not exists idx_saved_jobs_user_id on saved_jobs(user_id);
create index if not exists idx_resumes_user_id on resumes(user_id);
create index if not exists idx_resume_roasts_user_id on resume_roasts(user_id);
create index if not exists idx_mock_sessions_user_id on mock_sessions(user_id);
create index if not exists idx_application_tracker_user_id on application_tracker(user_id);
```

## 5. Storage Buckets

Recommended buckets:

### `resume-uploads`
- private
- original uploaded files

### `generated-resumes`
- private
- generated PDF exports

### `job-assets`
- public or controlled
- logos, banners, or related assets

## 6. Example JSON Shapes

## 6.1 `parsed_sections`
```json
{
  "basics": {
    "name": "John Doe",
    "email": "john@example.com"
  },
  "education": [
    {
      "school": "ABC University",
      "degree": "B.Tech CSE",
      "start": "2023",
      "end": "2027"
    }
  ],
  "skills": ["Kotlin", "Firebase", "SQL"],
  "projects": [
    {
      "name": "Campus Connect",
      "description": "Built an Android app for student collaboration"
    }
  ],
  "experience": []
}
```

## 6.2 `roast_result`
```json
{
  "issues": [
    {
      "section": "projects",
      "severity": "medium",
      "message": "Project bullets are too vague"
    }
  ],
  "missing_keywords": ["REST APIs", "Jetpack Compose"],
  "weak_bullets": [
    "Worked on app development"
  ],
  "rewritten_bullets": [
    "Built and shipped an Android app using Kotlin and Firebase, improving login success rate by 18%"
  ],
  "comments": [
    "Your skills section is broad but not targeted to the selected role."
  ]
}
```

## 6.3 `resume_json`
```json
{
  "basics": {},
  "education": [],
  "skills": [],
  "projects": [],
  "experience": [],
  "achievements": []
}
```

## 6.4 `feedback`
```json
{
  "strengths": ["Good structure"],
  "weaknesses": ["No concrete example"],
  "missing_points": ["Result metric", "Challenge context"],
  "follow_up": "What was the measurable outcome?"
}
```

## 7. Row Level Security Plan

Enable RLS on all app tables.

## 7.1 Profiles
User can read and update own profile.

## 7.2 Jobs and Job Analysis
Authenticated users can read active jobs and related analysis.

## 7.3 Saved Jobs
User can insert/select/delete only their own records.

## 7.4 Resumes, Roasts, Generated Resumes
User can manage only rows where `user_id = auth.uid()`.

## 7.5 Mock Sessions and Tracker
User can access only their own data.

## 8. Example RLS Policies

## 8.1 Profiles
```sql
alter table profiles enable row level security;

create policy "profiles_select_own"
on profiles for select
using (auth.uid() = id);

create policy "profiles_update_own"
on profiles for update
using (auth.uid() = id);

create policy "profiles_insert_own"
on profiles for insert
with check (auth.uid() = id);
```

## 8.2 Saved Jobs
```sql
alter table saved_jobs enable row level security;

create policy "saved_jobs_select_own"
on saved_jobs for select
using (auth.uid() = user_id);

create policy "saved_jobs_insert_own"
on saved_jobs for insert
with check (auth.uid() = user_id);

create policy "saved_jobs_delete_own"
on saved_jobs for delete
using (auth.uid() = user_id);
```

## 8.3 Resumes
```sql
alter table resumes enable row level security;

create policy "resumes_select_own"
on resumes for select
using (auth.uid() = user_id);

create policy "resumes_insert_own"
on resumes for insert
with check (auth.uid() = user_id);

create policy "resumes_update_own"
on resumes for update
using (auth.uid() = user_id);

create policy "resumes_delete_own"
on resumes for delete
using (auth.uid() = user_id);
```

## 9. Optional Future Tables

You do not need these in MVP, but they are easy extensions:
- `job_clicks`
- `daily_streaks`
- `readiness_snapshots`
- `learning_recommendations`
- `notifications`
- `admin_audit_logs`

## 10. Migration Strategy

Suggested migration order:
1. profiles
2. jobs
3. job_analysis
4. saved_jobs
5. resumes
6. resume_roasts
7. generated_resumes
8. mock_sessions
9. mock_questions
10. mock_answers
11. application_tracker
12. indexes
13. RLS policies

## 11. Schema Principles Summary

- Use JSONB for structured AI outputs
- Keep stable entities relational
- Use `user_id` consistently for ownership
- Avoid prematurely normalizing every AI detail
- Save outputs once and reuse them
