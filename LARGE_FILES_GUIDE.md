# Large TensorFlow Lite Library Files Guide

## Issue Resolved
The repository contained two large TensorFlow Lite static library files (`.a` files) that exceeded GitHub's 100MB file size limit:
- `app/src/main/cpp/tflite/android64/libtensorflow-lite.a` (129MB)
- `app/src/main/cpp/tflite/lib/android_arm64/libtensorflow-lite.a` (129MB)

These files have been removed from Git tracking and Git history.

## What Was Done
1. Created `.gitignore` to exclude these large library files
2. Removed files from Git tracking using `git rm --cached`
3. Cleaned Git history using `git-filter-repo` to remove all traces
4. Re-added the remote origin

## How to Get These Library Files

Since these files are needed for building the app but can't be stored in Git, you should:

### Option 1: Download from TensorFlow (Recommended)
Download the TensorFlow Lite Android AAR or build the static libraries yourself from the official TensorFlow repository.

### Option 2: Use Gradle to Download
Modify your `build.gradle.kts` to download TensorFlow Lite dependencies automatically:

```kotlin
dependencies {
    implementation("org.tensorflow:tensorflow-lite:2.14.0")
    implementation("org.tensorflow:tensorflow-lite-gpu:2.14.0")
}
```

### Option 3: Use Git LFS (Git Large File Storage)
For future large binary files, consider using Git LFS:

1. Install Git LFS: `brew install git-lfs`
2. Initialize: `git lfs install`
3. Track large files: `git lfs track "*.a"`
4. Add and commit: `git add .gitattributes && git commit -m "Add Git LFS tracking"`

### Option 4: External Storage
Store these files in:
- Cloud storage (Google Drive, Dropbox, AWS S3)
- A separate private repository
- Internal build server or artifact repository

## Pushing to GitHub
You can now push to GitHub using GitHub Desktop without any file size issues. The history has been cleaned and force push will be required:

**⚠️ Important:** Since we rewrote Git history, you'll need to:
1. In GitHub Desktop, it should detect the history rewrite
2. Push with force to update the remote repository
3. Other collaborators will need to re-clone the repository

## Notes
- The actual `.a` files still exist in your local project directory (not tracked by Git)
- Keep these files locally for building but don't commit them
- Update your build documentation to explain how to obtain these dependencies
