-- Purpose: create the Internship Uncle MVP foundation schema, constraints, timestamp triggers, and indexes.

create extension if not exists pgcrypto with schema extensions;

create schema if not exists private;

create or replace function private.set_updated_at()
returns trigger
language plpgsql
as $$
begin
  new.updated_at = now();
  return new;
end;
$$;

create table if not exists public.profiles (
  id uuid primary key references auth.users(id) on delete cascade,
  name text,
  email text,
  college text,
  degree text,
  graduation_year int,
  target_roles text[] not null default '{}'::text[],
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  constraint profiles_graduation_year_check
    check (graduation_year is null or graduation_year between 2000 and 2100)
);

create table if not exists public.jobs (
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
  tags text[] not null default '{}'::text[],
  is_featured boolean not null default false,
  is_active boolean not null default true,
  created_by uuid references auth.users(id) on delete set null,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists public.job_analysis (
  id uuid primary key default gen_random_uuid(),
  job_id uuid not null references public.jobs(id) on delete cascade,
  summary text,
  role_reality text,
  required_skills jsonb not null default '[]'::jsonb,
  preferred_skills jsonb not null default '[]'::jsonb,
  top_keywords jsonb not null default '[]'::jsonb,
  likely_interview_topics jsonb not null default '[]'::jsonb,
  difficulty text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  constraint job_analysis_job_id_key unique (job_id),
  constraint job_analysis_difficulty_check
    check (difficulty is null or difficulty in ('easy', 'medium', 'hard'))
);

create table if not exists public.saved_jobs (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  job_id uuid not null references public.jobs(id) on delete cascade,
  status text not null default 'saved',
  created_at timestamptz not null default now(),
  constraint saved_jobs_user_job_key unique (user_id, job_id),
  constraint saved_jobs_status_check
    check (status in ('saved', 'applied', 'archived'))
);

create table if not exists public.resumes (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  file_url text,
  file_name text,
  parsed_text text,
  parsed_sections jsonb not null default '{}'::jsonb,
  latest_score int,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  constraint resumes_latest_score_check
    check (latest_score is null or latest_score between 0 and 100)
);

create table if not exists public.resume_roasts (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  resume_id uuid not null references public.resumes(id) on delete cascade,
  target_job_id uuid references public.jobs(id) on delete set null,
  overall_score int,
  ats_score int,
  relevance_score int,
  clarity_score int,
  formatting_score int,
  roast_result jsonb not null default '{}'::jsonb,
  created_at timestamptz not null default now(),
  constraint resume_roasts_overall_score_check
    check (overall_score is null or overall_score between 0 and 100),
  constraint resume_roasts_ats_score_check
    check (ats_score is null or ats_score between 0 and 100),
  constraint resume_roasts_relevance_score_check
    check (relevance_score is null or relevance_score between 0 and 100),
  constraint resume_roasts_clarity_score_check
    check (clarity_score is null or clarity_score between 0 and 100),
  constraint resume_roasts_formatting_score_check
    check (formatting_score is null or formatting_score between 0 and 100)
);

create table if not exists public.generated_resumes (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  source_resume_id uuid references public.resumes(id) on delete set null,
  target_job_id uuid references public.jobs(id) on delete set null,
  template_name text,
  resume_json jsonb not null default '{}'::jsonb,
  pdf_url text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists public.mock_sessions (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  target_job_id uuid references public.jobs(id) on delete set null,
  role_name text,
  difficulty text,
  mode text,
  overall_score int,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  constraint mock_sessions_difficulty_check
    check (difficulty is null or difficulty in ('easy', 'medium', 'hard')),
  constraint mock_sessions_mode_check
    check (mode is null or mode in ('quick', 'full', 'pressure', 'resume_crossfire')),
  constraint mock_sessions_overall_score_check
    check (overall_score is null or overall_score between 0 and 100)
);

create table if not exists public.mock_questions (
  id uuid primary key default gen_random_uuid(),
  session_id uuid not null references public.mock_sessions(id) on delete cascade,
  question text not null,
  category text,
  sequence_no int not null,
  expected_points jsonb not null default '[]'::jsonb,
  created_at timestamptz not null default now(),
  constraint mock_questions_sequence_no_check
    check (sequence_no > 0),
  constraint mock_questions_session_sequence_key unique (session_id, sequence_no)
);

create table if not exists public.mock_answers (
  id uuid primary key default gen_random_uuid(),
  question_id uuid not null references public.mock_questions(id) on delete cascade,
  answer_text text,
  feedback jsonb not null default '{}'::jsonb,
  score int,
  improved_answer text,
  created_at timestamptz not null default now(),
  constraint mock_answers_question_id_key unique (question_id),
  constraint mock_answers_score_check
    check (score is null or score between 0 and 100)
);

create table if not exists public.application_tracker (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  job_id uuid not null references public.jobs(id) on delete cascade,
  stage text,
  applied_at timestamptz,
  notes text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  constraint application_tracker_user_job_key unique (user_id, job_id),
  constraint application_tracker_stage_check
    check (
      stage is null or
      stage in ('interested', 'applied', 'assessment', 'interviewing', 'offer', 'rejected', 'withdrawn')
    )
);

drop trigger if exists profiles_set_updated_at on public.profiles;
create trigger profiles_set_updated_at
before update on public.profiles
for each row
execute function private.set_updated_at();

drop trigger if exists jobs_set_updated_at on public.jobs;
create trigger jobs_set_updated_at
before update on public.jobs
for each row
execute function private.set_updated_at();

drop trigger if exists job_analysis_set_updated_at on public.job_analysis;
create trigger job_analysis_set_updated_at
before update on public.job_analysis
for each row
execute function private.set_updated_at();

drop trigger if exists resumes_set_updated_at on public.resumes;
create trigger resumes_set_updated_at
before update on public.resumes
for each row
execute function private.set_updated_at();

drop trigger if exists generated_resumes_set_updated_at on public.generated_resumes;
create trigger generated_resumes_set_updated_at
before update on public.generated_resumes
for each row
execute function private.set_updated_at();

drop trigger if exists mock_sessions_set_updated_at on public.mock_sessions;
create trigger mock_sessions_set_updated_at
before update on public.mock_sessions
for each row
execute function private.set_updated_at();

drop trigger if exists application_tracker_set_updated_at on public.application_tracker;
create trigger application_tracker_set_updated_at
before update on public.application_tracker
for each row
execute function private.set_updated_at();

create index if not exists idx_jobs_active_created_at
  on public.jobs (created_at desc)
  where is_active = true;

create index if not exists idx_jobs_active_deadline
  on public.jobs (deadline)
  where is_active = true and deadline is not null;

create index if not exists idx_jobs_active_featured_created_at
  on public.jobs (is_featured, created_at desc)
  where is_active = true;

create index if not exists idx_jobs_tags_gin
  on public.jobs
  using gin (tags);

create index if not exists idx_saved_jobs_job_id
  on public.saved_jobs (job_id);

create index if not exists idx_saved_jobs_user_created_at
  on public.saved_jobs (user_id, created_at desc);

create index if not exists idx_resumes_user_created_at
  on public.resumes (user_id, created_at desc);

create index if not exists idx_resume_roasts_user_created_at
  on public.resume_roasts (user_id, created_at desc);

create index if not exists idx_resume_roasts_resume_id
  on public.resume_roasts (resume_id);

create index if not exists idx_resume_roasts_target_job_id
  on public.resume_roasts (target_job_id)
  where target_job_id is not null;

create index if not exists idx_generated_resumes_user_created_at
  on public.generated_resumes (user_id, created_at desc);

create index if not exists idx_generated_resumes_source_resume_id
  on public.generated_resumes (source_resume_id)
  where source_resume_id is not null;

create index if not exists idx_generated_resumes_target_job_id
  on public.generated_resumes (target_job_id)
  where target_job_id is not null;

create index if not exists idx_mock_sessions_user_created_at
  on public.mock_sessions (user_id, created_at desc);

create index if not exists idx_mock_sessions_target_job_id
  on public.mock_sessions (target_job_id)
  where target_job_id is not null;

create index if not exists idx_application_tracker_job_id
  on public.application_tracker (job_id);

create index if not exists idx_application_tracker_user_updated_at
  on public.application_tracker (user_id, updated_at desc);
