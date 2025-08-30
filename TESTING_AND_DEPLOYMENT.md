# 🧪 Testing & Deployment Guide

## 🚀 **Testing Your Video Wallpaper App**

### **Option 1: Android Studio (Recommended)**

1. **Open Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to your `wallpaper` folder and select it

2. **Sync Project**
   - Android Studio will automatically detect it's an Android project
   - Click "Sync Project with Gradle Files" if prompted
   - Wait for the sync to complete

3. **Build the Project**
   - Go to Build → Make Project (or press Ctrl+F9 / Cmd+F9)
   - Check the Build tab for any errors

4. **Run on Device/Emulator**
   - Connect an Android device via USB (enable USB debugging)
   - Or create an Android Virtual Device (AVD)
   - Click the green "Run" button (▶️)

### **Option 2: Command Line Testing**

```bash
# Navigate to your project directory
cd /path/to/wallpaper

# Clean build files
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug

# Run tests
./gradlew test

# Run linting
./gradlew lintDebug
```

### **Option 3: Manual Testing Checklist**

- [ ] **App Installation**
  - [ ] App installs successfully
  - [ ] App launches without crashes
  - [ ] All UI elements are visible

- [ ] **Permissions**
  - [ ] Storage permission request works
  - [ ] Permission denial is handled gracefully
  - [ ] Permission can be granted from settings

- [ ] **Video Selection**
  - [ ] Gallery picker opens
  - [ ] Video files are selectable
  - [ ] Selected video appears in preview

- [ ] **Video Preview**
  - [ ] Video plays in preview mode
  - [ ] Playback controls work
  - [ ] Settings are applied (loop, mute, speed)

- [ ] **Wallpaper Setting**
  - [ ] Wallpaper chooser opens
  - [ ] Video wallpaper service is available
  - [ ] Video plays as wallpaper

- [ ] **Settings Persistence**
  - [ ] Settings are saved between app launches
  - [ ] Video selection is remembered
  - [ ] Preferences are restored correctly

## 🌐 **Setting Up GitHub Repository**

### **Step 1: Create GitHub Repository**

1. **Go to GitHub.com** and sign in
2. **Click the "+" icon** in the top right
3. **Select "New repository"**
4. **Fill in repository details:**
   - Repository name: `video-wallpaper-app`
   - Description: `Secure, open-source Android app for video wallpapers`
   - Make it **Public** (for open source)
   - **Don't** initialize with README (we already have one)
   - Click "Create repository"

### **Step 2: Connect Local Repository to GitHub**

```bash
# Add the remote origin (replace YOUR_USERNAME with your GitHub username)
git remote add origin https://github.com/YOUR_USERNAME/video-wallpaper-app.git

# Verify the remote was added
git remote -v

# Push your code to GitHub
git branch -M main
git push -u origin main
```

### **Step 3: Set Up GitHub Pages (Optional)**

1. **Go to repository Settings**
2. **Scroll to "Pages" section**
3. **Source: Deploy from a branch**
4. **Branch: main, folder: / (root)**
5. **Click Save**

### **Step 4: Add Repository Badges**

Add these to your README.md:

```markdown
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Android](https://img.shields.io/badge/Android-API%2021%2B-brightgreen.svg)](https://developer.android.com/about/versions/android-5.0)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.20-blue.svg)](https://kotlinlang.org/)
[![Gradle](https://img.shields.io/badge/Gradle-8.4.0-orange.svg)](https://gradle.org/)
```

## 🔧 **Troubleshooting Common Issues**

### **Gradle Build Issues**

```bash
# Clear Gradle cache
./gradlew clean
rm -rf .gradle/
rm -rf build/

# Update Gradle wrapper
./gradlew wrapper --gradle-version 8.4

# Check Java version (requires Java 17+)
java -version
```

### **Android Studio Issues**

1. **Invalid Gradle JDK**
   - File → Project Structure → SDK Location
   - Set Gradle JDK to Java 17 or higher

2. **Sync Failed**
   - File → Invalidate Caches and Restart
   - Try File → Sync Project with Gradle Files

3. **Build Variants**
   - View → Tool Windows → Build Variants
   - Select "debug" variant

### **Device Connection Issues**

```bash
# Check if device is connected
adb devices

# Enable USB debugging on device
# Settings → Developer options → USB debugging

# Install ADB if not available
brew install android-platform-tools
```

## 📱 **Testing on Different Devices**

### **Physical Device Testing**
- **Android 5.0+ (API 21+)**
- **Enable Developer Options**
- **Enable USB Debugging**
- **Allow USB Debugging** when prompted

### **Emulator Testing**
1. **Create AVD in Android Studio**
2. **Use API 21+ (Android 5.0+)**
3. **Enable hardware acceleration**
4. **Test different screen sizes**

### **Test Scenarios**
- [ ] **Portrait and Landscape** orientations
- [ ] **Different screen densities** (hdpi, xhdpi, xxhdpi)
- [ ] **Various Android versions** (5.0, 6.0, 7.0, 8.0, 9.0, 10, 11, 12, 13, 14)
- [ ] **Low-end devices** (limited RAM/CPU)

## 🚀 **Continuous Integration (Optional)**

### **GitHub Actions Setup**

Create `.github/workflows/android.yml`:

```yaml
name: Android CI

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
    - name: Build with Gradle
      run: ./gradlew build
    - name: Run tests
      run: ./gradlew test
```

## 📋 **Release Checklist**

Before releasing your app:

- [ ] **Code Quality**
  - [ ] All tests pass
  - [ ] Linting shows no errors
  - [ ] Code follows style guidelines

- [ ] **Documentation**
  - [ ] README is complete
  - [ ] Code is well-commented
  - [ ] API documentation exists

- [ ] **Testing**
  - [ ] App works on multiple devices
  - [ ] All features are tested
  - [ ] Edge cases are handled

- [ ] **Security**
  - [ ] No hardcoded secrets
  - [ ] Permissions are minimal
  - [ ] No tracking code

- [ ] **Performance**
  - [ ] App launches quickly
  - [ ] Memory usage is reasonable
  - [ ] Battery impact is minimal

## 🎯 **Next Steps After Testing**

1. **Fix any issues** found during testing
2. **Optimize performance** based on test results
3. **Add more test cases** for edge scenarios
4. **Create release builds** for distribution
5. **Share with beta testers** for feedback
6. **Publish to Google Play Store** (optional)

---

**Happy Testing! 🎉**

Your video wallpaper app is now ready for comprehensive testing and can be easily shared on GitHub for the open-source community to contribute to and improve.
