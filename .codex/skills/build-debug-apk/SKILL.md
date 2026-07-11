---
name: build-debug-apk
description: Build this Android project's debug APK with the checked-in Gradle wrapper. Use whenever the user asks to run a build, build the code, build the project, build the debug version, or build an APK.
---

# Build Debug APK

1. Run `./gradlew assembleDebug -q` from the repository root.
2. If the command succeeds, report that the debug build passed and identify the generated APK under `app/build/outputs/apk/debug/` when present.
3. If the command fails, report the relevant Gradle error and do not claim that the APK was built.

Use the exact command above unless the user explicitly requests another build variant or additional Gradle options.
