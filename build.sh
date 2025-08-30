#!/bin/bash

# Video Wallpaper App Build Script
# This script helps with common development tasks

echo "🎬 Video Wallpaper App Build Script"
echo "=================================="

# Function to show usage
show_usage() {
    echo "Usage: $0 [command]"
    echo ""
    echo "Commands:"
    echo "  clean     - Clean build files"
    echo "  build     - Build the app"
    echo "  install   - Install on connected device"
    echo "  test      - Run tests"
    echo "  lint      - Run linting"
    echo "  help      - Show this help message"
    echo ""
}

# Function to check if Android device is connected
check_device() {
    if ! adb devices | grep -q "device$"; then
        echo "❌ No Android device connected. Please connect a device and enable USB debugging."
        exit 1
    fi
    echo "✅ Android device connected"
}

# Function to clean build files
clean_build() {
    echo "🧹 Cleaning build files..."
    ./gradlew clean
    echo "✅ Clean completed"
}

# Function to build the app
build_app() {
    echo "🔨 Building app..."
    ./gradlew assembleDebug
    if [ $? -eq 0 ]; then
        echo "✅ Build completed successfully"
    else
        echo "❌ Build failed"
        exit 1
    fi
}

# Function to install on device
install_app() {
    check_device
    echo "📱 Installing app on device..."
    ./gradlew installDebug
    if [ $? -eq 0 ]; then
        echo "✅ App installed successfully"
    else
        echo "❌ Installation failed"
        exit 1
    fi
}

# Function to run tests
run_tests() {
    echo "🧪 Running tests..."
    ./gradlew test
    if [ $? -eq 0 ]; then
        echo "✅ Tests completed successfully"
    else
        echo "❌ Tests failed"
        exit 1
    fi
}

# Function to run linting
run_lint() {
    echo "🔍 Running linting..."
    ./gradlew lintDebug
    if [ $? -eq 0 ]; then
        echo "✅ Linting completed successfully"
    else
        echo "⚠️  Linting found issues"
    fi
}

# Main script logic
case "$1" in
    clean)
        clean_build
        ;;
    build)
        build_app
        ;;
    install)
        install_app
        ;;
    test)
        run_tests
        ;;
    lint)
        run_lint
        ;;
    help|--help|-h)
        show_usage
        ;;
    "")
        echo "❌ No command specified"
        show_usage
        exit 1
        ;;
    *)
        echo "❌ Unknown command: $1"
        show_usage
        exit 1
        ;;
esac

echo ""
echo "🎉 Script completed!"
