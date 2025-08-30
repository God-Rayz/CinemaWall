#!/bin/bash

# GitHub Setup Script for Video Wallpaper App
# This script helps you set up your GitHub repository

echo "🚀 GitHub Repository Setup for Video Wallpaper App"
echo "=================================================="

# Check if git is initialized
if [ ! -d ".git" ]; then
    echo "❌ Git repository not initialized. Please run 'git init' first."
    exit 1
fi

echo ""
echo "📋 Steps to set up your GitHub repository:"
echo ""

echo "1️⃣  Go to GitHub.com and create a new repository:"
echo "   - Repository name: video-wallpaper-app"
echo "   - Description: Secure, open-source Android app for video wallpapers"
echo "   - Make it Public"
echo "   - Don't initialize with README (we already have one)"
echo ""

echo "2️⃣  After creating the repository, run these commands:"
echo ""

# Get current directory name for repository suggestion
REPO_NAME=$(basename "$PWD")
echo "   # Add the remote origin (replace YOUR_USERNAME with your GitHub username)"
echo "   git remote add origin https://github.com/YOUR_USERNAME/$REPO_NAME.git"
echo ""
echo "   # Verify the remote was added"
echo "   git remote -v"
echo ""
echo "   # Push your code to GitHub"
echo "   git branch -M main"
echo "   git push -u origin main"
echo ""

echo "3️⃣  Optional: Set up GitHub Pages"
echo "   - Go to repository Settings → Pages"
echo "   - Source: Deploy from a branch"
echo "   - Branch: main, folder: / (root)"
echo ""

echo "4️⃣  Add these badges to your README.md:"
echo ""
echo "   [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)"
echo "   [![Android](https://img.shields.io/badge/Android-API%2021%2B-brightgreen.svg)](https://developer.android.com/about/versions/android-5.0)"
echo "   [![Kotlin](https://img.shields.io/badge/Kotlin-1.9.20-blue.svg)](https://kotlinlang.org/)"
echo "   [![Gradle](https://img.shields.io/badge/Gradle-8.4.0-orange.svg)](https://gradle.org/)"
echo ""

echo "🎉 Your repository will be ready for the open-source community!"
echo ""
echo "💡 Pro tip: Check TESTING_AND_DEPLOYMENT.md for detailed testing instructions."
