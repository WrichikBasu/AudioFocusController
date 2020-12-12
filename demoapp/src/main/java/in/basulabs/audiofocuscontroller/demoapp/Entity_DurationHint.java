package in.basulabs.audiofocuscontroller.demoapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "duration_hint_entity")
public class Entity_DurationHint {

	@ColumnInfo(name = "display_string")
	public String displayString;

	@PrimaryKey
	@ColumnInfo
	public int value;

	public Entity_DurationHint(String displayString, int value) {
		this.displayString = displayString;
		this.value = value;
	}

}
