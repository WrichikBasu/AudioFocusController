### Welcome!

It is important to control audio focus in your app if you want to play any sound (media, voice call, alarm, etc.) It would not be a pleasant situation to the user if two or more apps simultaneously play different sounds. The system of granting and abandoning audio focus makes sure that no two apps play their sounds at once. Therefore, before playing any sound, you must request the system for audio focus. This library makes it easy for you to request audio focus, thereby reducing the number of lines you have to write.

## Complete documentation:
See here: [https://wrichikbasu.github.io/AudioFocusController/javadoc/index.html](https://wrichikbasu.github.io/AudioFocusController/javadoc/index.html)

## Quick Setup Guide:

### I. Importing your project using Gradle in Android Studio:

**Step 1:** Add the following to your root (project's) `build.gradle` file, at the end of all other repositories:

```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

**Step 2:** Next, add the following dependency:

```
dependencies {
  implementation 'com.github.WrichikBasu:AudioFocusController:v2.1'
}
```

**Step 3:** Sync your project with Gradle files.


### II. Create an `AudioFocusController` object:

**Step 1:** First, implement the `AudioFocusController.OnAudioFocusChangeListener` interface. Through this interface, the library will inform you when you should start or stop playback, or when you should duck the volume.

You can either implement the interface in your Activity or Service:

```java
public class MainActivity extends AppCompatActivity implements AudioFocusController.OnAudioFocusChangeListener, ... { ... }
```

or, you can create a variable and pass that instance later:

```java
AudioFocusController.OnAudioFocusChangeListener listener = new AudioFocusController.OnAudioFocusChangeListener() {

  @Override
  public void decreaseVolume() {
    ...
  }

  @Override
  public void increaseVolume() {
    ...
  }

  @Override
  public void pause() {
    ...
  }

  @Override
  public void resume() {
    ...
  }
}
```

**Step 2:** Create an instance of the `AudioFocusController` class using the `AudioFocusController.Builder` class:

```java
audioFocusController = new AudioFocusController.Builder(context) // Context must be passed
            .setAudioFocusChangeListener(this) // Pass the listener instance created above
            .setAcceptsDelayedFocus(true) // Indicate whether you will accept delayed focus
            .setPauseWhenAudioIsNoisy(false) // Indicate whether you want to be paused when audio becomes noisy
            .setPauseWhenDucked(false) // Indicate whether you want to be paused instead of ducking
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC) // Set the content type
            .setDurationHint(AudioManager.AUDIOFOCUS_GAIN) // Set the duration hint
            .setUsage(AudioAttributes.USAGE_MEDIA) // Set the usage
            .setStream(AudioManager.STREAM_MUSIC) // Set the stream
            .build();
```

### III. Request focus, and relax while the library manages your app:

1. You do not need to know whether you have focus.
1. Every time you want to play a sound, ask for focus using the `audioFocusController.requestFocus()` method.
1. If focus is granted, the `resume()` method will be called.
1. If focus is lost suddenly, then the `pause()` method will be called, and you have to stop the sound immediately. Do **NOT** abandon focus, because the library will do that for you if required. If the system restores focus, again the `resume()` method will be called.
1. Every time you pause, call `audioFocusController.abandonFocus()` so that focus is freed up for other apps.
1. If you have chosen *not* to pause when ducked, then the `decreaseVolume()` method will be called when the system asks us to duck. Once the ducking period is over, the `increaseVolume()` method will be called.
1. If you have chosen to be paused instead of ducking, the `pause()` method will be called when the system asks us to duck. Later, the `resume()` method will be called.

#### That's it!

