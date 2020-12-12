package in.basulabs.audiofocuscontroller.demoapp;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.os.Build;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Entity_ContentType.class, Entity_DurationHint.class, Entity_StreamType.class, Entity_Usage.class}, version = 1, exportSchema = false)
public abstract class AudioOptionsDatabase extends RoomDatabase {

	private static final String DATABASE_NAME = "in_basulabs_audiofocuscontroller_demoapp_AudioOptionsDatabase";

	private static AudioOptionsDatabase instance;

	public abstract AudioOptionsDAO optionsDAO();

	//----------------------------------------------------------------------------------------------------------------

	public static synchronized AudioOptionsDatabase getInstance(Context context) {

		if (instance == null) {
			instance = Room.databaseBuilder(context.getApplicationContext(), AudioOptionsDatabase.class, DATABASE_NAME)
					.fallbackToDestructiveMigration()
					.allowMainThreadQueries()
					.build();
		}

		fillDatabase();

		return instance;

	}

	//----------------------------------------------------------------------------------------------------------------

	private static void fillDatabase() {

		if (instance.optionsDAO().countUsageEntries() == 0) {
			instance.optionsDAO().addUsage(new Entity_Usage("MEDIA", AudioAttributes.USAGE_MEDIA));
			instance.optionsDAO().addUsage(new Entity_Usage("ALARM", AudioAttributes.USAGE_ALARM));
			instance.optionsDAO().addUsage(new Entity_Usage("ASSISTANCE_ACCESSIBILITY", AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY));
			instance.optionsDAO().addUsage(new Entity_Usage("ASSISTANCE_NAVIGATION_GUIDANCE", AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE));
			instance.optionsDAO().addUsage(new Entity_Usage("ASSISTANCE_SONIFICATION", AudioAttributes.USAGE_ASSISTANCE_SONIFICATION));
			instance.optionsDAO().addUsage(new Entity_Usage("GAME", AudioAttributes.USAGE_GAME));
			instance.optionsDAO().addUsage(new Entity_Usage("NOTIFICATION", AudioAttributes.USAGE_NOTIFICATION));
			instance.optionsDAO().addUsage(new Entity_Usage("NOTIFICATION_COMMUNICATION_DELAYED",
					AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_DELAYED));
			instance.optionsDAO().addUsage(new Entity_Usage("NOTIFICATION_COMMUNICATION_INSTANT",
					AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_INSTANT));
			instance.optionsDAO().addUsage(new Entity_Usage("NOTIFICATION_COMMUNICATION_REQUEST",
					AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_REQUEST));
			instance.optionsDAO().addUsage(new Entity_Usage("NOTIFICATION_EVENT", AudioAttributes.USAGE_NOTIFICATION_EVENT));
			instance.optionsDAO().addUsage(new Entity_Usage("NOTIFICATION_RINGTONE", AudioAttributes.USAGE_NOTIFICATION_RINGTONE));
			instance.optionsDAO().addUsage(new Entity_Usage("VOICE_COMMUNICATION", AudioAttributes.USAGE_VOICE_COMMUNICATION));
			instance.optionsDAO().addUsage(new Entity_Usage("VOICE_COMMUNICATION_SIGNALLING", AudioAttributes.USAGE_VOICE_COMMUNICATION_SIGNALLING));
			instance.optionsDAO().addUsage(new Entity_Usage("UNKNOWN", AudioAttributes.USAGE_UNKNOWN));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				instance.optionsDAO().addUsage(new Entity_Usage("ASSISTANT", AudioAttributes.USAGE_ASSISTANT));
			}
		}

		if (instance.optionsDAO().countDurationHintEntries() == 0) {
			instance.optionsDAO().addDurationHint(new Entity_DurationHint("AUDIOFOCUS_GAIN", AudioManager.AUDIOFOCUS_GAIN));
			instance.optionsDAO().addDurationHint(new Entity_DurationHint("AUDIOFOCUS_GAIN_TRANSIENT", AudioManager.AUDIOFOCUS_GAIN_TRANSIENT));
			instance.optionsDAO()
					.addDurationHint(new Entity_DurationHint("AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE", AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE));
			instance.optionsDAO()
					.addDurationHint(new Entity_DurationHint("AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK", AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK));
		}

		if (instance.optionsDAO().countContentTypeEntries() == 0) {
			instance.optionsDAO().addContentType(new Entity_ContentType("MUSIC", AudioAttributes.CONTENT_TYPE_MUSIC));
			instance.optionsDAO().addContentType(new Entity_ContentType("MOVIE", AudioAttributes.CONTENT_TYPE_MOVIE));
			instance.optionsDAO().addContentType(new Entity_ContentType("SONIFICATION", AudioAttributes.CONTENT_TYPE_SONIFICATION));
			instance.optionsDAO().addContentType(new Entity_ContentType("SPEECH", AudioAttributes.CONTENT_TYPE_SPEECH));
			instance.optionsDAO().addContentType(new Entity_ContentType("UNKNOWN", AudioAttributes.CONTENT_TYPE_UNKNOWN));
		}

		if (instance.optionsDAO().countStreamEntries() == 0) {
			instance.optionsDAO().addStreamType(new Entity_StreamType("MUSIC", AudioManager.STREAM_MUSIC));
			instance.optionsDAO().addStreamType(new Entity_StreamType("ALARM", AudioManager.STREAM_ALARM));
			instance.optionsDAO().addStreamType(new Entity_StreamType("DTMF", AudioManager.STREAM_DTMF));
			instance.optionsDAO().addStreamType(new Entity_StreamType("NOTIFICATION", AudioManager.STREAM_NOTIFICATION));
			instance.optionsDAO().addStreamType(new Entity_StreamType("RING", AudioManager.STREAM_RING));
			instance.optionsDAO().addStreamType(new Entity_StreamType("SYSTEM", AudioManager.STREAM_SYSTEM));
			instance.optionsDAO().addStreamType(new Entity_StreamType("VOICE_CALL", AudioManager.STREAM_VOICE_CALL));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				instance.optionsDAO().addStreamType(new Entity_StreamType("ACCESSIBILITY", AudioManager.STREAM_ACCESSIBILITY));
			}
		}

	}

}
