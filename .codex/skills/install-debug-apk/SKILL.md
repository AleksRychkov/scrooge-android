---
name: install-debug-apk
description: Install and launch this Android project's debug APK on a connected Android device with ADB. Use whenever the user says "install debug", asks to install the debug APK, deploy or run the debug build on a device, or install the app from app/build/outputs/apk/debug.
---

# Install Debug APK

Install `app/build/outputs/apk/debug/app-debug.apk` with ADB.

## Workflow

1. Check that `app/build/outputs/apk/debug/app-debug.apk` exists. If missing, warn that the debug APK must be built first and stop; do not install another APK.
2. Run `adb devices -l` and consider only entries whose state is exactly `device`.
3. Handle the usable-device count:
   - Zero: warn that no connected, authorized Android device is available. Report `offline` or `unauthorized` entries when present and stop.
   - One: use that device serial and immediately run the install and launch commands without prompting the user.
   - More than one: show the serial and description for each usable device, ask the user which device to use, and stop until the user selects one. Never choose automatically.
4. Install with:

   ```sh
   adb -s <serial> install -r app/build/outputs/apk/debug/app-debug.apk
   ```

5. Continue only when installation exits successfully and prints `Success`. Launch the debug application on the same selected device with:

   ```sh
   adb -s <serial> shell monkey -p dev.aleksrychkov.scrooge.debug -c android.intent.category.LAUNCHER 1
   ```

6. Report installation and launch results separately. Claim full success only when both commands exit successfully; otherwise report the relevant ADB failure.

## Guardrails

- Always pass `-s <serial>`, including when only one device is connected.
- Prompt for device selection only when more than one usable device is connected.
- Use `-r` to replace the existing debug installation while preserving app data.
- Do not build the APK unless the user separately asks for a build.
- Do not uninstall the existing application, clear its data, or target an unauthorized/offline device.
