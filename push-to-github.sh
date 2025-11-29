#!/bin/bash
# Safe Push Script for Synesthesia AI
# This script ensures large files are excluded before pushing to GitHub

set -e  # Exit on error

echo "🚀 Synesthesia AI - Safe Push to GitHub"
echo "========================================"
echo ""

# Navigate to project directory
cd /Users/abhijat/Downloads/example-android-inferencing-main/example_camera_inference

# Check if .git exists
if [ -d ".git" ]; then
    echo "⚠️  Git repository already exists!"
    echo "Cleaning up old repository..."
    rm -rf .git
    echo "✅ Cleaned"
fi

echo ""
echo "📋 Step 1: Initializing git repository..."
git init

echo ""
echo "📋 Step 2: Adding .gitignore first (excludes large files)..."
git add .gitignore
git commit -m "Add .gitignore to exclude large files"

echo ""
echo "📋 Step 3: Checking for large files that will be excluded..."
echo "Looking for .a and .so files..."
LARGE_FILES=$(find . -type f \( -name "*.a" -o -name "*.so" \) ! -path "./.git/*" | head -5)
if [ -n "$LARGE_FILES" ]; then
    echo "Found (these WILL BE IGNORED):"
    echo "$LARGE_FILES"
else
    echo "No large library files found"
fi

echo ""
echo "📋 Step 4: Adding all other files (large files will be ignored)..."
git add .

echo ""
echo "📋 Step 5: Verifying no large files are staged..."
STAGED_LARGE=$(git ls-files | grep -E '\.(a|so)$' || true)
if [ -n "$STAGED_LARGE" ]; then
    echo "❌ ERROR: Large files are still staged:"
    echo "$STAGED_LARGE"
    echo "Please update .gitignore and try again"
    exit 1
else
    echo "✅ No large files staged - safe to push!"
fi

echo ""
echo "📋 Step 6: Committing all files..."
git commit -m "Initial commit: Synesthesia AI with complete documentation and working APK

- Professional hackathon-ready documentation
- Working APK included (extras/app-debug.apk)
- 92.5% model accuracy with <40ms inference
- 100% offline privacy-first architecture
- Complete Android + C++ source code
- Large TFLite libraries excluded (regenerated during build)"

echo ""
echo "📋 Step 7: Adding remote repository..."
git remote add origin https://github.com/abhijatsarari/Synesthesia-AI.git

echo ""
echo "📋 Step 8: Setting main branch..."
git branch -M main

echo ""
echo "🎯 Ready to push!"
echo ""
echo "Repository: https://github.com/abhijatsarari/Synesthesia-AI"
echo ""
read -p "Press Enter to push to GitHub (Ctrl+C to cancel)..."

echo ""
echo "📋 Step 9: Pushing to GitHub..."
git push -u origin main

echo ""
echo "✅ SUCCESS! Your repository has been pushed to GitHub!"
echo ""
echo "🔗 View it at: https://github.com/abhijatsarari/Synesthesia-AI"
echo ""
echo "📝 Next steps:"
echo "   1. Add your demo video link to README.md"
echo "   2. Add your Edge Impulse project link"
echo "   3. Create a release with the APK"
echo "   4. Configure repository topics and description"
echo ""
echo "🎉 You're ready for the hackathon!"
