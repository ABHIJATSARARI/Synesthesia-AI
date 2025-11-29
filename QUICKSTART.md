# 🚀 Quick Start Guide

Get Synesthesia AI running on your Android device in under 5 minutes!

## Prerequisites Checklist

- [ ] Android Studio installed (Iguana 2023.2.1+)
- [ ] Android device with USB debugging enabled
- [ ] USB cable to connect device
- [ ] 10 minutes of free time

## Step-by-Step Installation

### 1️⃣ Quick Install (Pre-built APK)

**Fastest way to get started:**

1. Download `extras/app-debug.apk` from the repository
2. Enable "Install from Unknown Sources" on your Android device
3. Transfer and install the APK
4. Grant camera permissions
5. Start using immediately!

**OR Build from Source:**

```bash
git clone https://github.com/abhijatsarari/Synesthesia-AI.git
cd Synesthesia-AI
```

### 2️⃣ Open in Android Studio

1. Launch **Android Studio**
2. Click **"Open"**
3. Navigate to the cloned `Synesthesia-AI` folder
4. Click **"OK"**

### 3️⃣ Install Required Tools

When Android Studio opens, it may prompt you to install missing components. Click **"Install"** for:

- **Android SDK Platform 35**
- **NDK (Side by side)**
- **CMake**

Alternatively, manually install via `Tools → SDK Manager → SDK Tools`:
- ✅ NDK (Side by side)
- ✅ CMake
- ✅ Android SDK Build-Tools 35

### 4️⃣ Sync Gradle

Android Studio should automatically sync Gradle. If not:
1. Click **File → Sync Project with Gradle Files**
2. Wait for sync to complete (may take 2-5 minutes on first run)

### 5️⃣ Enable Developer Mode on Your Device

**For Android 8.0+:**
1. Go to **Settings → About Phone**
2. Tap **Build Number** 7 times
3. Enter your PIN/password
4. Go back to **Settings → Developer Options**
5. Enable **USB Debugging**

### 6️⃣ Connect Your Device

1. Connect your Android device via USB
2. On your device, authorize USB debugging (tap "Allow")
3. In Android Studio, verify your device appears in the device dropdown

### 7️⃣ Run the App

1. Click the green **Run** button (▶️) or press `Shift + F10`
2. Select your connected device
3. Wait for build to complete (~2-3 minutes first time)
4. The app will launch automatically

### 8️⃣ Grant Permissions

When the app opens:
1. Tap **"Allow"** for camera permissions
2. Point camera at different environments
3. Experience real-time sensory regulation!

---

## 🎥 Testing Different Environments

Try pointing your camera at:

| Environment | Expected Result |
|-------------|-----------------|
| **Nature/Sky/Clean Room** | 🌿 Serenity + Calm music |
| **Cluttered Desk/Traffic** | ⚡ High Stimulus + Brown noise |
| **Keyboard/Grid/Bricks** | 📐 Geometric Order + Techno |
| **Fire/Red Alerts** | ⚠️ Danger + Alarm sound |

---

## 🛠️ Troubleshooting

### "NDK not configured"
**Solution:** Install NDK via SDK Manager (see Step 3)

### "CMake not found"
**Solution:** Install CMake via SDK Manager (see Step 3)

### App crashes on launch
**Solution:** 
- Check Logcat in Android Studio (`View → Tool Windows → Logcat`)
- Ensure device is API 28+ (Android 8.0+)
- Try uninstalling and reinstalling

### Camera not working
**Solution:**
- Grant camera permissions manually: `Settings → Apps → Synesthesia AI → Permissions`
- Restart the app

### No sound playing
**Solution:**
- Check device volume
- Tap the 🔊 button to unmute
- Ensure audio files are present in `app/src/main/res/raw/`

### Build fails with "Unsupported class file major version"
**Solution:** Update Gradle JDK to Java 11+
- `File → Settings → Build, Execution, Deployment → Build Tools → Gradle`
- Set **Gradle JDK** to Java 11 or higher

---

## 📱 Building APK for Distribution

### Debug APK (for testing)
```bash
./gradlew assembleDebug
```
Output: `app/build/outputs/apk/debug/app-debug.apk`

### Release APK (for production)
1. Generate a keystore:
   ```bash
   keytool -genkey -v -keystore my-release-key.keystore -alias my-key-alias -keyalg RSA -keysize 2048 -validity 10000
   ```

2. Configure signing in `app/build.gradle.kts`:
   ```kotlin
   signingConfigs {
       create("release") {
           storeFile = file("../my-release-key.keystore")
           storePassword = "your-password"
           keyAlias = "my-key-alias"
           keyPassword = "your-password"
       }
   }
   ```

3. Build:
   ```bash
   ./gradlew assembleRelease
   ```
   Output: `app/build/outputs/apk/release/app-release.apk`

---

## 🧪 Running Tests

### Unit Tests
```bash
./gradlew test
```

### Instrumented Tests (requires connected device)
```bash
./gradlew connectedAndroidTest
```

---

## 📊 Checking Performance

1. Open **Android Studio Profiler**: `View → Tool Windows → Profiler`
2. Select your running app
3. Monitor:
   - **CPU:** Should be <15% during inference
   - **Memory:** Should stay ~100MB
   - **Network:** Should be 0 (fully offline)

---

## 🎨 Customizing the App

### Change Audio Files
1. Replace files in `app/src/main/res/raw/`
2. Keep filenames: `calm.mp3`, `brown_noise.mp3`, `techno.mp3`, `alarm.mp3`
3. Rebuild

### Adjust Confidence Threshold
In `MainActivity.kt`, line ~225:
```kotlin
if (score < 0.70f) {  // Change 0.70 to your preferred value
```

### Change Inference Rate
In `MainActivity.kt`, line ~173:
```kotlin
if (currentTime - lastInferenceTime > 300) {  // Change 300ms to your preference
```

---

## 🌐 Next Steps

- ⭐ **Star the repository** on GitHub
- 📖 Read the full [README.md](README.md)
- 🤝 Check out [CONTRIBUTING.md](CONTRIBUTING.md)
- 🐛 Report issues on [GitHub Issues](https://github.com/abhijatsarari/Synesthesia-AI/issues)

---

## 📞 Need Help?

- **Documentation:** Check the main [README.md](README.md)
- **Issues:** [Open an issue](https://github.com/abhijatsarari/Synesthesia-AI/issues)
- **Quick Install:** Use the pre-built APK in `extras/app-debug.apk`
- **Email:** your.email@example.com

---

**Happy Building! 🎉**

Made with ❤️ for the neurodivergent community
