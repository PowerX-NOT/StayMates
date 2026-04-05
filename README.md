# StayMates

Student-focused **Rental Property & Roommate Finder** Android app.

## Features (current)

- Listings feed (search + create listing + details)
- Bottom navigation: Listings, Matches, Chat, Split, Profile
- Material 3 UI with **Dynamic Color (Material You / wallpaper-based)** on Android 12+
- Client-side roommate matching (computed in-app from profile data)

## Planned / in progress

- Verified listings
- Real-time chat
- Rent split calculator (advanced)

## Tech stack

- **Android**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Navigation**: `androidx.navigation:navigation-compose`
- **Backend (WIP)**: Supabase (PostgREST)

## Project structure (high level)

- `app/src/main/java/com/android/staymates/ui/`
  - `navigation/` – Compose navigation + bottom bar
  - `screens/` – Compose screens
- `app/src/main/java/com/android/staymates/data/`
  - `models/` – data models
  - `repositories/` – data access logic

## Setup

### Requirements

- Android Studio (latest stable recommended)
- Android SDK / Platform tools (`adb`)

### Build

```bash
./gradlew :app:assembleDebug
```

### Install on device/emulator

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### Local `.env` (required for build)

The app build reads Supabase settings from a root `.env` file and exposes them via `BuildConfig`.

1. Copy the template:

```bash
cp .env.example .env
```

2. Fill in values:

```bash
SUPABASE_URL=https://xxxx.supabase.co
SUPABASE_ANON_KEY=eyJ...
```

Notes:

- `.env` is gitignored and should stay local.
- If `.env` is missing (or variables are empty), Gradle will fail with a clear error.

### Matching implementation note

Matching is computed client-side by fetching:

- The current user profile from `profiles`
- A pool of other profiles from `profiles`

The app computes match scores locally and sorts descending. There is no dependency on a `matches` table.

## Contributing

- Create a feature branch
- Commit small, focused changes
- Open a PR

## License

TBD
