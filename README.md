# Smart Pantry Manager
A Java Android application for managing pantry ingredients and suggesting recipes that can be made using **every required ingredient in sufficient quantity**.

## Technology
- Android Studio
- Java
- SQLite via SQLiteOpenHelper
- XML layouts
- RecyclerView + custom adapters

## Database
SQLite was selected because the application is local, does not require cloud synchronisation, and the assignment explicitly permits SQLite. Pantry data persists on-device after the application is closed.

## Features
- Pantry CRUD: create, read, update and delete
- Optional expiry date
- 20 seeded recipes on first run
- Strict recipe matching
- Singular/plural normalization
- g/kg and ml/L conversion
- Recipe details
- Settings screen
- Input validation
- Intent-based navigation

## Setup
1. Open this folder in Android Studio.
2. Allow Gradle to sync.
3. Connect an Android device or start an emulator.
4. Run the `app` configuration.
5. On first launch, SQLite creates the database and seeds the recipe collection.

## Important
The project intentionally does not use Google Maps, GPS, location services, payment processing, or external recipe APIs.
