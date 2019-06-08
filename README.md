# Bitcoin Games Android

[![Build Status](https://app.bitrise.io/app/a6dbce9119abac7f/status.svg?token=9XWsYnhhu6hulazGVdTkbw)](https://app.bitrise.io/app/a6dbce9119abac7f)

### Download Android SDK and get latest build tools
1. wget https://dl.google.com/android/repository/tools_r25.2.3-linux.zip
2. unzip tools_r25.2.3-linux.zip -d android
3. android/tools/android update sdk -u -a -t 3

### Create keystore
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-alias

### Build

./gradlew assembleRelease

### Align, sign and verify apk
android/build-tools/25.0.2/zipalign -v -p 4 my-app-unsigned.apk my-app-unsigned-aligned.apk

android/build-tools/25.0.2/apksigner sign --ks my-release-key.jks --out my-app-release.apk my-app-unsigned-aligned.apk

android/build-tools/25.0.2/apksigner verify -v my-app-release.apk

# Links
1. https://developer.android.com/studio/index.html#downloads
2. https://developer.android.com/studio/publish/app-signing.html#signing-manually
