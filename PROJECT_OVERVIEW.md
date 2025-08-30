# Video Wallpaper App - Project Overview

## Architecture Overview

This Android application follows the **MVVM (Model-View-ViewModel)** architecture pattern with clean separation of concerns and modern Android development practices.

### Project Structure

```
app/
├── src/main/
│   ├── java/com/videowallpaper/app/
│   │   ├── data/           # Data layer (models, preferences)
│   │   ├── service/        # Wallpaper service
│   │   ├── ui/            # UI components (activities, fragments)
│   │   └── viewmodel/     # ViewModels for UI logic
│   ├── res/               # Resources (layouts, drawables, values)
│   └── AndroidManifest.xml
├── build.gradle.kts       # App-level build configuration
├── proguard-rules.pro     # Code obfuscation rules
└── README.md             # Project documentation
```

## Key Components

### 1. MainActivity
- **Purpose**: Main entry point for user interaction
- **Features**: 
  - Video selection from gallery
  - Settings configuration
  - Wallpaper preview and application
- **UI**: Material Design with cards, switches, and sliders

### 2. VideoWallpaperService
- **Purpose**: Core service that runs the video wallpaper
- **Features**:
  - Extends `WallpaperService` for system integration
  - Uses ExoPlayer for efficient video playback
  - Handles surface lifecycle management
  - Applies user settings (loop, mute, speed)

### 3. MainViewModel
- **Purpose**: Manages UI state and business logic
- **Features**:
  - LiveData for reactive UI updates
  - Settings persistence
  - Video selection management

### 4. PreferencesManager
- **Purpose**: Handles app settings and video configuration
- **Features**:
  - SharedPreferences for data persistence
  - Video settings management
  - User preferences storage

## Technical Implementation

### Video Playback
- **ExoPlayer**: Modern, efficient video player from Google
- **Surface Management**: Proper lifecycle handling for wallpaper surfaces
- **Performance**: Optimized for continuous playback with minimal battery impact

### Permissions
- **READ_EXTERNAL_STORAGE**: Access to video files
- **SET_WALLPAPER**: Set wallpaper on device
- **WAKE_LOCK**: Prevent device sleep during video playback

### Security Features
- **No Internet Access**: All processing is local
- **Minimal Permissions**: Only necessary permissions requested
- **No Tracking**: No analytics or user data collection
- **Open Source**: Transparent code for community review

## User Experience

### Video Selection
1. User taps "Select Video" button
2. Permission request dialog (if needed)
3. Gallery picker opens
4. Selected video appears in preview card

### Settings Configuration
- **Loop Video**: Continuous playback
- **Mute Audio**: Silent wallpaper option
- **Playback Speed**: 0.25x to 2.0x range
- **Quality Settings**: Performance vs. quality trade-offs

### Wallpaper Application
1. User configures settings
2. Taps "Set as Wallpaper"
3. System wallpaper chooser opens
4. User selects "Video Wallpaper" option
5. Video becomes active wallpaper

## Performance Considerations

### Battery Optimization
- Efficient video decoding with ExoPlayer
- Surface lifecycle management
- Minimal CPU usage during playback

### Memory Management
- Proper resource cleanup
- Surface view recycling
- ExoPlayer lifecycle management

### Video Quality
- Adaptive quality based on device capabilities
- Efficient codec selection
- Optimized for wallpaper use case

## Compatibility

### Android Versions
- **Minimum**: API 21 (Android 5.0 Lollipop)
- **Target**: API 34 (Android 14)
- **Tested**: API 21-34

### Device Requirements
- **Hardware**: Touchscreen (optional)
- **Software**: Live wallpaper support
- **Storage**: Sufficient space for video files

## Development Setup

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK API 21-34
- Kotlin 1.9.10+
- Gradle 8.2+

### Build Process
1. Clone repository
2. Open in Android Studio
3. Sync Gradle dependencies
4. Build project
5. Run on device/emulator

### Dependencies
- **ExoPlayer**: Video playback
- **Material Components**: UI components
- **PermissionX**: Permission handling
- **Glide**: Image loading
- **Navigation**: Screen navigation

## Testing

### Manual Testing
- Video selection from gallery
- Settings persistence
- Wallpaper application
- Performance on various devices

### Automated Testing
- Unit tests for ViewModels
- Integration tests for services
- UI tests for activities

## Future Enhancements

### Planned Features
- Video effects and filters
- Multiple video playlists
- Scheduled wallpaper changes
- Cloud video support
- Advanced quality settings

### Performance Improvements
- Hardware acceleration
- Video preloading
- Adaptive bitrate streaming
- Background optimization

## Contributing

### Code Style
- Kotlin-first approach
- Material Design guidelines
- Clean architecture principles
- Comprehensive documentation

### Pull Request Process
1. Fork repository
2. Create feature branch
3. Implement changes
4. Add tests
5. Submit pull request

## License

This project is licensed under the MIT License - see LICENSE file for details.

## Support

For issues, questions, or contributions:
- Create GitHub issue
- Submit pull request
- Review documentation
- Check existing discussions

---

*This app provides a secure, open-source solution for video wallpapers with no tracking, ads, or unnecessary permissions.*
