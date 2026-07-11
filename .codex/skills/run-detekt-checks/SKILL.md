---
name: run-detekt-checks
description: Run this repository's Detekt validation workflow and report or resolve remaining findings. Use whenever the user asks to run Detekt, perform Detekt checks, check Kotlin lint or static analysis with Detekt, reproduce the pre-commit Detekt validation, or fix Detekt failures in this project.
---

# Run Detekt Checks

Follow the repository's `.githooks/pre-commit` behavior.

## Workflow

1. Inspect `git status --short` before running checks so existing user changes are known.
2. Run `./gradlew detekt --auto-correct` from the repository root. This pass may modify Kotlin files.
3. Run `./gradlew detekt --auto-correct` again. Capture and inspect its output and exit status; the second pass identifies violations that auto-correction did not resolve.
4. If the second pass succeeds, review `git status --short` and `git diff --check`. Report success and list any files Detekt changed.
5. If it fails, read the reported file locations and rules. Fix actionable findings only when the user requested fixes; otherwise report them concisely. Re-run the two-pass workflow after fixes.

## Guardrails

- Preserve unrelated working-tree changes and never revert user edits.
- Treat Detekt auto-corrections as expected project changes; inspect them before reporting completion.
- Run commands through the checked-in Gradle wrapper, not a system Gradle installation.
- If Gradle cannot access required dependencies because of sandbox or network restrictions, request the necessary approval and retry.
- Do not claim success unless the final Detekt invocation exits with status 0.
