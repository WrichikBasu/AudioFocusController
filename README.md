# AudioFocusController
<i>An Android library to help you manage Audio Focus in your app with minimal code.</i>

![GitHub All Releases](https://img.shields.io/github/downloads/WrichikBasu/AudioFocusController/total)
![Snyk Vulnerabilities for GitHub Repo](https://img.shields.io/snyk/vulnerabilities/github/WrichikBasu/AudioFocusController)
![GitHub license](https://img.shields.io/github/license/WrichikBasu/AudioFocusController)

![GitHub last commit](https://img.shields.io/github/last-commit/WrichikBasu/AudioFocusController)

[![GitHub release (latest by date)](https://img.shields.io/github/v/release/WrichikBasu/AudioFocusController)](https://github.com/WrichikBasu/AudioFocusController/releases/latest)
![GitHub Release Date](https://img.shields.io/github/release-date/WrichikBasu/AudioFocusController)

![Minimum SDK version](https://img.shields.io/badge/minimum%20sdk%20version-Lollipop%20(API%2021)-brightgreen)
![Target SDK version](https://img.shields.io/badge/target%20sdk%20version-R%20(API%2030)-brightgreen)

You will have to manage Audio Focus in your Android app if you want to play any type of sound. Be it an alarm tone, music or a voice call, you will have to first ask for focus from the system, and play the tone if and only if the system grants you focus. With the help of this library, you can reduce the number of lines that you have to write.

## Steps to use this library:
### 1. Import the library to your Android Studio project (Gradle):
  **1.** Add the following to your project's (root) `build.gradle` file at the end of all other repositories:
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

  **2.** Add the following dependency:
```
dependencies {
  implementation 'com.github.WrichikBasu:AudioFocusController:Tag'
}
```

  **3.** Sync your project with Gradle files.

### 2. Create an `AudioFocusController` object:

  **1.** Implement the `AudioFocusController.OnAudioFocusChangeListener` interface:

You can either implement this in your class,...

```java
public class MainActivity extends AppCompatActivity implements AudioFocusController.OnAudioFocusChangeListener, ... { ... }
```

... or create a variable:

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

  **2.** Use the `AudioFocusController.Builder` class to create an instance of the `AudioFocusController` class:

```java
audioFocusController = new AudioFocusController.Builder(context) // Context must be passed
            .setAudioFocusChangeListener(this) // or pass the listener variable created above
            .setAcceptsDelayedFocus(true)
            .setPauseWhenAudioIsNoisy(false)
            .setPauseWhenDucked(false)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setDurationHint(AudioManager.AUDIOFOCUS_GAIN)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setStream(AudioManager.STREAM_MUSIC)
            .build();
```

### 3. Handling audio focus:
  
1. We are almost done. **Every** time you want to start/resume playback, call `audioFocusController.requestFocus()`. If the system grants focus, the `OnAudioFocusChangeListener.resume()` method will be called. Note, that if the system grants delayed focus, or rejects the request, then nothing will happen. However, in case of a delayed request, the `resume()` method will be called once the system finally grants focus.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**Every** time you pause playback, call `audioFocusController.abandonFocus()` so that the focus is freed and other apps can use it.

2. If focus is lost suddenly, then the `OnAudioFocusChangeListener.pause()` method will be called, and you should stop playback immediately. Do **NOT** abandon focus, because the library will do it for you when necessary. Note that you do not need to know whether or not you have focus, because every time you want to start playback, you will request focus, as stated in step 3 above. If focus is granted, you will be notified via the `resume()` method.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;If focus is restored later by the system automatically, the `resume()` method will be called.

3. If you have passed `false` to the `AudioFocusController.Builder.setPauseWhenDucked(boolean)` method, the `OnAudioFocusChangeListener.decreaseVolume()` method will be called every time the system asks us to duck the volume. Once the ducking period is over, the `OnAudioFocusChangeListener.increaseVolume()` will be called.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;If you have passed `true`, then you will be paused (via `pause()`) every time the system asks us to duck, and later resumed (via `resume()`) automatically.

### That's it!

#### The following points are worth re-iterating:
1. Call `requestFocus()` every single time you want to start playback.
1. Call `abandonFocus()` every time you are paused by the user.
1. Start playback only when the `resume()` method is called.
1. Stop playback immediately if the `pause()` method is called.
