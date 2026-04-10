-- Purpose: enable row level security and define MVP policies for authenticated job reads and user-owned data.

alter table public.profiles enable row level security;
alter table public.jobs enable row level security;
alter table public.job_analysis enable row level security;
alter table public.saved_jobs enable row level security;
alter table public.resumes enable row level security;
alter table public.resume_roasts enable row level security;
alter table public.generated_resumes enable row level security;
alter table public.mock_sessions enable row level security;
alter table public.mock_questions enable row level security;
alter table public.mock_answers enable row level security;
alter table public.application_tracker enable row level security;

alter table public.profiles force row level security;
alter table public.jobs force row level security;
alter table public.job_analysis force row level security;
alter table public.saved_jobs force row level security;
alter table public.resumes force row level security;
alter table public.resume_roasts force row level security;
alter table public.generated_resumes force row level security;
alter table public.mock_sessions force row level security;
alter table public.mock_questions force row level security;
alter table public.mock_answers force row level security;
alter table public.application_tracker force row level security;

create policy "profiles_select_own"
on public.profiles
for select
to authenticated
using (auth.uid() is not null and auth.uid() = id);

create policy "profiles_insert_own"
on public.profiles
for insert
to authenticated
with check (auth.uid() is not null and auth.uid() = id);

create policy "profiles_update_own"
on public.profiles
for update
to authenticated
using (auth.uid() is not null and auth.uid() = id)
with check (auth.uid() is not null and auth.uid() = id);

create policy "profiles_delete_own"
on public.profiles
for delete
to authenticated
using (auth.uid() is not null and auth.uid() = id);

create policy "jobs_select_active_for_authenticated"
on public.jobs
for select
to authenticated
using (is_active = true);

create policy "job_analysis_select_for_active_jobs"
on public.job_analysis
for select
to authenticated
using (
  exists (
    select 1
    from public.jobs
    where jobs.id = job_id
      and jobs.is_active = true
  )
);

create policy "saved_jobs_select_own"
on public.saved_jobs
for select
to authenticated
using (auth.uid() is not null and auth.uid() = user_id);

create policy "saved_jobs_insert_own"
on public.saved_jobs
for insert
to authenticated
with check (
  auth.uid() is not null
  and auth.uid() = user_id
  and exists (
    select 1
    from public.jobs
    where jobs.id = job_id
      and jobs.is_active = true
  )
);

create policy "saved_jobs_update_own"
on public.saved_jobs
for update
to authenticated
using (auth.uid() is not null and auth.uid() = user_id)
with check (
  auth.uid() is not null
  and auth.uid() = user_id
  and exists (
    select 1
    from public.jobs
    where jobs.id = job_id
      and jobs.is_active = true
  )
);

create policy "saved_jobs_delete_own"
on public.saved_jobs
for delete
to authenticated
using (auth.uid() is not null and auth.uid() = user_id);

create policy "resumes_select_own"
on public.resumes
for select
to authenticated
using (auth.uid() is not null and auth.uid() = user_id);

create policy "resumes_insert_own"
on public.resumes
for insert
to authenticated
with check (auth.uid() is not null and auth.uid() = user_id);

create policy "resumes_update_own"
on public.resumes
for update
to authenticated
using (auth.uid() is not null and auth.uid() = user_id)
with check (auth.uid() is not null and auth.uid() = user_id);

create policy "resumes_delete_own"
on public.resumes
for delete
to authenticated
using (auth.uid() is not null and auth.uid() = user_id);

create policy "resume_roasts_select_own"
on public.resume_roasts
for select
to authenticated
using (auth.uid() is not null and auth.uid() = user_id);

create policy "resume_roasts_delete_own"
on public.resume_roasts
for delete
to authenticated
using (auth.uid() is not null and auth.uid() = user_id);

create policy "generated_resumes_select_own"
on public.generated_resumes
for select
to authenticated
using (auth.uid() is not null and auth.uid() = user_id);

create policy "generated_resumes_insert_own"
on public.generated_resumes
for insert
to authenticated
with check (
  auth.uid() is not null
  and auth.uid() = user_id
  and (
    source_resume_id is null or exists (
      select 1
      from public.resumes
      where resumes.id = source_resume_id
        and resumes.user_id = auth.uid()
    )
  )
  and (
    target_job_id is null or exists (
      select 1
      from public.jobs
      where jobs.id = target_job_id
        and jobs.is_active = true
    )
  )
);

create policy "generated_resumes_update_own"
on public.generated_resumes
for update
to authenticated
using (auth.uid() is not null and auth.uid() = user_id)
with check (
  auth.uid() is not null
  and auth.uid() = user_id
  and (
    source_resume_id is null or exists (
      select 1
      from public.resumes
      where resumes.id = source_resume_id
        and resumes.user_id = auth.uid()
    )
  )
  and (
    target_job_id is null or exists (
      select 1
      from public.jobs
      where jobs.id = target_job_id
        and jobs.is_active = true
    )
  )
);

create policy "generated_resumes_delete_own"
on public.generated_resumes
for delete
to authenticated
using (auth.uid() is not null and auth.uid() = user_id);

create policy "mock_sessions_select_own"
on public.mock_sessions
for select
to authenticated
using (auth.uid() is not null and auth.uid() = user_id);

create policy "mock_sessions_insert_own"
on public.mock_sessions
for insert
to authenticated
with check (
  auth.uid() is not null
  and auth.uid() = user_id
  and (
    target_job_id is null or exists (
      select 1
      from public.jobs
      where jobs.id = target_job_id
        and jobs.is_active = true
    )
  )
);

create policy "mock_sessions_update_own"
on public.mock_sessions
for update
to authenticated
using (auth.uid() is not null and auth.uid() = user_id)
with check (
  auth.uid() is not null
  and auth.uid() = user_id
  and (
    target_job_id is null or exists (
      select 1
      from public.jobs
      where jobs.id = target_job_id
        and jobs.is_active = true
    )
  )
);

create policy "mock_sessions_delete_own"
on public.mock_sessions
for delete
to authenticated
using (auth.uid() is not null and auth.uid() = user_id);

create policy "mock_questions_select_for_owned_sessions"
on public.mock_questions
for select
to authenticated
using (
  exists (
    select 1
    from public.mock_sessions
    where mock_sessions.id = session_id
      and mock_sessions.user_id = auth.uid()
  )
);

create policy "mock_answers_select_for_owned_sessions"
on public.mock_answers
for select
to authenticated
using (
  exists (
    select 1
    from public.mock_questions
    join public.mock_sessions on mock_sessions.id = mock_questions.session_id
    where mock_questions.id = question_id
      and mock_sessions.user_id = auth.uid()
  )
);

create policy "mock_answers_insert_for_owned_sessions"
on public.mock_answers
for insert
to authenticated
with check (
  exists (
    select 1
    from public.mock_questions
    join public.mock_sessions on mock_sessions.id = mock_questions.session_id
    where mock_questions.id = question_id
      and mock_sessions.user_id = auth.uid()
  )
);

create policy "mock_answers_update_for_owned_sessions"
on public.mock_answers
for update
to authenticated
using (
  exists (
    select 1
    from public.mock_questions
    join public.mock_sessions on mock_sessions.id = mock_questions.session_id
    where mock_questions.id = question_id
      and mock_sessions.user_id = auth.uid()
  )
)
with check (
  exists (
    select 1
    from public.mock_questions
    join public.mock_sessions on mock_sessions.id = mock_questions.session_id
    where mock_questions.id = question_id
      and mock_sessions.user_id = auth.uid()
  )
);

create policy "mock_answers_delete_for_owned_sessions"
on public.mock_answers
for delete
to authenticated
using (
  exists (
    select 1
    from public.mock_questions
    join public.mock_sessions on mock_sessions.id = mock_questions.session_id
    where mock_questions.id = question_id
      and mock_sessions.user_id = auth.uid()
  )
);

create policy "application_tracker_select_own"
on public.application_tracker
for select
to authenticated
using (auth.uid() is not null and auth.uid() = user_id);

create policy "application_tracker_insert_own"
on public.application_tracker
for insert
to authenticated
with check (auth.uid() is not null and auth.uid() = user_id);

create policy "application_tracker_update_own"
on public.application_tracker
for update
to authenticated
using (auth.uid() is not null and auth.uid() = user_id)
with check (auth.uid() is not null and auth.uid() = user_id);

create policy "application_tracker_delete_own"
on public.application_tracker
for delete
to authenticated
using (auth.uid() is not null and auth.uid() = user_id);
