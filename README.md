# Internship Uncle

Internship Uncle is a curated internship preparation platform designed to help students go from **confused applicant** to **ready candidate**.

It combines five connected workflows in one product:

1. Discover internships
2. Analyze job descriptions in plain English
3. Build and improve resumes
4. Practice role-specific mock interviews
5. Track readiness and application progress

## Product Positioning

**Internship Uncle — Your brutally honest internship copilot**

The core product idea is simple:

> Pick the internship you want. Internship Uncle tells you how to get ready for it.

This product should not feel like five separate AI tools. Everything should revolve around a **Target Role / Target Internship**.

## Core Workflow

A user should be able to:

1. Find an internship in the app
2. Understand what the job actually wants
3. Check whether they are a fit
4. Tailor their resume to the role
5. Roast and improve their resume
6. Practice interview questions for that role
7. Apply with more confidence

That connected flow is what makes the product strong.

## Main Product Sections

- **Discover** — curated internship listings
- **Analyze** — JD breakdown, fit score, skill gaps, prep roadmap
- **Resume Lab** — resume maker, roast, keyword matching, export
- **Interview Prep** — mock interviews, answer scoring, follow-ups
- **Dashboard** — readiness score, progress, saved jobs, deadlines

## Recommended Tech Stack

### Android App
- Kotlin
- Jetpack Compose
- MVVM
- Hilt
- Coroutines + Flow
- Room for local caching

### Backend
- Supabase Auth
- Supabase Postgres
- Supabase Storage
- Supabase Edge Functions
- AI APIs called only from backend functions

### Admin
- Simple React or Next.js admin panel
- Job upload and enrichment tools
- Publishing and analytics controls

## Docs in this folder

- `PRD.md` — full product requirements document
- `ARCHITECTURE.md` — system and backend architecture
- `DATABASE.md` — schema design and SQL
- `API.md` — Edge Function and service contract design
- `ANDROID.md` — Kotlin app structure and screen breakdown
- `ROADMAP.md` — phased MVP build plan
- `PROMPTS.md` — exact Codex prompts to scaffold and build features

## Local Setup

The Android app reads Supabase config from the root `local.properties` file, which is ignored by git. Set:

- `SUPABASE_URL`
- `SUPABASE_PUBLISHABLE_KEY`

For Supabase Edge Functions, use `supabase/functions/.env.local` with:

- `SUPABASE_URL`
- `SUPABASE_SERVICE_ROLE_KEY` or `SUPABASE_SECRET_KEY`
- `GEMINI_API_KEY`
- `GEMINI_MODEL` if you want to override the default model

Use the publishable key in the Android app. Keep the service role key and Gemini key server-side only.

## Suggested Repo Structure

```text
internship-uncle/
├── app-android/
├── admin-web/
├── supabase/
│   ├── functions/
│   ├── migrations/
│   └── seed/
├── docs/
│   ├── README.md
│   ├── PRD.md
│   ├── ARCHITECTURE.md
│   ├── DATABASE.md
│   ├── API.md
│   ├── ANDROID.md
│   ├── ROADMAP.md
│   └── PROMPTS.md
└── assets/
```

## MVP Goal

Build the smallest version that proves the product can help a student:

- find a role
- understand it
- improve resume quality
- prepare for it
- feel more ready to apply

## Guiding Product Rules

1. Every important action should connect to a target job or target role.
2. AI outputs must be structured, not giant random paragraphs.
3. Curated jobs beat generic scraped volume for the MVP.
4. Brutally honest feedback is good, but it must still be actionable.
5. Do not overbuild early — focus on one sharp workflow.

## One-Line Summary

**Internship Uncle is a curated internship prep platform that helps students discover roles, understand what those roles actually mean, tailor their resume, get brutally honest feedback, and practice interviews based on the exact job they want.**
