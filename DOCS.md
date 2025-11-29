# 📚 Project Documentation Index

Welcome to the Synesthesia AI documentation hub! This index will help you navigate all available documentation.

## 📖 Quick Navigation

### Getting Started
1. **[README.md](README.md)** - Main project overview and technical details
2. **[QUICKSTART.md](QUICKSTART.md)** - 5-minute setup guide
3. **[CONTRIBUTING.md](CONTRIBUTING.md)** - Contribution guidelines

### Legal & Licensing
- **[LICENSE](LICENSE)** - MIT License details

### Project Structure

```
Synesthesia-AI/
│
├── 📄 README.md                    # Project overview & documentation
├── 🚀 QUICKSTART.md                # Quick setup guide
├── 🤝 CONTRIBUTING.md              # Contribution guidelines
├── ⚖️ LICENSE                      # MIT License
├── 📚 DOCS.md                      # This file
│
├── .github/                        # GitHub configuration
│   ├── ISSUE_TEMPLATE/
│   │   ├── bug_report.yml         # Bug report template
│   │   └── feature_request.yml    # Feature request template
│   └── PULL_REQUEST_TEMPLATE.md   # PR template
│
├── app/                           # Android application
│   ├── src/main/
│   │   ├── cpp/                   # C++ Native code
│   │   │   ├── synesthesia-ai.cpp # JNI bridge
│   │   │   └── edge-impulse-sdk/  # ML inference engine
│   │   ├── java/                  # Kotlin source
│   │   │   └── com/example/test_camera/
│   │   │       ├── MainActivity.kt
│   │   │       ├── SplashActivity.kt
│   │   │       ├── IntroActivity.kt
│   │   │       └── StarfieldView.kt
│   │   ├── res/                   # Resources
│   │   │   ├── layout/            # XML layouts
│   │   │   ├── raw/               # Audio files
│   │   │   └── drawable/          # Images
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
│
├── extras/                        # Additional resources
│   ├── icon.png                   # App icon
│   ├── Screenshot...png           # Model performance screenshots
│   └── app-debug.apk             # Pre-built APK
│
└── gradle/                        # Gradle configuration
```

## 🎯 Documentation by Audience

### For Users
- [README.md](README.md) - Understand what Synesthesia AI does
- [QUICKSTART.md](QUICKSTART.md#-testing-different-environments) - Learn how to use the app

### For Developers
- [README.md](README.md#-technical-architecture) - Technical architecture overview
- [QUICKSTART.md](QUICKSTART.md) - Development setup
- [CONTRIBUTING.md](CONTRIBUTING.md) - How to contribute code

### For Researchers/Clinicians
- [README.md](README.md#-scientific-foundation) - Scientific basis
- [README.md](README.md#-performance--validation) - Validation results

### For Contributors
- [CONTRIBUTING.md](CONTRIBUTING.md) - Contribution process
- [.github/PULL_REQUEST_TEMPLATE.md](.github/PULL_REQUEST_TEMPLATE.md) - PR guidelines

## 🔧 Technical Documentation

### Architecture Overview

#### 1. Native Layer (C++)
**Location:** `app/src/main/cpp/`

The native layer handles computationally intensive tasks:
- **YUV to RGB conversion** - Color space transformation
- **Image preprocessing** - Center crop and resize to 96x96
- **TFLite inference** - Model execution via Edge Impulse SDK
- **Thread safety** - Mutex-protected buffers

**Key Files:**
- `synesthesia-ai.cpp` - Main JNI bridge
- `edge-impulse-sdk/` - ML inference engine
- `tflite-model/` - Trained model

#### 2. Application Layer (Kotlin)
**Location:** `app/src/main/java/com/example/test_camera/`

The application layer manages UI and user experience:
- **Camera management** - CameraX API integration
- **Audio engine** - MediaPlayer with cross-fading
- **Visual effects** - Starfield and color overlays
- **User interactions** - Button handlers and haptic feedback

**Key Classes:**
- `MainActivity.kt` - Main app controller (377 lines)
- `StarfieldView.kt` - Custom animated view
- `SplashActivity.kt` - App launch screen
- `IntroActivity.kt` - Onboarding flow

#### 3. Resource Layer
**Location:** `app/src/main/res/`

Contains all app resources:
- **Layouts** - XML UI definitions
- **Audio** - Therapeutic soundscapes (calm.mp3, brown_noise.mp3, techno.mp3, alarm.mp3)
- **Graphics** - Icons and drawables

## 📊 Performance Metrics

### Inference Performance
- **Latency:** 22ms average
- **Accuracy:** 92.5%
- **RAM:** 1.4 MB
- **ROM:** 2.8 MB

### Battery Impact
- **Camera:** ~10% per hour (standard for camera apps)
- **Inference:** <2% additional overhead
- **Audio:** ~3% per hour

## 🧪 Testing Strategy

### Unit Tests
- Model output parsing
- Audio state machine transitions
- Camera permission handling

### Integration Tests
- End-to-end inference pipeline
- Audio-visual synchronization
- Camera switching

### Manual Testing Checklist
- [ ] App launches without crashes
- [ ] Camera preview displays correctly
- [ ] All four classes are detected accurately
- [ ] Audio transitions are smooth
- [ ] Haptic feedback works
- [ ] Front/back camera switching works
- [ ] Mute/unmute functions correctly
- [ ] App works offline

## 🐛 Debugging Guide

### Common Issues

#### App Crashes on Launch
**Symptom:** Immediate crash after opening  
**Causes:**
- Missing native library
- Camera permissions not granted
- Incompatible Android version

**Solutions:**
1. Check Logcat for native library errors
2. Verify NDK is properly installed
3. Ensure device is Android 8.0+

#### Inference Not Working
**Symptom:** "Analyzing..." stays permanently  
**Causes:**
- Model file missing
- Poor lighting conditions
- Low confidence predictions

**Solutions:**
1. Check model files exist in `cpp/tflite-model/`
2. Improve lighting
3. Lower confidence threshold in code

#### Audio Not Playing
**Symptom:** No sound output  
**Causes:**
- Audio files missing
- Device volume is zero
- Sound is muted in app

**Solutions:**
1. Verify files in `res/raw/`
2. Check device volume
3. Tap unmute button

### Logging

Enable verbose logging in `MainActivity.kt`:
```kotlin
private val TAG = "SynesthesiaAI"
Log.d(TAG, "Your debug message")
```

View logs:
```bash
adb logcat | grep SynesthesiaAI
```

## 🔐 Security & Privacy

### Data Handling
- **No Data Storage:** Images are never saved to disk
- **No Network:** App functions 100% offline
- **No Analytics:** No user tracking or telemetry

### Permissions
- **Camera:** Required for environmental analysis
- **Vibrate:** Optional for haptic feedback

## 📈 Roadmap

### Version 1.1 (Q1 2026)
- [ ] Haptic patterns for each environment
- [ ] Custom soundscape uploads
- [ ] Sensitivity settings

### Version 1.2 (Q2 2026)
- [ ] WearOS companion app
- [ ] Daily usage reports
- [ ] Multiple language support

### Version 2.0 (Q3 2026)
- [ ] iOS port
- [ ] Cloud sync (optional)
- [ ] Clinical dashboard

## 🤝 Community

### Contributing
See [CONTRIBUTING.md](CONTRIBUTING.md) for detailed guidelines.

**Quick Links:**
- [Report a Bug](.github/ISSUE_TEMPLATE/bug_report.yml)
- [Request a Feature](.github/ISSUE_TEMPLATE/feature_request.yml)
- [Submit a PR](.github/PULL_REQUEST_TEMPLATE.md)

### Support
- **GitHub Issues:** Technical problems
- **Email:** General inquiries
- **Discussions:** Community Q&A

## 📝 Documentation Standards

When updating documentation:
1. Use clear, concise language
2. Include code examples where relevant
3. Add screenshots for UI changes
4. Update this index if adding new files
5. Keep README.md as the main entry point

## 🔗 External Resources

### Edge Impulse
- [Edge Impulse Docs](https://docs.edgeimpulse.com/)
- [Model Optimization Guide](https://docs.edgeimpulse.com/docs/edge-impulse-studio/model-optimization)

### Android Development
- [CameraX Documentation](https://developer.android.com/training/camerax)
- [NDK Guide](https://developer.android.com/ndk/guides)
- [Material Design Guidelines](https://material.io/design)

### Sensory Processing Research
- [SPD Foundation](https://www.spdfoundation.net/)
- [STAR Institute](https://www.spdstar.org/)

## 📞 Contact

- **Project Lead:** your.email@example.com
- **GitHub:** [@abhijatsarari](https://github.com/abhijatsarari)
- **Repository:** [Synesthesia-AI](https://github.com/abhijatsarari/Synesthesia-AI)
- **Download APK:** [extras/app-debug.apk](extras/app-debug.apk)
- **Edge Impulse Project:** [Link](YOUR_EDGE_IMPULSE_LINK)

---

Last Updated: November 29, 2025

**Need to add something to these docs?** See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines!
