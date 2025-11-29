# Contributing to Synesthesia AI

First off, thank you for considering contributing to Synesthesia AI! It's people like you that make this tool better for the neurodivergent community.

## Code of Conduct

This project and everyone participating in it is governed by our commitment to creating a welcoming and inclusive environment. We expect all contributors to:

- Use welcoming and inclusive language
- Be respectful of differing viewpoints and experiences
- Gracefully accept constructive criticism
- Focus on what is best for the community
- Show empathy towards other community members

## How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check the existing issues to avoid duplicates. When you create a bug report, include as many details as possible:

- **Use a clear and descriptive title**
- **Describe the exact steps to reproduce the problem**
- **Provide specific examples** to demonstrate the steps
- **Describe the behavior you observed** and what you expected
- **Include screenshots or recordings** if possible
- **Specify your Android version** and device model
- **Include logs** from Android Logcat if available

### Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion:

- **Use a clear and descriptive title**
- **Provide a detailed description** of the suggested enhancement
- **Explain why this enhancement would be useful** to the community
- **Include mockups or examples** if applicable

### Pull Requests

1. **Fork the repository** and create your branch from `main`
2. **Make your changes** following our coding standards
3. **Test your changes** on a real Android device
4. **Update documentation** if you're changing functionality
5. **Write clear commit messages**
6. **Submit a pull request**

## Development Setup

### Prerequisites

- Android Studio Iguana (2023.2.1) or later
- Android SDK API 28+
- NDK (Side by side)
- CMake 3.10+

### Building from Source

```bash
# Clone your fork
git clone https://github.com/YOUR_GITHUB_USERNAME/Synesthesia-AI.git
cd Synesthesia-AI

# Note: Main repository is at https://github.com/abhijatsarari/Synesthesia-AI

# Open in Android Studio
# Sync Gradle dependencies
# Connect an Android device
# Run the app
```

## Coding Standards

### Kotlin

- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add comments for complex logic
- Keep functions small and focused

### C++

- Follow [Google C++ Style Guide](https://google.github.io/styleguide/cppguide.html)
- Use C++17 features appropriately
- Document JNI functions thoroughly
- Ensure thread safety with proper synchronization

### Git Commit Messages

- Use the present tense ("Add feature" not "Added feature")
- Use the imperative mood ("Move cursor to..." not "Moves cursor to...")
- Limit the first line to 72 characters
- Reference issues and pull requests when relevant

Example:
```
Add haptic feedback for environment changes

- Implement vibration patterns for each environment type
- Add settings toggle for haptic feedback
- Update documentation

Fixes #123
```

## Project Structure

```
app/src/main/
├── cpp/                    # Native C++ code (Edge Impulse integration)
├── java/                   # Kotlin source files
│   └── com/example/test_camera/
│       ├── MainActivity.kt # Main app logic
│       └── StarfieldView.kt # Custom views
├── res/                    # Resources (layouts, drawables, audio)
└── AndroidManifest.xml
```

## Testing

- Test on multiple Android devices if possible
- Verify camera functionality on both front and back cameras
- Check audio transitions are smooth
- Ensure haptic feedback works correctly
- Test with camera permissions denied/granted
- Verify offline functionality

## Areas We Need Help

### High Priority
- [ ] iOS port using Swift/Metal
- [ ] Accessibility improvements (VoiceOver, TalkBack)
- [ ] Additional soundscape options
- [ ] Localization (translations)

### Medium Priority
- [ ] WearOS support
- [ ] Custom model training guide
- [ ] Performance optimization
- [ ] Battery usage optimization

### Documentation
- [ ] Video tutorials
- [ ] Clinical use case studies
- [ ] Developer API documentation
- [ ] User guide

## Community

- **GitHub Issues:** For bug reports and feature requests
- **Discussions:** For questions and community support
- **Email:** For security issues or private inquiries

## Recognition

Contributors will be:
- Listed in the README.md
- Credited in release notes
- Given a shoutout on social media (with permission)

## Questions?

Don't hesitate to reach out! Open an issue with the "question" label, and we'll be happy to help.

---

Thank you for helping make Synesthesia AI better for everyone! 🙏
