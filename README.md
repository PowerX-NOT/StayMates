# StayMates

Student-focused **Rental Property & Roommate Finder** Android app.

## Features (current)

- Listings feed (search + create listing + details)
- Bottom navigation: Listings, Matches, Chat, Split, Profile
- Material 3 UI with **Dynamic Color (Material You / wallpaper-based)** on Android 12+

## Planned / in progress

- Preference-based roommate matching
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

## Supabase configuration

This repo contains Supabase-related code (`SupabaseClient`, repositories, and serializable models).

Best practice is to **avoid committing secrets** (anon keys, service keys) into source control.

Recommended approach:

- Store `SUPABASE_URL` and `SUPABASE_ANON_KEY` in a local, untracked config (example: `.env`)
- Read them via `BuildConfig` / Gradle properties (so you can have different values per build type)

If you want, I can refactor `SupabaseClient` to read from `BuildConfig` instead of hardcoded values.

## Contributing

- Create a feature branch
- Commit small, focused changes
- Open a PR

## License

TBD
