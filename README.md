# Tome Fix (NeoForge 1.21.1)
Fixes the infinite recursion between Quark's Ancient Tomes and Enchiridion
(StackOverflowError in EnchantmentHelper.getComponentType).

## Build without installing anything
1. Create a free GitHub repo, upload all these files (keep the folder layout, including `.github`).
2. Open the repo's Actions tab -> "build" -> wait for it to finish.
3. Download the `tomefix-jar` artifact, unzip, and put the .jar in your instance's `mods` folder.

## Build locally
Install JDK 21 and Gradle 8.10+, then run `gradle build`. The jar is in `build/libs/`.
After installing, re-enable Quark's Ancient Tomes (quark-common.toml) to test.
