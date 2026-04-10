# PRD — Internship Uncle

## 1. Product Summary

Internship Uncle is a mobile-first internship preparation app for students. It combines:

- curated internship discovery
- job description analysis
- resume creation
- resume review and improvement
- interview practice
- readiness tracking

The product is designed around one central object:

**Target Internship / Target Role**

Everything meaningful in the app should be connected back to a specific role whenever possible.

## 2. Problem Statement

Students struggle with internships because the workflow is fragmented:

- Job boards show listings, but not what they actually mean.
- Resume builders create documents, but not strategy.
- AI interview apps ask generic questions.
- Resume review tools often give shallow advice.
- Students do not know whether they are actually ready to apply.

As a result, students:
- apply blindly
- use weak resumes
- prepare in the wrong direction
- waste time on low-fit jobs
- lose confidence

## 3. Product Vision

The product should feel like a brutally honest but helpful mentor that gives students real, practical guidance.

### Desired product feeling
- direct
- practical
- useful
- not corporate
- slightly savage, but never useless

### Positioning
**Internship Uncle — Your brutally honest internship copilot**

## 4. Goals

### Primary goals
- Help students discover better internship opportunities
- Improve resume quality for target roles
- Improve interview preparedness
- Increase confidence before application
- Keep users engaged in a repeatable prep loop

### Secondary goals
- Make job descriptions easier to understand
- Help users decide whether to apply
- Create a reusable workflow around internship prep
- Build a sticky product with repeated usage

## 5. Non-Goals for MVP

Do not focus on these in the first version:
- social feed
- recruiter marketplace
- voice interviews
- huge job scraping infrastructure
- community posting
- complex collaboration features
- dozens of resume templates

## 6. Target Users

### Primary users
- college students
- recent graduates
- students applying for their first internship
- students targeting tech, design, analytics, or similar roles

### Secondary users
- students switching domains
- final-year students looking for fresher roles
- students preparing for multiple internship tracks

## 7. Main User Jobs

Users come to the app to do one of these jobs:

1. Find internships worth applying to
2. Understand what a job description really means
3. Improve an existing resume
4. Build a new resume for a target role
5. Practice interview questions for a role
6. Track whether they are getting more ready

## 8. Main Product Sections

## 8.1 Discover
Purpose: help users find internships.

### Features
- curated job board
- internship cards with filters
- save job
- apply link
- deadline tracking
- featured roles
- role tags

### Job card fields
- title
- company
- location
- work mode
- stipend
- internship type
- tags
- deadline
- featured badge
- fit badge later

## 8.2 Analyze
Purpose: decode the role before applying.

### Features
- plain-English JD summary
- what the intern will likely do
- hidden expectations
- likely interview topics
- top keywords
- skill gap analysis
- fit classification: Safe / Match / Reach
- prep roadmap

## 8.3 Resume Lab
Purpose: create and improve role-targeted resumes.

### Features
- upload resume
- parse resume
- roast resume
- recruiter review mode
- rewrite bullets
- generate targeted resume
- export PDF
- save resume versions

## 8.4 Interview Prep
Purpose: simulate realistic internship interview preparation.

### Features
- mock interviews by role
- role-specific questions
- technical + HR + behavioral rounds
- answer scoring
- improved answer suggestions
- follow-up questioning
- session history

## 8.5 Dashboard
Purpose: show progress and readiness.

### Features
- readiness score
- recent scores
- saved jobs
- next deadlines
- recent activity
- interview trend
- resume trend

## 9. Core Product Flows

## 9.1 Discover → Analyze → Resume → Interview → Apply
This is the core flow.

1. User opens job details
2. Reads the JD reality check
3. Sees fit score and missing skills
4. Uploads or generates resume for that role
5. Gets a roast tailored to that role
6. Practices mock interview for that role
7. Applies

This is the strongest workflow in the product.

## 9.2 Resume Roast Flow
1. User uploads resume
2. User optionally selects target job
3. Resume is parsed
4. Resume is scored across multiple dimensions
5. User sees savage roast + actionable fixes
6. User rewrites weak bullets
7. User can generate improved resume version

## 9.3 Resume Maker Flow
1. User enters or imports data
2. User optionally selects target role/job
3. AI suggests what to highlight
4. Resume JSON is generated
5. User previews template
6. User exports PDF

## 9.4 Mock Interview Flow
1. User selects role or job
2. User selects difficulty and mode
3. App generates question set
4. User answers one by one
5. AI scores answers
6. App shows stronger answer and follow-up
7. App stores session history

## 10. Signature Features

## 10.1 JD Reality Check
A plain-English explanation of what the job is actually asking for.

### Outputs
- summary
- role reality
- required skills
- preferred skills
- top keywords
- likely interview topics
- difficulty estimate

## 10.2 Resume Roast
A structured, role-aware resume review.

### Score categories
- ATS friendliness
- relevance to target job
- clarity of impact
- formatting
- technical depth

### Modes
- Savage Roast
- Recruiter Mode

## 10.3 Resume vs JD Match
Show:
- matched keywords
- missing keywords
- weak sections
- strongest matching experiences
- what to fix before applying

## 10.4 Mock Interview
Should support:
- quick practice
- full mock
- pressure round
- resume-based crossfire

## 10.5 Uncle Verdict
For each job, give a plain recommendation:
- Apply now
- Improve and apply
- Probably skip

## 11. Engagement and Retention

### Retention hooks
- readiness score
- weekly prep streak
- saved internships
- progress history
- “3 fixes before applying”
- “7-day internship sprint”

### Useful, not gimmicky
Gamification should always support action:
- improve a bullet
- revise a keyword gap
- complete a mock session
- raise readiness score

## 12. MVP Scope

The MVP includes:

1. auth
2. profile onboarding
3. job board
4. job detail + JD analysis
5. save jobs
6. resume upload + roast
7. resume builder + export
8. text-based mock interview
9. dashboard with simple readiness view

## 13. User Stories

### Discover
- As a student, I want to browse internships by role and location so I can find opportunities relevant to me.
- As a student, I want to save jobs so I can return to them later.
- As a student, I want to see deadlines so I do not miss opportunities.

### Analyze
- As a student, I want a plain-English explanation of the JD so I can understand whether it fits me.
- As a student, I want to see likely interview topics so I know what to prepare.

### Resume Lab
- As a student, I want to upload my resume and get actionable feedback so I can improve it.
- As a student, I want my resume roast to be tailored to a selected job.
- As a student, I want to generate a resume for a specific role.

### Interview Prep
- As a student, I want role-specific questions so I can practice for the exact type of internship I want.
- As a student, I want feedback on my answers so I know how to improve.

### Dashboard
- As a student, I want a readiness score so I know whether I should apply now or keep preparing.

## 14. Success Metrics

### Product metrics
- number of saved jobs per active user
- number of resume roasts per active user
- number of mock interview sessions per active user
- repeat usage within 7 days
- percentage of jobs opened that lead to another action

### Workflow metrics
- job detail → resume roast conversion
- roast → resume generation conversion
- job detail → mock interview conversion
- saved jobs → apply link click conversion

### Quality metrics
- user-rated usefulness of roast
- user-rated usefulness of interview feedback
- resume export rate
- session completion rate

## 15. Risks

### Product risks
- features feel disconnected
- resume roast becomes too generic
- users do not trust job quality
- interview feedback feels repetitive
- app becomes bloated too early

### Mitigations
- center every feature around target job
- curate jobs carefully
- use structured AI outputs
- keep feedback specific and role-aware
- avoid adding too many side features early

## 16. Final Product Principle

This product wins if it feels like this:

> I found a role I want.  
> Internship Uncle helped me understand it, improve my resume for it, practice for it, and feel ready to apply.

That should be the standard for every feature decision.
