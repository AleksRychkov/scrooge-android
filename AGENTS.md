# Repository Guidelines

## Project Structure & Module Organization

Scrooge is a Kotlin, Jetpack Compose, multi-module Android application. `app/` is the application entry point. Shared infrastructure lives under `core/` (database, entities, routing, UDF helpers, resources, and design system). Domain code is grouped in `feature/<domain>/{api,default,di}`; keep interfaces in `api`, implementations in `default`, and wiring in `di`. UI is split between reusable `presentation/component/` modules and route-level `presentation/screen/` modules. Convention plugins are maintained in `android-build-logic/`, dependency versions in `gradle/libs.versions.toml`, and Detekt configuration in `config/detekt/`.

Use the standard Android source layout: production Kotlin in `src/main/kotlin`, resources in `src/main/res`, local tests in `src/test/kotlin`, and instrumented tests in `src/androidTest/kotlin`.

## Build, Test, and Development Commands

- `./gradlew assembleDebug` builds the debug APK for all required modules.
- `./gradlew installDebug` installs the debug app on a connected emulator or device.
- `./gradlew test` runs all JVM unit tests with JUnit Platform.
- `./gradlew :presentation:component:calculator:test` runs one module's tests during focused development.
- `./gradlew connectedDebugAndroidTest` runs instrumented tests on a connected device, when present.
- `./gradlew detekt` performs static analysis and formatting checks; findings fail the build.

Use the checked-in Gradle wrapper rather than a system Gradle installation.

## Coding Style & Naming Conventions

Follow Kotlin conventions with four-space indentation and trailing commas in multiline declarations. Use `UpperCamelCase` for types and Compose functions, `lowerCamelCase` for functions and properties, and lowercase package names under `dev.aleksrychkov.scrooge`. Name implementations clearly, such as `DefaultSettingsComponent`, and keep non-public implementation details in an `internal/` package. Run Detekt before submitting; its configuration includes `detekt-formatting`.

## Testing Guidelines

Tests use JUnit 5, with MockK and `kotlinx-coroutines-test` available in relevant modules. Mirror the production package and name files/classes `*Test.kt`, for example `PostfixNotationTest.kt`. Name test methods `When <condition> Then <result>` and structure each body with `// Given`, `// When`, and `// Then` sections. Add unit tests for reducers, transformations, serializers, and domain logic. There is no configured coverage threshold; prioritize regression tests for changed behavior.

## Commit & Pull Request Guidelines

Recent commits use concise, imperative, lowercase subjects such as `fix IndexOutOfBoundsException` and `update filters on resume`. Keep each commit focused. Pull requests should explain the user-visible change, list affected modules, reference an issue when applicable, and include screenshots or recordings for Compose UI changes. Report the Gradle checks run and note any tests intentionally omitted.
