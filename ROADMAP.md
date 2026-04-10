# Roadmap — Internship Uncle

## 1. Delivery Philosophy

Build the smallest version that proves this workflow:

1. user finds a relevant internship
2. user understands what it really wants
3. user improves their resume for it
4. user practices for it
5. user feels more ready to apply

That is the MVP.

## 2. Phase Breakdown

## Phase 1 — Foundation
Goal: create the core project skeleton and data model.

### Deliverables
- Android project setup
- navigation scaffold
- auth flow
- onboarding
- Supabase project
- database schema
- storage buckets
- initial admin panel scaffold

## Phase 2 — Curated Job Board
Goal: make job discovery usable.

### Deliverables
- jobs list
- filters
- job detail screen
- saved jobs
- admin job create/edit
- publish/unpublish flow

## Phase 3 — JD Analysis
Goal: make jobs valuable, not just listed.

### Deliverables
- `analyze-job` function
- `job_analysis` rendering
- role reality section
- keyword section
- interview topics section
- difficulty tag

## Phase 4 — Resume Roast
Goal: build the first compelling AI feature.

### Deliverables
- resume upload
- parse resume flow
- `parse-resume` function
- `roast-resume` function
- roast result screen
- missing keywords + rewritten bullets UI

## Phase 5 — Resume Builder
Goal: let users act on feedback.

### Deliverables
- resume builder forms
- generated resume JSON
- PDF export flow
- saved versions

## Phase 6 — Mock Interviews
Goal: create repeat engagement and role prep.

### Deliverables
- mock setup screen
- session generation
- answer submission
- answer evaluation
- session history
- summary scores

## Phase 7 — Dashboard and Polish
Goal: unify the product.

### Deliverables
- dashboard summary
- readiness score
- deadline reminders
- recent activity
- empty states
- loading states
- error handling polish

## 3. 6-Week MVP Plan

## Week 1
### Backend
- create Supabase project
- create initial schema
- enable auth
- create storage buckets

### Android
- scaffold Compose app
- setup Hilt
- setup navigation
- build login/signup
- build onboarding

### Admin
- create basic admin panel shell

## Week 2
### Backend
- jobs table ready
- seed sample jobs
- RLS for jobs and saved jobs

### Android
- jobs home screen
- job card UI
- job detail screen
- save/unsave jobs

### Admin
- add job form
- edit job form

## Week 3
### Backend
- implement `analyze-job`
- store `job_analysis`

### Android
- render analysis sections
- show keywords and interview topics
- add CTA buttons from job detail

### Goal
Jobs now feel intelligent instead of static.

## Week 4
### Backend
- implement `parse-resume`
- implement `roast-resume`

### Android
- upload resume flow
- roast result screen
- comments, scores, missing keywords, rewrite cards

### Goal
First strong AI hook is live.

## Week 5
### Backend
- implement `generate-resume`
- implement `export-resume-pdf`

### Android
- resume builder form
- resume preview
- export button
- generated versions list

### Goal
Users can take action on roast feedback.

## Week 6
### Backend
- implement `generate-mock-session`
- implement `evaluate-answer`
- create dashboard summary endpoint or query

### Android
- mock setup
- question flow
- answer feedback screen
- dashboard
- readiness score
- final polish

### Goal
Complete end-to-end workflow works.

## 4. Priority Rules

If you run out of time:
1. keep job board
2. keep JD analysis
3. keep resume roast
4. keep text-based mock interview
5. simplify resume builder if needed

The core product value is more important than feature completeness.

## 5. Quality Checklist Before MVP Launch

### Product
- jobs are curated and clean
- every screen has empty states
- every AI output is structured and understandable
- feedback feels specific, not generic
- flows connect back to target role

### Technical
- auth works reliably
- private data is protected
- storage uploads are stable
- retries work for failed AI actions
- database schema is clean

### UX
- no dead-end screens
- CTA buttons are obvious
- loading states are visible
- score screens are easy to read
- dashboard is not cluttered

## 6. Post-MVP Roadmap

## V2
- application tracker
- personalized fit check
- 7-day prep sprint
- better dashboard analytics
- more template options
- profile-based recommendations

## V3
- voice mock interviews
- role-specific learning plans
- smarter keyword matching
- recommendation engine for jobs
- web app for students

## 7. What to Avoid

Avoid spending too much time on:
- visual polish before core workflow works
- advanced admin analytics
- too many resume templates
- scraping too many job sources
- building social features early

## 8. Launch Strategy Suggestion

For first rollout:
- onboard a small student group
- manually curate strong jobs
- collect screenshots of useful roast outputs
- see which workflow drives repeat usage:
  - job detail to roast
  - roast to resume generation
  - job detail to mock interview

That will tell you what feature is really pulling users back.
