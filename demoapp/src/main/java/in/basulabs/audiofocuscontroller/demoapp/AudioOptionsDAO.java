package in.basulabs.audiofocuscontroller.demoapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AudioOptionsDAO {

	@Insert(entity = Entity_Usage.class, onConflict = OnConflictStrategy.REPLACE)
	void addUsage(Entity_Usage entity);

	@Insert(entity = Entity_DurationHint.class, onConflict = OnConflictStrategy.REPLACE)
	void addDurationHint(Entity_DurationHint entity);

	@Insert(entity = Entity_ContentType.class, onConflict = OnConflictStrategy.REPLACE)
	void addContentType(Entity_ContentType entity);

	@Insert(entity = Entity_StreamType.class, onConflict = OnConflictStrategy.REPLACE)
	void addStreamType(Entity_StreamType entity);

	@Query("SELECT COUNT(*) FROM usage_entity")
	int countUsageEntries();

	@Query("SELECT COUNT(*) FROM duration_hint_entity")
	int countDurationHintEntries();

	@Query("SELECT COUNT(*) FROM content_type_entity")
	int countContentTypeEntries();

	@Query("SELECT COUNT(*) FROM stream_type_entity")
	int countStreamEntries();

	@Query("SELECT value FROM usage_entity WHERE display_string = :displayString")
	int getUsageValue(String displayString);

	@Query("SELECT value FROM duration_hint_entity WHERE display_string = :displayString")
	int getDurationHintValue(String displayString);

	@Query("SELECT value FROM content_type_entity WHERE display_string = :displayString")
	int getContentTypeValue(String displayString);

	@Query("SELECT value FROM stream_type_entity WHERE display_string = :displayString")
	int getStreamTypeValue(String displayString);

	@Query("SELECT display_string FROM usage_entity WHERE value = :value")
	String getDisplayStringForUsage(int value);

	@Query("SELECT display_string FROM duration_hint_entity WHERE value = :value")
	String getDisplayStringForDurationHint(int value);

	@Query("SELECT display_string FROM content_type_entity WHERE value = :value")
	String getDisplayStringForContentType(int value);

	@Query("SELECT display_string FROM stream_type_entity WHERE value = :value")
	String getDisplayStringForStreamType(int value);

	@Query("SELECT display_string FROM usage_entity")
	List<String> getAllDisplayStringsForUsage();

	@Query("SELECT display_string FROM content_type_entity")
	List<String> getAllDisplayStringsForContentType();

	@Query("SELECT display_string FROM duration_hint_entity")
	List<String> getAllDisplayStringsForDurationHint();

	@Query("SELECT display_string FROM stream_type_entity")
	List<String> getAllDisplayStringsForStreamType();

}
