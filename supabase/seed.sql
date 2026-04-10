-- Seed data: curated internship listings and matching job analysis rows for local MVP testing.

insert into public.jobs (
  id,
  title,
  company,
  location,
  work_mode,
  employment_type,
  stipend,
  apply_url,
  deadline,
  description_raw,
  description_clean,
  tags,
  is_featured,
  is_active
)
values
  (
    '5a5f4f36-3b37-4c62-a746-9f64c5f0a101',
    'Android Engineering Intern',
    'Northstar Labs',
    'Bengaluru, India',
    'Hybrid',
    'Internship',
    'INR 35,000 / month',
    'https://example.com/jobs/android-engineering-intern',
    timestamptz '2026-05-20 18:00:00+00',
    'Build Android product features in Kotlin, collaborate with design and backend teams, ship polished UI, and learn how a production mobile stack is maintained.',
    'Work on a modern Kotlin Android app, own small product slices, and improve app quality, performance, and UX with guidance from senior engineers.',
    array['android', 'kotlin', 'jetpack compose', 'mobile'],
    true,
    true
  ),
  (
    'b67a4f74-8476-4de2-93c8-e8d7f3e4f202',
    'Data Analyst Intern',
    'MetricMint',
    'Remote',
    'Remote',
    'Internship',
    'INR 28,000 / month',
    'https://example.com/jobs/data-analyst-intern',
    timestamptz '2026-05-27 18:00:00+00',
    'Support dashboard reporting, clean raw business data, identify trends, and work with operations and product teams to answer real business questions.',
    'Turn messy operational data into clean reporting, build repeatable analyses, and present useful insights without hiding behind jargon.',
    array['data', 'sql', 'analytics', 'dashboards'],
    false,
    true
  ),
  (
    'c7153d43-51d0-46fc-909f-22da92c6c303',
    'Product Design Intern',
    'Pixel Harbor',
    'Mumbai, India',
    'On-site',
    'Internship',
    'INR 30,000 / month',
    'https://example.com/jobs/product-design-intern',
    timestamptz '2026-06-02 18:00:00+00',
    'Design product flows, create wireframes, partner with PMs and engineers, and contribute to a consumer mobile experience with strong usability standards.',
    'Shape real mobile product flows, turn rough ideas into usable screens, and justify design decisions with user thinking and clean execution.',
    array['product design', 'figma', 'ux', 'mobile'],
    false,
    true
  )
on conflict (id) do update
set
  title = excluded.title,
  company = excluded.company,
  location = excluded.location,
  work_mode = excluded.work_mode,
  employment_type = excluded.employment_type,
  stipend = excluded.stipend,
  apply_url = excluded.apply_url,
  deadline = excluded.deadline,
  description_raw = excluded.description_raw,
  description_clean = excluded.description_clean,
  tags = excluded.tags,
  is_featured = excluded.is_featured,
  is_active = excluded.is_active;

insert into public.job_analysis (
  id,
  job_id,
  summary,
  role_reality,
  required_skills,
  preferred_skills,
  top_keywords,
  likely_interview_topics,
  difficulty
)
values
  (
    '6e8db70a-3a99-4ec5-8e16-91f2f8011101',
    '5a5f4f36-3b37-4c62-a746-9f64c5f0a101',
    'A hands-on Android internship focused on shipping UI, integrating backend data, and learning production mobile development discipline.',
    'They do not want a tutorial-only Android learner. They want someone who can already build clean screens, reason about app state, and explain tradeoffs.',
    '["Kotlin", "Jetpack Compose", "Coroutines", "REST APIs"]'::jsonb,
    '["Room", "Testing", "Material 3", "Git"]'::jsonb,
    '["kotlin", "compose", "android", "coroutines", "api integration"]'::jsonb,
    '["state management", "networking", "architecture", "mobile projects"]'::jsonb,
    'medium'
  ),
  (
    'ddbc3b10-8d4c-43db-a234-d85b20e72202',
    'b67a4f74-8476-4de2-93c8-e8d7f3e4f202',
    'A business-facing analytics role where the intern cleans datasets, writes SQL, and turns numbers into useful decisions.',
    'This is not a spreadsheet babysitting job. They want someone who can ask better questions, structure messy data, and explain findings clearly.',
    '["SQL", "Spreadsheet fluency", "Data cleaning", "Basic statistics"]'::jsonb,
    '["Python", "BI dashboards", "A/B testing", "Presentation skills"]'::jsonb,
    '["sql", "dashboard", "reporting", "data quality", "insights"]'::jsonb,
    '["sql queries", "business metrics", "analysis process", "stakeholder communication"]'::jsonb,
    'easy'
  ),
  (
    'f8dbb21f-9bd6-44df-9c59-87b6f29f3303',
    'c7153d43-51d0-46fc-909f-22da92c6c303',
    'A mobile product design internship centered on user flows, interface craft, and cross-functional collaboration.',
    'They want more than pretty screens. They want someone who can defend layout decisions, simplify flows, and work fast with engineers.',
    '["Figma", "Wireframing", "Interaction design", "UX thinking"]'::jsonb,
    '["Prototype testing", "Design systems", "Mobile heuristics", "Visual polish"]'::jsonb,
    '["figma", "user flows", "mobile ux", "prototyping", "interaction design"]'::jsonb,
    '["portfolio walkthrough", "design decisions", "usability tradeoffs", "cross-functional collaboration"]'::jsonb,
    'medium'
  )
on conflict (job_id) do update
set
  summary = excluded.summary,
  role_reality = excluded.role_reality,
  required_skills = excluded.required_skills,
  preferred_skills = excluded.preferred_skills,
  top_keywords = excluded.top_keywords,
  likely_interview_topics = excluded.likely_interview_topics,
  difficulty = excluded.difficulty;
