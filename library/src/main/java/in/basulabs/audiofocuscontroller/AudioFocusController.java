package in.basulabs.audiofocuscontroller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * A class to help you manage audio focus in your app without writing too many lines of
 * code.
 * <p>
 * After you have imported this library to your project, first build an instance of this
 * class using the {@link Builder} class. You must set the context, the duration hint and
 * the {@link OnAudioFocusChangeListener} instance.
 * </p>
 * <p>
 * Once you have built the instance, you are ready to go. Every time before starting
 * playback, call {@link #requestFocus()}. If Android grants focus, you will receive a
 * callback in {@link OnAudioFocusChangeListener#resume()} method. If focus is lost,
 * whether for some time or permanently, the {@link OnAudioFocusChangeListener#pause()}
 * method will be called. If the system grants delayed focus, then
 * {@link OnAudioFocusChangeListener#resume()} method will be called when the system
 * finally grants focus. If focus request is denied, no method will be called.
 * </p>
 * <p>
 * Remember that whenever you start the playback, always call {@link #requestFocus()} and
 * wait for the {@link OnAudioFocusChangeListener#resume()} method to be called. When you
 * pause the playback, make sure you call {@link #abandonFocus()} so that the focus can be
 * freed for other apps.
 * </p>
 */
@Keep
public final class AudioFocusController {

	private final Context context;
	private final boolean pauseWhenDucked;
	private final boolean pauseWhenNoisy;
	private final int streamType;
	private final int durationHint;
	private final OnAudioFocusChangeListener myListener;
	private final AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;
	private final AudioManager audioManager;
	private final AudioFocusRequest audioFocusRequest;

	private boolean volumeDucked;

	private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Objects.equals(intent.getAction(),
				AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
				myListener.pause();
				abandonFocus();
			}
		}
	};

	public interface OnAudioFocusChangeListener {

		/**
		 * Duck the volume.
		 * <p>
		 * Will be called if and only if
		 * {@link Builder#setPauseWhenAudioIsNoisy(boolean)}
		 * is passed {@code true}.
		 * </p>
		 */
		void decreaseVolume();

		/**
		 * Revive the volume to what it was before ducking.
		 * <p>
		 * Will be called if and only if
		 * {@link Builder#setPauseWhenAudioIsNoisy(boolean)}
		 * is passed {@code true}.
		 * </p>
		 */
		void increaseVolume();

		/**
		 * Pause the playback.
		 */
		void pause();

		/**
		 * Resume/start the playback.
		 */
		void resume();

	}

	/**
	 * Builder class for {@link AudioFocusController} class objects.
	 */
	public static final class Builder {

		private final Context context;
		private int usage;
		private int contentType;
		private boolean acceptsDelayedFocus;
		private boolean pauseWhenDucked;
		private OnAudioFocusChangeListener myListener;
		private int stream;
		private int durationHint;
		private boolean pauseOnAudioNoisy;

		/**
		 * @param context The {@link Context} that is asking for audio focus.
		 */
		public Builder(@NonNull Context context) {

			this.context = context;

			acceptsDelayedFocus = true;
			pauseWhenDucked = false;
			pauseOnAudioNoisy = false;

			myListener = null;

			usage = AudioAttributes.USAGE_UNKNOWN;
			durationHint = AudioManager.AUDIOFOCUS_GAIN;
			contentType = AudioAttributes.CONTENT_TYPE_UNKNOWN;
			stream = AudioManager.USE_DEFAULT_STREAM_TYPE;
		}

		/**
		 * Sets the attribute describing what is the intended use of the audio signal.
		 *
		 * @param usage one of {@link AudioAttributes#USAGE_UNKNOWN},
		 *    {@link AudioAttributes#USAGE_MEDIA},
		 *    {@link AudioAttributes#USAGE_VOICE_COMMUNICATION},
		 *    {@link AudioAttributes#USAGE_VOICE_COMMUNICATION_SIGNALLING},
		 *    {@link AudioAttributes#USAGE_ALARM},
		 *    {@link AudioAttributes#USAGE_NOTIFICATION},
		 *    {@link AudioAttributes#USAGE_NOTIFICATION_RINGTONE},
		 *    {@link AudioAttributes#USAGE_NOTIFICATION_COMMUNICATION_REQUEST},
		 *    {@link AudioAttributes#USAGE_NOTIFICATION_COMMUNICATION_INSTANT},
		 *    {@link AudioAttributes#USAGE_NOTIFICATION_COMMUNICATION_DELAYED},
		 *    {@link AudioAttributes#USAGE_NOTIFICATION_EVENT},
		 *    {@link AudioAttributes#USAGE_ASSISTANT},
		 *    {@link AudioAttributes#USAGE_ASSISTANCE_ACCESSIBILITY},
		 *    {@link AudioAttributes#USAGE_ASSISTANCE_NAVIGATION_GUIDANCE},
		 *    {@link AudioAttributes#USAGE_ASSISTANCE_SONIFICATION},
		 *    {@link AudioAttributes#USAGE_GAME}.
		 * @return The same Builder instance.
		 */
		public Builder setUsage(int usage) {
			this.usage = usage;
			return this;
		}

		/**
		 * Sets the attribute describing the content type of the audio signal, such as
		 * speech, or music.
		 *
		 * @param contentType the content type values, one of
		 *    {@link AudioAttributes#CONTENT_TYPE_MOVIE},
		 *    {@link AudioAttributes#CONTENT_TYPE_MUSIC},
		 *    {@link AudioAttributes#CONTENT_TYPE_SONIFICATION},
		 *    {@link AudioAttributes#CONTENT_TYPE_SPEECH},
		 *    {@link AudioAttributes#CONTENT_TYPE_UNKNOWN}.
		 * @return the same Builder instance.
		 */
		public Builder setContentType(int contentType) {
			this.contentType = contentType;
			return this;
		}

		/**
		 * Sets whether the app will accept delayed focus gain. Default is {@code true}.
		 *
		 * @param acceptsDelayedFocus Whether the app accepts delayed focus gain.
		 * @return The same Builder instance.
		 */
		public Builder setAcceptsDelayedFocus(boolean acceptsDelayedFocus) {
			this.acceptsDelayedFocus = acceptsDelayedFocus;
			return this;
		}

		/**
		 * Sets whether the audio will be paused instead of ducking when
		 * {@link AudioManager#AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK} is received.
		 * Default is
		 * {@code false}.
		 *
		 * @param pauseWhenDucked Whether the audio will be paused instead of ducking.
		 * @return The same Builder instance.
		 */
		public Builder setPauseWhenDucked(boolean pauseWhenDucked) {
			this.pauseWhenDucked = pauseWhenDucked;
			return this;
		}

		/**
		 * Sets the {@link OnAudioFocusChangeListener} that will receive callbacks.
		 *
		 * @param listener The {@link OnAudioFocusChangeListener} implementation that
		 * 	will receive callbacks.
		 * @return The same Builder instance.
		 */
		public Builder setAudioFocusChangeListener(
			@NonNull OnAudioFocusChangeListener listener) {
			this.myListener = listener;
			return this;
		}

		/**
		 * Sets the audio stream for devices lower than Android Oreo.
		 *
		 * @param stream The stream that will be used for playing the audio. Should be
		 * 	one of {@link AudioManager#STREAM_ACCESSIBILITY},
		 *    {@link AudioManager#STREAM_ALARM}, {@link AudioManager#STREAM_DTMF},
		 *    {@link AudioManager#STREAM_MUSIC},
		 *    {@link AudioManager#STREAM_NOTIFICATION},
		 *    {@link AudioManager#STREAM_RING}, {@link AudioManager#STREAM_SYSTEM} or
		 *    {@link AudioManager#STREAM_VOICE_CALL}.
		 * @return The same Builder instance.
		 */
		public Builder setStream(int stream) {
			this.stream = stream;
			return this;
		}

		/**
		 * Sets the duration for which the audio will be played.
		 *
		 * @param durationHint The duration hint, one of
		 *    {@link AudioManager#AUDIOFOCUS_GAIN},
		 *    {@link AudioManager#AUDIOFOCUS_GAIN_TRANSIENT},
		 *    {@link AudioManager#AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE} or
		 *    {@link AudioManager#AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK}.
		 * @return The same Builder instance.
		 */
		public Builder setDurationHint(int durationHint) {
			this.durationHint = durationHint;
			return this;
		}

		/**
		 * Sets whether playback will be paused when audio becomes noisy. Default is
		 * {@code false}.
		 * <p>
		 * If this function is passed {@code true}, a context-registered broadcast
		 * receiver is registered for {@link AudioManager#ACTION_AUDIO_BECOMING_NOISY}.
		 * When this broadcast is received, {@link OnAudioFocusChangeListener#pause()}
		 * will be called, and focus will be abandoned.
		 * </p>
		 *
		 * @param value Whether playback will be paused when audio becomes noisy.
		 * @return The same Builder instance.
		 */
		public Builder setPauseWhenAudioIsNoisy(boolean value) {
			this.pauseOnAudioNoisy = value;
			return this;
		}

		/**
		 * Builds a new {@link AudioFocusController} instance combining all the
		 * information gathered by this {@code Builder}'s configuration methods.
		 * <p>
		 * Throws {@link IllegalStateException} when the listener has not been set.
		 * </p>
		 *
		 * @return the {@link AudioFocusController} instance qualified by all the
		 * 	properties set on this {@code Builder}.
		 */
		@NonNull
		public AudioFocusController build() {
			if (myListener == null) {
				throw new IllegalStateException("Listener cannot be null.");
			}
			return new AudioFocusController(this);
		}
	}

	private AudioFocusController(@NonNull Builder builder) {

		context = builder.context;
		boolean acceptsDelayedFocus = builder.acceptsDelayedFocus;
		pauseWhenDucked = builder.pauseWhenDucked;
		pauseWhenNoisy = builder.pauseOnAudioNoisy;
		myListener = builder.myListener;
		int usage = builder.usage;
		int contentType = builder.contentType;
		streamType = builder.stream;
		durationHint = builder.durationHint;

		volumeDucked = false;

		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

		AudioAttributes attributes = new AudioAttributes.Builder()
			.setUsage(usage)
			.setContentType(contentType)
			.build();

		audioFocusChangeListener = (focusChange) -> {

			switch (focusChange) {

				case AudioManager.AUDIOFOCUS_LOSS -> {
					myListener.pause();
					abandonFocus();
					unregisterReceiver();
				}

				case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
					myListener.pause();
					unregisterReceiver();
				}

				case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
					if (pauseWhenDucked) {
						myListener.pause();
						unregisterReceiver();
					} else {
						myListener.decreaseVolume();
						volumeDucked = true;
					}
				}

				case AudioManager.AUDIOFOCUS_GAIN -> {
					if (volumeDucked) {
						volumeDucked = false;
						myListener.increaseVolume();
					} else {
						myListener.resume();
						registerReceiver();
					}
				}
			}

		};

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			audioFocusRequest = new AudioFocusRequest.Builder(durationHint)
				.setAudioAttributes(attributes)
				.setWillPauseWhenDucked(pauseWhenDucked)
				.setAcceptsDelayedFocusGain(acceptsDelayedFocus)
				.setOnAudioFocusChangeListener(audioFocusChangeListener)
				.build();
		} else {
			audioFocusRequest = null;
		}
	}

	/**
	 * Requests audio focus from the system.
	 * <p>
	 * This function should be called every time you want to start/resume playback. If
	 * the
	 * system grants focus, you will get a call in
	 * {@link OnAudioFocusChangeListener#resume()}.
	 * </p>
	 * <p>
	 * If the system issues delayed focus, or rejects the request, then no callback will
	 * be issued. However, once the system grants full focus after delayed focus has been
	 * issued, the {@link OnAudioFocusChangeListener#resume()} method will be called.
	 * </p>
	 */
	public void requestFocus() {

		int status;

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			status = audioManager.requestAudioFocus(audioFocusRequest);
		} else {
			status = audioManager.requestAudioFocus(audioFocusChangeListener, streamType
				, durationHint);
		}

		if (status == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
			myListener.resume();
			registerReceiver();
			if (volumeDucked) {
				myListener.increaseVolume();
				volumeDucked = false;
			}
		}

	}

	/**
	 * Abandons audio focus.
	 * <p>
	 * Call this method every time you stop/pause playback. This will free audio focus
	 * for
	 * other apps.
	 * </p>
	 */
	public void abandonFocus() {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			audioManager.abandonAudioFocusRequest(audioFocusRequest);
		} else {
			audioManager.abandonAudioFocus(audioFocusChangeListener);
		}

		unregisterReceiver();
	}

	/**
	 * Unregisters the broadcast receiver for
	 * {@link AudioManager#ACTION_AUDIO_BECOMING_NOISY}.
	 */
	private void unregisterReceiver() {
		if (pauseWhenNoisy) {
			try {
				context.unregisterReceiver(broadcastReceiver);
			} catch (Exception ignored) {
			}
		}
	}

	/**
	 * Registers the broadcast receiver for
	 * {@link AudioManager#ACTION_AUDIO_BECOMING_NOISY}.
	 */
	private void registerReceiver() {
		if (pauseWhenNoisy) {
			IntentFilter intentFilter = new IntentFilter(
				AudioManager.ACTION_AUDIO_BECOMING_NOISY);
			try {
				context.registerReceiver(broadcastReceiver, intentFilter);
			} catch (Exception ignored) {
			}
		}
	}

}
